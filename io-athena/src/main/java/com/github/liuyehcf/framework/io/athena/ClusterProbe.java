package com.github.liuyehcf.framework.io.athena;

import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.io.athena.event.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
abstract class ClusterProbe {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterProbe.class);
    private static final String DEFAULT_GROUP = ClusterProbe.class.getSimpleName();
    private static final String KEY_ENVOY = "envoy";
    private static final String KEY_STATUS = "status";
    private static Scheduler scheduler;

    static {
        init();
    }

    private static void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    public static void register(UnsafeEnvoy envoy) {
        AthenaConfig config = envoy.getConfig();

        String quartzIdentifier = envoy.getCluster().getSelf().toString();
        JobDetail jobDetail = JobBuilder.newJob(ConnectionKeepAliveJob.class)
                .withIdentity(quartzIdentifier, DEFAULT_GROUP)
                .build();

        jobDetail.getJobDataMap().put(KEY_ENVOY, envoy);
        jobDetail.getJobDataMap().put(KEY_STATUS, new Status());

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzIdentifier, DEFAULT_GROUP)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(config.getProbeInterval()).repeatForever())
                .startAt(new Date(System.currentTimeMillis() + config.getProbeInterval()))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Throwable e) {
            LOGGER.error("scheduleJob catch exception, errorMsg={}", e.getMessage(), e);
        }
    }

    public static void unRegister(UnsafeEnvoy envoy) {
        String quartzIdentifier = envoy.getCluster().getSelf().toString();
        TriggerKey triggerKey = new TriggerKey(quartzIdentifier, DEFAULT_GROUP);
        try {
            if (!scheduler.unscheduleJob(triggerKey)) {
                LOGGER.error("unScheduleJob failed, trigger={}", triggerKey);
            }
        } catch (Throwable e) {
            LOGGER.error("unScheduleJob catch exception, errorMsg={}", e.getMessage(), e);
        }
    }

    public static final class ConnectionKeepAliveJob implements Job {

        private UnsafeEnvoy envoy;
        private Cluster cluster;
        private Member self;
        private AthenaConfig config;
        private Status status;

        @Override
        public void execute(JobExecutionContext context) {
            try {
                prepare(context);
                commonProbe();
                followerProbe();
                leaderProbe();
            } catch (Throwable e) {
                LOGGER.error("cluster probe catch exception, errorMsg={}", e.getMessage(), e);
            }
        }

        private void prepare(JobExecutionContext context) {
            JobDetail jobDetail = context.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            envoy = (UnsafeEnvoy) jobDataMap.get(KEY_ENVOY);
            cluster = envoy.getCluster();
            self = cluster.getSelf();
            config = envoy.getConfig();
            status = (Status) jobDataMap.get(KEY_STATUS);
        }

        private void commonProbe() {
            joinCluster();
        }

        private void joinCluster() {
            if (!self.getStatus().isInit()) {
                return;
            }

            // forbidding multiply join process
            if (status.isJoining) {
                LOGGER.debug("join process hasn't finished yet");
                return;
            }

            long currentTimestamp = System.currentTimeMillis();

            if (!needStartJoinClusterProcess()) {
                return;
            }

            try {
                // hold lock
                status.isJoining = true;

                boolean hasValidSeed = false;
                for (Address address : config.getSeeds()) {
                    if (!Objects.equals(self, address)) {
                        LeaderQuery query = new LeaderQuery(envoy.nextId());
                        LeaderQueryNotifier.Promise promise = LeaderQueryNotifier.register(query);
                        try {
                            if (envoy.whisperIgnoreStatus(address, query, true)) {
                                try {
                                    LeaderQueryResponse response = promise.get(1, TimeUnit.SECONDS);
                                    if (response.getIdentifier() != null) {
                                        hasValidSeed = true;
                                        break;
                                    }
                                } catch (Throwable e) {
                                    LOGGER.error("query leader timeout, errorMsg={}", e.getMessage(), e);
                                }
                            }
                        } finally {
                            promise.tryCancel();
                        }
                    }
                }

                if (!hasValidSeed) {
                    if (self.isSeed()) {
                        LOGGER.info("[{}] join cluster and assume leader", self);
                        envoy.acceptLeader(self, cluster.getVersion() + 1);
                        envoy.enqueue(new MemberJoiningEvent(self));
                        envoy.enqueue(new MemberActiveEvent(self));
                    } else {
                        LOGGER.error("[{}] failed to connect any seeds, try to connect after {}s", self, config.getRetryInterval());
                    }
                }
            } finally {
                status.lastJoinTimestamp = currentTimestamp;

                // release lock
                status.isJoining = false;
            }
        }

        private boolean needStartJoinClusterProcess() {
            return System.currentTimeMillis() - status.lastJoinTimestamp > config.getRetryInterval() * NumberUtils._1K;
        }

        private void followerProbe() {
            if (!self.getRole().isFollower()) {
                return;
            }

            keepAliveWithLeader();
        }

        private void keepAliveWithLeader() {
            // leader may detect member unreachable, but actually not
            // so in this situation, this follower still can heartbeat with leader to recover from the unreachable status
            if (!self.getStatus().isActive()
                    && !self.getStatus().isUnreachable()) {
                return;
            }

            AthenaConfig config = envoy.getConfig();

            long currentTimestamp = System.currentTimeMillis();
            if (needKeepAliveWithLeader()) {
                // event must be create before check
                LeaderKeepAlive leaderKeepAlive = new LeaderKeepAlive(cluster.getAbstractInfo(), cluster.getVersion());
                Member leader = cluster.getLeader();
                if (leader != null) {
                    leader = leader.copy();
                }

                if (Member.isValidLeader(leader)) {
                    envoy.whisper(leader, leaderKeepAlive);
                    LOGGER.debug("[{}] send 'LeaderKeepAlive' to [{}]", self, leader);
                    status.lastLeaderKeepAliveTimestamp = currentTimestamp;
                }
            }

            if (isLeaderKeepAliveAckTtlTimeout()) {
                if (isLeaderKeepAliveAck3TtlTimeout()) {
                    LOGGER.info("[{}] hasn't receive 'LeaderKeepAliveAck' from [{}] for more then {}s, then try to rejoin the cluster",
                            self, cluster.getLeader(), config.getTtlTimeout() * 3);
                    if (self.isSeed()) {
                        cluster.atomicOperate(() -> {
                            long version = cluster.getVersion();
                            cluster.reset();

                            // keep the current version
                            cluster.setVersion(version);
                        });
                    } else {
                        cluster.atomicOperate(cluster::reset);
                    }
                    return;
                }
                if (needLeaderStatusQuery()) {
                    LOGGER.info("[{}] hasn't receive 'LeaderKeepAliveAck' from [{}] for more than {}s, then start leader status query process",
                            self, cluster.getLeader(), config.getTtlTimeout());
                    cluster.atomicOperate(() -> cluster.setLeader(null));
                    envoy.roar(new LeaderStatusQuery(envoy.nextId(), cluster.getVersion()), true);
                    status.lastQueryLeaderStatusTimestamp = currentTimestamp;
                }
            }
        }

        private boolean needKeepAliveWithLeader() {
            return System.currentTimeMillis() - status.lastLeaderKeepAliveTimestamp > config.getHeartbeatInterval() * NumberUtils._1K;
        }

        private boolean isLeaderKeepAliveAckTtlTimeout() {
            return System.currentTimeMillis() - envoy.getLatestLeaderKeepAliveAckTimestamp() > config.getTtlTimeout() * NumberUtils._1K;
        }

        private boolean isLeaderKeepAliveAck3TtlTimeout() {
            return System.currentTimeMillis() - envoy.getLatestLeaderKeepAliveAckTimestamp() > 3 * config.getTtlTimeout() * NumberUtils._1K;
        }

        private boolean needLeaderStatusQuery() {
            return System.currentTimeMillis() - status.lastQueryLeaderStatusTimestamp > config.getRetryInterval() * NumberUtils._1K;
        }

        private void leaderProbe() {
            if (!self.getRole().isLeader()) {
                return;
            }

            keepAliveWithOtherSeeds();
            updateFollowerStatus();
        }

        private void keepAliveWithOtherSeeds() {
            AthenaConfig config = envoy.getConfig();

            if (needKeepAliveWithOtherSeeds()) {
                for (Address seed : config.getSeeds()) {
                    if (Objects.equals(self, seed)) {
                        continue;
                    }

                    envoy.whisperIgnoreStatus(seed, new SeedKeepAlive(cluster), true);
                }
                status.lastSeedKeepAliveTimestamp = System.currentTimeMillis();
            }
        }

        private boolean needKeepAliveWithOtherSeeds() {
            return System.currentTimeMillis() - status.lastSeedKeepAliveTimestamp > config.getHeartbeatInterval() * NumberUtils._1K;
        }

        private void updateFollowerStatus() {
            List<Member> members = cluster.getMembers();
            for (Member member : members) {
                if (Objects.equals(self, member)) {
                    continue;
                }
                if (member.getStatus().isActive() && isLeaderKeepAliveHeartbeatTimeout(member)) {
                    cluster.atomicOperate(() -> {
                        // double check
                        // between the two checks, leader may complete join process and update the follower keepAliveTimestamp
                        if (member.getStatus().isActive() && isLeaderKeepAliveHeartbeatTimeout(member)) {
                            LOGGER.info("[{}] is moving [{}]'s status to [{}]", self, member, MemberStatus.unreachable);
                            cluster.updateMemberStatus(member, MemberStatus.unreachable);
                            envoy.roar(new MemberStatusUpdate(member, MemberStatus.unreachable), false);
                            envoy.enqueue(new MemberUnreachableEvent(member));
                        }
                    });
                } else if (member.getStatus().isUnreachable() && isLeaderKeepAliveTtlTimeout(member)) {
                    cluster.atomicOperate(() -> {
                        if (member.getStatus().isUnreachable()) {
                            LOGGER.info("[{}] is moving [{}]'s status to [{}]", self, member, MemberStatus.leaving);
                            cluster.updateMemberStatus(member, MemberStatus.leaving);
                            envoy.roar(new MemberStatusUpdate(member, MemberStatus.leaving), false);
                            envoy.enqueue(new MemberLeavingEvent(member));

                            LOGGER.info("[{}] is moving [{}]'s status to [{}]", self, member, MemberStatus.removed);
                            LOGGER.info("[{}] is removing [{}] from the cluster", self, member);
                            cluster.updateMemberStatus(member, MemberStatus.removed);
                            if (cluster.removeMember(member)) {
                                envoy.enqueue(new MemberRemovedEvent(member));
                            }
                            envoy.roar(new MemberStatusUpdate(member, MemberStatus.removed), false);
                        }
                    });
                }
            }
        }

        private boolean isLeaderKeepAliveHeartbeatTimeout(Member member) {
            return System.currentTimeMillis() - envoy.getLatestLeaderKeepAliveTimestamp(member) > config.getHeartbeatTimeout() * NumberUtils._1K;
        }

        private boolean isLeaderKeepAliveTtlTimeout(Member member) {
            return System.currentTimeMillis() - envoy.getLatestLeaderKeepAliveTimestamp(member) > config.getTtlTimeout() * NumberUtils._1K;
        }
    }

    private static final class Status {
        private volatile long lastJoinTimestamp = 0;
        private volatile long lastLeaderKeepAliveTimestamp = 0;
        private volatile long lastSeedKeepAliveTimestamp = 0;
        private volatile long lastQueryLeaderStatusTimestamp = 0;
        private volatile boolean isJoining = false;
    }
}
