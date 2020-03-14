package com.github.liuyehcf.framework.io.athena.test;

import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.common.tools.time.TimeUtils;
import com.github.liuyehcf.framework.io.athena.*;
import com.github.liuyehcf.framework.io.athena.event.*;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * offline leader之后，预计多少时间可以选出leader
 * ttlTimeout 之后，开始选leader，在短时间内会选出leader
 * ttlTimeout + heartbeatTimeout 之后，会将之前offline的节点标记为unreachable
 * ttlTimeout + ttlTimeout之后，会将之前offline的节点标记为leaving,removed并移出集群
 * ttlTimeout + ttlTimeout + 2之后，集群应该稳定了
 *
 * @author hechenfeng
 * @date 2020/2/6
 */
public class TestEnvoy {

    private static final Logger LOGGER = getLogger(TestEnvoy.class);

    private static final int PORT_OFFSET = 10000;
    private static final int TOTAL_NUM = 3;
    private static final Random RANDOM = new Random();
    private final List<Envoy> envoys = Lists.newArrayList();
    private final ArrayList<AtomicInteger> joiningCounts = Lists.newArrayList();
    private final ArrayList<AtomicInteger> activeCounts = Lists.newArrayList();
    private final ArrayList<AtomicInteger> unreachableCounts = Lists.newArrayList();
    private final ArrayList<AtomicInteger> leavingCounts = Lists.newArrayList();
    private final ArrayList<AtomicInteger> removedCounts = Lists.newArrayList();

    @After
    public void after() {
        envoys.forEach(Envoy::stop);

        joiningCounts.clear();
        activeCounts.clear();
        unreachableCounts.clear();
        leavingCounts.clear();
        removedCounts.clear();

        envoys.clear();
    }

    @Test
    public void testConfigCheck() {
        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(0);
            return Envoy.create(config);
        }, "totalNum must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(-1);
            return Envoy.create(config);
        }, "totalNum must be positive");

//        assertException(() -> {
//            AthenaConfig config = new AthenaConfig();
//            config.setTotalNum(3);
//            config.setHost("unknown");
//            return createEnvoy(config);
//        }, "unknown host");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.2");
            return Envoy.create(config);
        }, "ip matches non of the local ips");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setPort(0);
            return Envoy.create(config);
        }, "port is invalid");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setPort(65536);
            return Envoy.create(config);
        }, "port is invalid");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setName("");
            return Envoy.create(config);
        }, "name is blank");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            return Envoy.create(config);
        }, "seeds is empty");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(null, null));
            return Envoy.create(config);
        }, "seed");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("", 12222)));
            return Envoy.create(config);
        }, "seed's host is blank");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 0)));
            return Envoy.create(config);
        }, "seed's port is invalid");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 65536)));
            return Envoy.create(config);
        }, "seed's port is invalid");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setProbeInterval(0);
            return Envoy.create(config);
        }, "probeInterval must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setHeartbeatInterval(0);
            return Envoy.create(config);
        }, "heartbeatInterval must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setHeartbeatTimeout(0);
            return Envoy.create(config);
        }, "heartbeatTimeout must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setTtlTimeout(0);
            return Envoy.create(config);
        }, "ttlTimeout must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setRetryInterval(0);
            return Envoy.create(config);
        }, "retryInterval must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setThreadNum(0);
            return Envoy.create(config);
        }, "threadNum must be positive");

        assertException(() -> {
            AthenaConfig config = new AthenaConfig();
            config.setTotalNum(3);
            config.setHost("127.0.0.1");
            config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 12222)));
            config.setThreadNum(1);
            return Envoy.create(config);
        }, "workerId must between 1 and 1023");
    }

    @Test
    public void testRecoverFromUnreachable() {
        Envoy envoySeed = createEnvoy(7,
                1, Lists.newArrayList(1),
                5,
                2,
                20,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoySeed.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        Envoy normal = createEnvoy(7,
                2, Lists.newArrayList(1),
                1,
                2,
                20,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        normal.start();

        for (int i = 3; i <= 7; i++) {
            Envoy envoy = createEnvoy(7,
                    i, Lists.newArrayList(1),
                    8,
                    4,
                    20,
                    AthenaConfig.DEFAULT_RETRY_INTERVAL);

            envoy.start();
        }

        TimeUtils.sleep(6, TimeUnit.SECONDS);
        // this time all the followers haven't send LeaderKeepAlive
        assertCnt(1,
                7, 7,
                7, 7,
                5, 5,
                0, 0,
                0, 0);
        assertCnt(2,
                1, 6,
                7, 7,
                5, 5,
                0, 0,
                0, 0);

        TimeUtils.sleep(4, TimeUnit.SECONDS);
        // this time all the followers have send LeaderKeepAlive
        assertCnt(1,
                7, 7,
                12, 12,
                5, 5,
                0, 0,
                0, 0);
        assertCnt(2,
                1, 6,
                12, 12,
                5, 5,
                0, 0,
                0, 0);
    }

    @Test
    public void testMultiSeedsConflict() {
        // member 1
        Envoy envoy1 = createEnvoy(3,
                1, Lists.newArrayList(1, 2),
                3,
                AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                AthenaConfig.DEFAULT_TTL_TIMEOUT,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoy1.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        // member 2
        Envoy envoy2 = createEnvoy(3,
                2, Lists.newArrayList(2),
                3,
                AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                AthenaConfig.DEFAULT_TTL_TIMEOUT,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoy2.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        // member 3
        Envoy envoy3 = createEnvoy(3,
                3, Lists.newArrayList(1, 2, 3),
                3,
                AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                AthenaConfig.DEFAULT_TTL_TIMEOUT,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoy3.start();

        TimeUtils.sleep(10, TimeUnit.SECONDS);

        assertCnt(1,
                3, 4,
                5, 5,
                0, 0,
                0, 0,
                0, 0);
        assertCnt(2,
                3, 3,
                3, 3,
                0, 0,
                0, 0,
                0, 0);
        assertCnt(3,
                2, 3,
                5, 5,
                0, 0,
                0, 0,
                0, 0);
        assertEnvoyEquals(1, 3, envoy1, envoy2, envoy3);
    }

    @Test
    public void testLeaderUnreachable() {
        Envoy envoy1 = createEnvoy(3,
                1, Lists.newArrayList(1),
                3,
                4,
                5,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoy1.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        Envoy envoy2 = createEnvoy(3,
                2, Lists.newArrayList(1),
                3,
                4,
                5,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoy2.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        Envoy envoy3 = createEnvoy(3,
                3, Lists.newArrayList(1),
                3,
                4,
                5,
                AthenaConfig.DEFAULT_RETRY_INTERVAL);
        envoy3.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        envoy1.stop();

        TimeUtils.sleep(15, TimeUnit.SECONDS);

        assertCnt(1,
                3, 3,
                3, 3,
                0, 0,
                0, 0,
                0, 0);
        assertCnt(2,
                2, 2,
                3, 3,
                1, 1,
                1, 1,
                1, 1);
        assertCnt(3,
                1, 1,
                3, 3,
                1, 1,
                1, 1,
                1, 1);
        assertEnvoyEquals(2, 2, envoy2, envoy3);
    }

    @Test
    public void testMemberEvent() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testMemberEvent(i);
        }
    }

    @Test
    public void testOneSeedBootOneByOne() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testOneSeedBootOneByOne(i);
        }
    }

    @Test
    public void testOneSeedFirstBoot() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testOneSeedFirstBoot(i);
        }
    }

    @Test
    public void testOneSeedLastBoot() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testOneSeedLastBoot(i);
        }
    }

    @Test
    public void testOneSeedBootConcurrently() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testOneSeedBootConcurrently(i);
        }
    }

    @Test
    public void testMultiplySeedBootConcurrently() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testMultiplySeedBootConcurrently(i);
        }
    }

    @Test
    public void testAllSeedBootOneByOne() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testAllSeedBootOneByOne(i);
        }
    }

    @Test
    public void testAllSeedBootConcurrently() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testAllSeedBootConcurrently(i);
        }
    }

    @Test
    public void testContinuesShutdown() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testContinuesShutdown(i);
        }
    }

    @Test
    public void testContinuesLeaderElection() {
        for (int i = 1; i <= TOTAL_NUM; i++) {
            testContinuesLeaderElection(i);
        }
    }

    @Test
    public void testMajorityBoundary() {
        for (int i = 3; i <= TOTAL_NUM; i++) {
            testMajorityBoundary(i);
        }
    }

    @Test
    public void testContinuesReboot() {
        for (int i = 3; i <= TOTAL_NUM; i++) {
            testContinuesReboot(i);
        }
    }

    @Test
    public void testRecoverFromMinority() {
        for (int i = 3; i <= TOTAL_NUM; i++) {
            testRecoverFromMinority(i);
        }
    }

    @Test
    public void testMemoryLeak() {
        Envoy envoy1 = createEnvoy(2,
                1, Lists.newArrayList(1),
                1,
                2,
                3,
                2);
        Envoy envoy2 = createEnvoy(2,
                2, Lists.newArrayList(1),
                1,
                2,
                3,
                2);

        envoy1.start();
        TimeUtils.sleep(1, TimeUnit.SECONDS);
        envoy2.start();
        TimeUtils.sleep(1, TimeUnit.SECONDS);

        AtomicLong count = new AtomicLong();
        envoy2.registerReceiver(ReceiverBuilder.create()
                .match(byte[].class, (bytes) -> {
                    count.addAndGet(bytes.length);
                    LOGGER.error("receive byte length={}, totalLength={}M",
                            bytes.length, count.get() / NumberUtils._1M);
                })
                .build());

        assertEnvoyEquals(1, 2, envoy1, envoy2);

        Address address = Address.of(AthenaConfig.DEFAULT_HOST, 10002);
        for (int i = 0; i < 20000; i++) {
            if (envoy1.whisper(address, new byte[NumberUtils._1M])) {
                TimeUtils.sleep(10, TimeUnit.MILLISECONDS);
            }
        }

        assertEnvoyEquals(1, 2, envoy1, envoy2);
    }

    private void testMemberEvent(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start one by one, seed first
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    AthenaConfig.DEFAULT_RETRY_INTERVAL));
        }
        for (int i = 0; i < totalNum; i++) {
            Envoy envoy = envoys.get(i);
            LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), i + 1, totalNum);
            envoy.start();
            TimeUtils.sleep(1, TimeUnit.SECONDS);
        }
        assertEnvoyEquals(1, totalNum, envoys);

        // stop follower one by one
        for (int i = totalNum - 1; i >= 0; i--) {
            Envoy envoy = envoys.get(i);
            LOGGER.error("[{}] stop, index={}; totalNum={}", envoy.getCluster().getSelf(), i + 1, totalNum);
            envoy.stop();
            TimeUtils.sleep(5, TimeUnit.SECONDS);
        }
        for (int i = 0; i < totalNum; i++) {
            assertCnt(i + 1,
                    totalNum - i, totalNum - i,
                    totalNum, totalNum,
                    totalNum - i - 1, totalNum - i - 1,
                    totalNum - i - 1, totalNum - i - 1,
                    totalNum - i - 1, totalNum - i - 1);
        }

        after();
    }

    private void testOneSeedBootOneByOne(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start one by one in random order
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                    AthenaConfig.DEFAULT_TTL_TIMEOUT,
                    2));
        }
        Collections.shuffle(envoys);
        for (int i = 0; i < totalNum; i++) {
            Envoy envoy = envoys.get(i);
            LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), i + 1, totalNum);
            envoy.start();
            TimeUtils.sleep(1, TimeUnit.SECONDS);
        }
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(1, totalNum, envoys);

        after();
    }

    private void testOneSeedFirstBoot(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    AthenaConfig.DEFAULT_HEARTBEAT_INTERVAL,
                    AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                    AthenaConfig.DEFAULT_TTL_TIMEOUT,
                    AthenaConfig.DEFAULT_RETRY_INTERVAL));
        }
        // start seed
        Envoy envoy = envoys.get(0);
        LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), 1, totalNum);
        envoy.start();
        TimeUtils.sleep(2, TimeUnit.SECONDS);
        // start followers
        for (int i = 1; i < totalNum; i++) {
            envoy = envoys.get(i);
            LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), i + 1, totalNum);
            envoy.start();
        }
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(1, totalNum, envoys);

        after();
    }

    private void testOneSeedLastBoot(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                    AthenaConfig.DEFAULT_TTL_TIMEOUT,
                    2));
        }
        // start followers
        for (int i = 1; i < totalNum; i++) {
            Envoy envoy = envoys.get(i);
            LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), i + 1, totalNum);
            envoy.start();
        }
        TimeUtils.sleep(1, TimeUnit.SECONDS);
        // start seed
        Envoy envoy = envoys.get(0);
        LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), 1, totalNum);
        envoy.start();
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(1, totalNum, envoys);

        after();
    }

    private void testOneSeedBootConcurrently(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start concurrently, no leader conflict because of one seed
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                    AthenaConfig.DEFAULT_TTL_TIMEOUT,
                    2));
        }
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(null, totalNum, envoys);

        after();
    }

    private void testMultiplySeedBootConcurrently(int totalNum) {
        List<Integer> seedIndexes = Lists.newArrayList();
        while (seedIndexes.isEmpty()) {
            for (int i = 0; i < totalNum; i++) {
                if (RANDOM.nextBoolean()) {
                    if (!seedIndexes.contains(i + 1)) {
                        seedIndexes.add(i + 1);
                    }
                }
            }
        }

        LOGGER.error("totalNum={}; seedIndexes={}", totalNum, seedIndexes);

        // start concurrently, leader conflict may happen
        // so if member is in the process of joining, then it cannot receive ReJoinCluster event
        // because leader only send RejoinCluster to active members
        // so wo need to wait more 3 times of ttlTimeout to force the member rejoin the cluster
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    2));
        }
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(15, TimeUnit.SECONDS);
        assertEnvoyEquals(null, totalNum, envoys);

        after();
    }

    private void testAllSeedBootOneByOne(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            seedIndexes.add(i + 1);
        }

        // start one by one at random order
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    AthenaConfig.DEFAULT_HEARTBEAT_INTERVAL,
                    AthenaConfig.DEFAULT_HEARTBEAT_TIMEOUT,
                    AthenaConfig.DEFAULT_TTL_TIMEOUT,
                    AthenaConfig.DEFAULT_RETRY_INTERVAL));
        }
        Collections.shuffle(envoys);
        for (int i = 0; i < totalNum; i++) {
            Envoy envoy = envoys.get(i);
            LOGGER.error("[{}] start, index={}; totalNum={}", envoy.getCluster().getSelf(), i + 1, totalNum);
            envoy.start();
            TimeUtils.sleep(1, TimeUnit.SECONDS);
        }
        TimeUtils.sleep(3, TimeUnit.SECONDS);
        assertEnvoyEquals(1, totalNum, envoys);

        after();
    }

    private void testAllSeedBootConcurrently(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            seedIndexes.add(i + 1);
        }

        // start concurrently, leader conflict may happen
        // so if member is in the process of joining, then it cannot receive ReJoinCluster event
        // because leader only send RejoinCluster to active members
        // so wo need to wait more 3 times of ttlTimeout to force the member rejoin the cluster
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    2));
        }
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(15, TimeUnit.SECONDS);
        assertEnvoyEquals(null, totalNum, envoys);

        after();
    }

    private void testContinuesShutdown(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        int majority = NumberUtils.majorityOf(totalNum);
        int version = 0;
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start concurrently, no leader conflict because of one seed
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    2));
        }
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(++version, totalNum, envoys);

        Collections.shuffle(envoys);
        Iterator<Envoy> iterator = envoys.iterator();
        int activeNum = envoys.size();
        while (envoys.size() > majority) {
            activeNum--;
            Envoy envoy = iterator.next();
            if (Member.isValidLeader(envoy.getCluster().getSelf())) {
                LOGGER.error("offline leader [{}]; activeNum={}; totalNum={}", envoy.getCluster().getSelf(), activeNum, totalNum);
                envoy.stop();
                iterator.remove();
                TimeUtils.sleep(9, TimeUnit.SECONDS);
                assertEnvoyEquals(++version, activeNum, envoys);
            } else {
                LOGGER.error("offline follower [{}]; activeNum={}; totalNum={}", envoy.getCluster().getSelf(), activeNum, totalNum);
                envoy.stop();
                iterator.remove();
                TimeUtils.sleep(5, TimeUnit.SECONDS);
                assertEnvoyEquals(version, activeNum, envoys);
            }
        }

        after();
    }

    private void testContinuesLeaderElection(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        int majority = NumberUtils.majorityOf(totalNum);
        int version = 0;
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start concurrently, no leader conflict because of one seed
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    2));
        }
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(++version, totalNum, envoys);

        // continues stop leader
        int activeNum = totalNum;
        while (activeNum > majority) {
            activeNum--;
            int leaderIndex = -1;
            for (int i = 0; i < activeNum + 1; i++) {
                Envoy envoy = envoys.get(i);
                if (envoy.getCluster().getSelf().getRole().isLeader()) {
                    leaderIndex = i;
                }
            }
            Assert.assertNotEquals(-1, leaderIndex);
            Envoy envoy = envoys.get(leaderIndex);
            LOGGER.error("offline leader [{}]; activeNum={}; totalNum={}", envoy.getCluster().getSelf(), activeNum, totalNum);
            envoy.stop();
            envoys.remove(leaderIndex);
            TimeUtils.sleep(9, TimeUnit.SECONDS);
            assertEnvoyEquals(++version, activeNum, envoys);
        }

        // stop the final leader, then cluster cannot elect another leader
        activeNum--;
        int leaderIndex = -1;
        for (int i = 0; i < activeNum + 1; i++) {
            Envoy envoy = envoys.get(i);
            if (envoy.getCluster().getSelf().getRole().isLeader()) {
                leaderIndex = i;
            }
        }
        Assert.assertNotEquals(-1, leaderIndex);
        Envoy envoy = envoys.get(leaderIndex);
        LOGGER.error("offline leader [{}]", envoy.getCluster().getSelf());
        envoy.stop();
        envoys.remove(leaderIndex);
        TimeUtils.sleep(9, TimeUnit.SECONDS);
        for (Envoy envoy1 : envoys) {
            Assert.assertNull(envoy1.getCluster().getLeader());
        }

        after();
    }

    private void testMajorityBoundary(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        int majority = NumberUtils.majorityOf(totalNum);
        int unreachableNum = totalNum - majority;
        List<Integer> seedIndexes = Lists.newArrayList(1);

        // start concurrently, no leader conflict because of one seed
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    2));
        }
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(5, TimeUnit.SECONDS);
        assertEnvoyEquals(1, totalNum, envoys);

        // stop leader
        Assert.assertTrue(Member.isValidLeader(envoys.get(0).getCluster().getSelf()));
        LOGGER.error("offline leader [{}]", envoys.get(0).getCluster().getSelf());
        envoys.get(0).stop();
        envoys.remove(0);
        // stop some followers and let cluster's active member reach majority
        Collections.shuffle(envoys);
        int i = 0;
        Iterator<Envoy> iterator = envoys.iterator();
        while (i < unreachableNum - 1) {
            Assert.assertTrue(iterator.hasNext());
            Envoy envoy = iterator.next();
            LOGGER.error("offline [{}]", envoy.getCluster().getSelf());
            envoy.stop();
            iterator.remove();
            i++;
        }
        TimeUtils.sleep(9, TimeUnit.SECONDS);
        assertEnvoyEquals(2, majority, envoys);

        after();
    }

    private void testContinuesReboot(int totalNum) {
        LOGGER.error("totalNum={}", totalNum);
        int version = 0;
        List<Integer> seedIndexes = Lists.newArrayList(1, 2);

        // seed1 start first, then others start concurrently
        List<Envoy> envoys = Lists.newArrayList();
        for (int i = 0; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    2));
        }
        envoys.get(0).start();
        TimeUtils.sleep(2, TimeUnit.SECONDS);
        envoys.forEach(Envoy::start);
        TimeUtils.sleep(3, TimeUnit.SECONDS);
        assertEnvoyEquals(++version, totalNum, envoys);

        Collections.shuffle(envoys);
        for (int i = 0; i < envoys.size(); i++) {
            Envoy envoy = envoys.get(i);

            // shutdown
            if (Member.isValidLeader(envoy.getCluster().getSelf())) {
                LOGGER.error("offline leader [{}]; activeNum={}; totalNum={}", envoy.getCluster().getSelf(), totalNum - 1, totalNum);
                envoy.stop();
                TimeUtils.sleep(9, TimeUnit.SECONDS);
                List<Envoy> envoysCopy = Lists.newArrayList(envoys);
                envoysCopy.remove(envoy);
                assertEnvoyEquals(++version, totalNum - 1, envoysCopy);
            } else {
                LOGGER.error("offline follower [{}]; activeNum={}; totalNum={}", envoy.getCluster().getSelf(), totalNum - 1, totalNum);
                envoy.stop();
                TimeUtils.sleep(5, TimeUnit.SECONDS);
                List<Envoy> envoysCopy = Lists.newArrayList(envoys);
                envoysCopy.remove(envoy);
                assertEnvoyEquals(version, totalNum - 1, envoysCopy);
            }

            // restart
            envoy.start();
            TimeUtils.sleep(2, TimeUnit.SECONDS);
            assertEnvoyEquals(version, totalNum, envoys);
        }

        after();
    }

    private void testRecoverFromMinority(int totalNum) {
        int majority = NumberUtils.majorityOf(totalNum);
        LOGGER.error("totalNum={}", totalNum);
        List<Integer> seedIndexes = Lists.newArrayList(1, 2);

        List<Envoy> envoys = Lists.newArrayList();

        // start seed1 first, let be the leader
        Envoy envoy1 = createEnvoy(totalNum,
                1, seedIndexes,
                1,
                2,
                3,
                3);
        envoys.add(envoy1);
        envoy1.start();

        TimeUtils.sleep(1, TimeUnit.SECONDS);

        for (int i = 1; i < totalNum; i++) {
            envoys.add(createEnvoy(totalNum,
                    i + 1, seedIndexes,
                    1,
                    2,
                    3,
                    3));
        }

        envoys.forEach(Envoy::start);

        TimeUtils.sleep(5, TimeUnit.SECONDS);

        assertEnvoyEquals(1, totalNum, envoys);

        // offline leader
        Assert.assertTrue(Member.isValidLeader(envoy1.getCluster().getSelf()));
        LOGGER.error("offline leader [{}]", envoy1.getCluster().getSelf());
        envoy1.stop();

        TimeUtils.sleep(9, TimeUnit.SECONDS);

        // another leader must elected
        assertEnvoyEquals(2, totalNum - 1, envoys.subList(1, envoys.size()));

        // restart envoy1, it will join the cluster through envoy2(another seed)
        envoy1.start();
        TimeUtils.sleep(5, TimeUnit.SECONDS);

        assertEnvoyEquals(2, totalNum, envoys);

        // find new leader
        int leaderIndex = -1;
        for (int i = 1; i < envoys.size(); i++) {
            Envoy envoy = envoys.get(i);
            if (envoy.getCluster().getSelf().getRole().isLeader()) {
                leaderIndex = i;
            }
        }

        Assert.assertNotEquals(-1, leaderIndex);
        Assert.assertNotEquals(0, leaderIndex);

        // if seed2 is leader
        if (leaderIndex == 1) {
            envoys.get(leaderIndex).stop();
            envoys.remove(leaderIndex);
        }
        // if seed2 is not leader
        else {
            envoys.get(leaderIndex).stop();
            envoys.remove(leaderIndex);

            envoys.get(1).stop();
            envoys.remove(1);
        }


        while (envoys.size() >= majority) {
            Collections.shuffle(envoys);

            Envoy envoy = envoys.get(0);
            if (envoy == envoy1) {
                continue;
            }
            envoy.stop();
            envoys.remove(0);
        }

        // after 3 * ttlTimeout seed will make itself as leader,and other members rejoin the cluster
        TimeUtils.sleep(15, TimeUnit.SECONDS);

        Assert.assertEquals(majority - 1, envoys.size());

        assertEnvoyEquals(3, majority - 1, envoys);

        after();
    }

    private Envoy createEnvoy(int totalNum, int index, List<Integer> seedIndexes,
                              int heartbeatInterval, int heartbeatTimeout, int ttlTimeout,
                              int retryInterval) {
        AthenaConfig config = new AthenaConfig();

        config.setTotalNum(totalNum);
        config.setHost(AthenaConfig.DEFAULT_HOST);
        config.setPort(PORT_OFFSET + index);
        config.setHeartbeatInterval(heartbeatInterval);
        config.setHeartbeatTimeout(heartbeatTimeout);
        config.setTtlTimeout(ttlTimeout);
        config.setRetryInterval(retryInterval);
        config.setWorkerId(index);

        List<Address> seeds = Lists.newArrayList();
        for (Integer seedIndex : seedIndexes) {
            seeds.add(Address.of(AthenaConfig.DEFAULT_HOST, PORT_OFFSET + seedIndex));
        }
        config.setSeeds(seeds);

        Envoy envoy = Envoy.create(config);

        while (joiningCounts.size() < totalNum) {
            joiningCounts.add(new AtomicInteger());
        }
        AtomicInteger joiningCnt = joiningCounts.get(index - 1);

        while (activeCounts.size() < totalNum) {
            activeCounts.add(new AtomicInteger());
        }
        AtomicInteger activeCnt = activeCounts.get(index - 1);

        while (unreachableCounts.size() < totalNum) {
            unreachableCounts.add(new AtomicInteger());
        }
        AtomicInteger unreachableCnt = unreachableCounts.get(index - 1);

        while (leavingCounts.size() < totalNum) {
            leavingCounts.add(new AtomicInteger());
        }
        AtomicInteger leavingCnt = leavingCounts.get(index - 1);

        while (removedCounts.size() < totalNum) {
            removedCounts.add(new AtomicInteger());
        }
        AtomicInteger removedCnt = removedCounts.get(index - 1);

        envoy.registerReceiver(ReceiverBuilder.create()
                .match(MemberJoiningEvent.class, (event) -> joiningCnt.incrementAndGet())
                .match(MemberActiveEvent.class, (event) -> activeCnt.incrementAndGet())
                .match(MemberUnreachableEvent.class, (event) -> unreachableCnt.incrementAndGet())
                .match(MemberLeavingEvent.class, (event) -> leavingCnt.incrementAndGet())
                .match(MemberRemovedEvent.class, (event) -> removedCnt.incrementAndGet())
                .build());

        envoys.add(envoy);
        return envoy;
    }

    private void assertException(Callable<?> callable, String message) {
        try {
            callable.call();
        } catch (Throwable e) {
            Assert.assertEquals(message, e.getMessage());
            return;
        }

        throw new RuntimeException();
    }

    private void assertCnt(int index,
                           int minJoiningCount, int maxJoiningCount,
                           int minActiveCount, int maxActiveCount,
                           int minUnreachableCount, int maxUnreachableCount,
                           int minLeavingCount, int maxLeavingCount,
                           int minRemovedCount, int maxRemovedCount) {
        LOGGER.error("assertCnt");

        Assert.assertTrue(joiningCounts.size() >= index);
        Assert.assertTrue(activeCounts.size() >= index);
        Assert.assertTrue(unreachableCounts.size() >= index);
        Assert.assertTrue(leavingCounts.size() >= index);
        Assert.assertTrue(unreachableCounts.size() >= index);

        AtomicInteger joiningCount = joiningCounts.get(index - 1);
        AtomicInteger activeCount = activeCounts.get(index - 1);
        AtomicInteger unreachableCount = unreachableCounts.get(index - 1);
        AtomicInteger leavingCount = leavingCounts.get(index - 1);
        AtomicInteger removedCount = removedCounts.get(index - 1);

        Assert.assertNotNull(joiningCount);
        Assert.assertNotNull(activeCount);
        Assert.assertNotNull(unreachableCount);
        Assert.assertNotNull(leavingCount);
        Assert.assertNotNull(removedCount);

        if (!(minJoiningCount <= joiningCount.get() && joiningCount.get() <= maxJoiningCount)) {
            LOGGER.error("joiningCount mismatch, index={}; min={}; max={}; actual={}",
                    index, minJoiningCount, maxJoiningCount, joiningCount.get());
        }
        Assert.assertTrue(minJoiningCount <= joiningCount.get() && joiningCount.get() <= maxJoiningCount);

        if (!(minActiveCount <= activeCount.get() && activeCount.get() <= maxActiveCount)) {
            LOGGER.error("activeCount mismatch, index={}; min={}; max={}; actual={}",
                    index, minActiveCount, maxActiveCount, activeCount.get());
        }
        Assert.assertTrue(minActiveCount <= activeCount.get() && activeCount.get() <= maxActiveCount);

        if (!(minUnreachableCount <= unreachableCount.get() && unreachableCount.get() <= maxUnreachableCount)) {
            LOGGER.error("unreachableCount mismatch, index={}; min={}; max={}; actual={}",
                    index, minUnreachableCount, maxUnreachableCount, unreachableCount.get());
        }
        Assert.assertTrue(minUnreachableCount <= unreachableCount.get() && unreachableCount.get() <= maxUnreachableCount);

        if (!(minLeavingCount <= leavingCount.get() && leavingCount.get() <= maxLeavingCount)) {
            LOGGER.error("leavingCount mismatch, index={}; min={}; max={}; actual={}",
                    index, minLeavingCount, maxLeavingCount, leavingCount.get());
        }
        Assert.assertTrue(minLeavingCount <= leavingCount.get() && leavingCount.get() <= maxLeavingCount);

        if (!(minRemovedCount <= removedCount.get() && removedCount.get() <= maxRemovedCount)) {
            LOGGER.error("removedCount mismatch, index={}; min={}; max={}; actual={}",
                    index, minRemovedCount, maxRemovedCount, removedCount.get());
        }
        Assert.assertTrue(minRemovedCount <= removedCount.get() && removedCount.get() <= maxRemovedCount);
    }

    private void assertEnvoyEquals(Integer version, int activeNum, Envoy... envoys) {
        assertEnvoyEquals(version, activeNum, Lists.newArrayList(envoys));
    }

    private void assertEnvoyEquals(Integer version, int activeNum, List<Envoy> envoys) {
        LOGGER.error("assertEnvoyEquals");

        Cluster firstCluster = envoys.get(0).getCluster();
        long actualVersion = firstCluster.getVersion();
        if (version != null) {
            if (version != actualVersion) {
                LOGGER.error("version mismatch, version={}; actualVersion={}; \ncluster={}",
                        version, actualVersion, firstCluster.toReadableString());
            }
            Assert.assertEquals((int) version, actualVersion);
        }
        long actualActiveNum = firstCluster.getMembers()
                .stream()
                .filter(m -> m.getStatus().isActive())
                .count();
        if (activeNum != actualActiveNum) {
            LOGGER.error("activeNum mismatch, activeNum={}; actualActiveNum={}; \ncluster={}",
                    activeNum, actualActiveNum, firstCluster.toReadableString());
        }
        Assert.assertEquals(activeNum, actualActiveNum);

        boolean isLeaderValid = Member.isValidLeader(firstCluster.getLeader());
        if (!isLeaderValid) {
            LOGGER.error("leader invalid, \ncluster={}", firstCluster.toReadableString());
        }
        Assert.assertTrue(isLeaderValid);

        for (int i = 1; i < envoys.size(); i++) {
            Cluster cluster = envoys.get(i).getCluster();
            isLeaderValid = Member.isValidLeader(cluster.getLeader());
            if (!isLeaderValid) {
                LOGGER.error("cluster leader invalid, \nfirstCluster={}; \ncluster={}", firstCluster.toReadableString(), cluster.toReadableString());
            }
            if (!Objects.equals(firstCluster.getLeader(), cluster.getLeader())) {
                LOGGER.error("cluster leader mismatch, \nfirstCluster={}; \ncluster={}", firstCluster.toReadableString(), cluster.toReadableString());
            }
            Assert.assertEquals(firstCluster.getLeader(), cluster.getLeader());

            if (firstCluster.getVersion() != cluster.getVersion()) {
                LOGGER.error("cluster version mismatch, \nfirstCluster={}; \ncluster={}", firstCluster.toReadableString(), cluster.toReadableString());
            }
            Assert.assertEquals(firstCluster.getVersion(), cluster.getVersion());

            actualActiveNum = cluster.getMembers()
                    .stream()
                    .filter(m -> m.getStatus().isActive())
                    .count();
            if (activeNum != actualActiveNum) {
                LOGGER.error("cluster activeNum mismatch, \nfirstCluster={}; \ncluster={}", firstCluster.toReadableString(), cluster.toReadableString());
            }

            if (!Objects.equals(firstCluster.getAbstractInfo(), cluster.getAbstractInfo())) {
                LOGGER.error("cluster abstract mismatch, \nfirstCluster={}; \ncluster={}", firstCluster.toReadableString(), cluster.toReadableString());
            }
            Assert.assertEquals(firstCluster.getAbstractInfo(), cluster.getAbstractInfo());
        }
    }
}
