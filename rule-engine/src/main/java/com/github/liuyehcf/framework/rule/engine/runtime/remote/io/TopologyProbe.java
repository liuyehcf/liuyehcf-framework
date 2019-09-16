package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.*;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.JoiningRequest;
import com.google.common.collect.Lists;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
abstract class TopologyProbe extends BaseScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologyProbe.class);

    private static final String DEFAULT_GROUP = "topologyProbe";
    private static final String KEY_EVENT_LOOP = "eventLoop";
    private static final String KEY_OTHER_SEEDS = "seeds";

    static void register(ClusterEventLoop clusterEventLoop) {
        RuleProperties properties = clusterEventLoop.getEngine().getProperties();
        int topologyProbeInterval = properties.getTopologyProbeInterval();
        List<MemberConfig> seeds = initOtherSeeds(properties);

        String name = UUID.randomUUID().toString();

        JobDetail jobDetail = JobBuilder.newJob(TopologyProbeJob.class)
                .withIdentity(name, DEFAULT_GROUP)
                .build();

        jobDetail.getJobDataMap().put(KEY_EVENT_LOOP, clusterEventLoop);
        jobDetail.getJobDataMap().put(KEY_OTHER_SEEDS, seeds);

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, DEFAULT_GROUP)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(topologyProbeInterval).repeatForever())
                .startAt(new Date(System.currentTimeMillis() + topologyProbeInterval * 1000))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            clusterEventLoop.addCloseListener((promise) -> {
                if (scheduler.unscheduleJob(trigger.getKey())) {
                    LOGGER.info("unregister topology probe due to cluster event loop shutdown");
                }
            });
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<MemberConfig> initOtherSeeds(RuleProperties properties) {
        List<MemberConfig> otherSeeds = Lists.newArrayList();

        String host = properties.getHost();
        int port = properties.getPort();
        List<MemberConfig> seeds = properties.getClusterConfig().getSeeds();

        for (MemberConfig seed : seeds) {
            if (!Objects.equals(host, seed.getHost())
                    || !Objects.equals(port, seed.getPort())) {
                otherSeeds.add(seed);
            }
        }

        return otherSeeds;
    }

    public static final class TopologyProbeJob implements Job {

        @Override
        @SuppressWarnings("unchecked")
        public void execute(JobExecutionContext context) {
            JobDetail jobDetail = context.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            ClusterEventLoop clusterEventLoop = (ClusterEventLoop) jobDataMap.get(KEY_EVENT_LOOP);
            List<MemberConfig> otherSeeds = (List<MemberConfig>) jobDataMap.get(KEY_OTHER_SEEDS);

            // keep alive with cluster members
            keepAliveWithClusterMember(clusterEventLoop);

            // if self is leader, then repair member with abnormal status
            leaderRepairMemberWithAbnormalStatus(clusterEventLoop);

            // seed init process
            seedInit(clusterEventLoop);

            // normal member init process
            nonSeedInit(clusterEventLoop, otherSeeds);

            // make sure than every member (seed or non-seed) to keep alive with other seeds
            // in order to avoid forming isolated islands
            everyMemberKeepAliveWithSeedToAvoidIsolatedIslands(clusterEventLoop, otherSeeds);
        }

        private void keepAliveWithClusterMember(ClusterEventLoop clusterEventLoop) {
            Topology topology = clusterEventLoop.getTopology();

            for (Member member : topology) {
                // member's status is active means leader think it is active right now (or status update message hasn't reach self member)
                // member's localStatus is unreachable means this member has already detected reachable
                if (!topology.isSelf(member)
                        && member.getStatus().isActive()) {
                    if (clusterEventLoop.hasChannel(member)) {
                        member.setLocalStatus(MemberStatus.active);
                    } else {
                        try {
                            clusterEventLoop.createChannel(member);
                        } catch (Throwable e) {
                            LOGGER.error("create cluster channel catch unknown error, errorMsg={}", e.getMessage());
                        }
                    }
                }
            }
        }

        private void leaderRepairMemberWithAbnormalStatus(ClusterEventLoop clusterEventLoop) {
            Topology topology = clusterEventLoop.getTopology();

            if (!topology.isSelfLeader()) {
                return;
            }

            for (Member member : topology) {
                if (member.getLocalStatus().isUnreachable()) {
                    clusterEventLoop.updateAndBroadcastMemberStatus(member.clone(topology.generateNextMemberId(), MemberRole.follower, MemberStatus.leaving));
                    clusterEventLoop.updateAndBroadcastMemberStatus(member.clone(topology.generateNextMemberId(), MemberRole.follower, MemberStatus.inactive));
                }


                // non-seed member will always keep alive with seed member
                // when only-seed member reboot, it will create a isolated island with one member(itself)
                // then non-seed member will attach this seed-member
                // in this situation, the seed-member is inactive from the perspective of leader
                // so the following process is to correct this abnormal state
                if (member.getStatus().isInactive()
                        && clusterEventLoop.hasChannel(member)) {
                    clusterEventLoop.updateAndBroadcastMemberStatus(member.clone(topology.generateNextMemberId(), MemberRole.follower, MemberStatus.active));
                }
            }
        }

        private void seedInit(ClusterEventLoop clusterEventLoop) {
            Topology topology = clusterEventLoop.getTopology();
            RuleProperties properties = clusterEventLoop.getEngine().getProperties();

            if (!properties.isSeed()
                    || topology.getLeader() != null && topology.getLeader().getLocalStatus().isActive()) {
                return;
            }

            // seed just mark self as leader and let cluster to correct
            LOGGER.info("seed [{}] start as leader", topology.getSelf().getIdentifier());
            while (!topology.assumeLeader(topology.getTransactionId())) ;
        }

        private void nonSeedInit(ClusterEventLoop clusterEventLoop, List<MemberConfig> otherSeeds) {
            Topology topology = clusterEventLoop.getTopology();
            RuleProperties properties = clusterEventLoop.getEngine().getProperties();

            if (properties.isSeed()
                    || !topology.getSelf().getStatus().isInit()) {
                return;
            }

            Collections.shuffle(otherSeeds);
            for (MemberConfig seed : otherSeeds) {
                if (!topology.hasMember(seed)) {
                    try {
                        clusterEventLoop.createChannel(seed);

                        ClusterChannel clusterChannel;
                        if ((clusterChannel = clusterEventLoop.getChannel(seed)) == null) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("member [{}] is joining cluster failed due to seed [{}] is unreachable",
                                        properties.getSelfConfig().getIdentifier(), seed.getIdentifier());
                            }
                            continue;
                        }

                        Member self = topology.getSelf();
                        if (!self.getStatus().isInit()) {
                            topology.addOrReplaceMember(self.clone(Member.INVALID_ID, null, MemberStatus.init));
                            self = topology.getSelf();
                        }

                        clusterChannel.write(new JoiningRequest(self, false));

                        LOGGER.info("member [{}] is joining cluster through [{}]",
                                properties.getSelfConfig().getIdentifier(), seed.getIdentifier());

                        break;
                    } catch (Throwable e) {
                        LOGGER.warn("member [{}] joining cluster catch unknown error, errorMsg={}",
                                properties.getSelfConfig().getIdentifier(), e.getMessage(), e);
                    }
                }
            }
        }

        private void everyMemberKeepAliveWithSeedToAvoidIsolatedIslands(ClusterEventLoop clusterEventLoop, List<MemberConfig> otherSeeds) {
            RuleProperties properties = clusterEventLoop.getEngine().getProperties();

            for (MemberConfig seed : otherSeeds) {
                if (clusterEventLoop.hasChannel(seed)) {
                    continue;
                }

                try {
                    clusterEventLoop.createChannel(seed);

                    if (clusterEventLoop.getChannel(seed) == null) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("member [{}] keep alive with seed failed due to seed [{}] is unreachable",
                                    properties.getSelfConfig().getIdentifier(), seed.getIdentifier());
                        }
                        return;
                    }

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("member [{}] is keep alive with seed [{}]",
                                properties.getSelfConfig().getIdentifier(), seed.getIdentifier());
                    }
                } catch (Throwable e) {
                    LOGGER.warn("member [{}] keep alive with seed catch unknown error, errorMsg={}",
                            properties.getSelfConfig().getIdentifier(), e.getMessage(), e);
                }
            }
        }
    }
}
