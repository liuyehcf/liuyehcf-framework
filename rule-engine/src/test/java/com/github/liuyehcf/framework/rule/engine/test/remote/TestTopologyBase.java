package com.github.liuyehcf.framework.rule.engine.test.remote;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultRuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Member;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.MemberConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Topology;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.ClusterEventLoop;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.SerializeType;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenfeng.hcf
 * @date 2019/9/13
 */
public class TestTopologyBase extends TestRemoteBase {

    private static final String HOST = "127.0.0.1";

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(1);

    private static final List<RuleEngine> ruleEnginesToBeDeleted = Lists.newArrayList();

    @After
    public void after() throws Exception {
        ruleEnginesToBeDeleted.parallelStream().forEach(RuleEngine::shutdownNow);
        ruleEnginesToBeDeleted.clear();
        TimeUnit.SECONDS.sleep(1);
    }

    ClusterConfig createClusterConfig(int[] seedPorts, int port) {
        ClusterConfig clusterConfig = new ClusterConfig();

        List<MemberConfig> seeds = Lists.newArrayList();

        for (int seedPort : seedPorts) {
            MemberConfig seed = new MemberConfig();
            seed.setHost(HOST);
            seed.setPort(seedPort);
            seeds.add(seed);
        }

        MemberConfig self = new MemberConfig();
        self.setHost(HOST);
        self.setPort(port);

        clusterConfig.setSeeds(seeds);
        clusterConfig.setSelf(self);
        clusterConfig.setSerializeType(SerializeType.hessian.name());
        clusterConfig.setHeartbeatInterval(1);
        clusterConfig.setHeartbeatRetryInterval(1);
        clusterConfig.setTopologyProbeInterval(1);

        return clusterConfig;
    }

    RuleEngine createRuleEngine(RuleProperties properties) {
        RuleEngine ruleEngine = new DefaultRuleEngine(properties, EXECUTOR, SCHEDULED_EXECUTOR);
        ruleEnginesToBeDeleted.add(ruleEngine);
        return ruleEngine;
    }

    RuleEngine reCreateRuleEngine(RuleEngine ruleEngine) {
        return createRuleEngine(CloneUtils.hessianClone(ruleEngine.getProperties()));
    }

    Topology getTopology(RuleEngine ruleEngine) {
        try {
            Field eventLoopField = ruleEngine.getClass().getDeclaredField("clusterEventLoop");

            eventLoopField.setAccessible(true);

            ClusterEventLoop clusterEventLoop = (ClusterEventLoop) eventLoopField.get(ruleEngine);
            return clusterEventLoop.getTopology();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    int[] getSeedPorts(int memberNum) {
        int[] seedPorts = new int[memberNum];
        for (int i = 0; i < memberNum; i++) {
            seedPorts[i] = 10000 + i;
        }

        return seedPorts;
    }

    int[] getSeedPorts(int memberNum, int seedNum) {
        boolean[] seedMarks = new boolean[memberNum];
        for (int i = 0; i < seedNum; i++) {
            seedMarks[i] = true;
        }

        int[] seedPorts = new int[seedNum];

        ArrayUtils.shuffle(seedMarks);
        int seedCnt = 0;
        for (int i = 0; i < memberNum; i++) {
            if (seedMarks[i]) {
                seedPorts[seedCnt++] = 10000 + i;
            }
        }

        return seedPorts;
    }

    void assertTopology(RuleEngine ruleEngine, int activeNum) {
        Topology topology = getTopology(ruleEngine);
        Assert.assertNotNull(topology.getLeader());
        Assert.assertTrue(topology.getLeader().getStatus().isActive());
        Assert.assertFalse(topology.getLeader().getLocalStatus().isUnreachable());
        Assert.assertEquals(activeNum, topology.activeNum());
    }

    void assertRuleEngines(List<RuleEngine> ruleEngines) {
        Member leader = null;
        String clusterIdentifier = null;
        for (RuleEngine ruleEngine : ruleEngines) {
            assertTopology(ruleEngine, ruleEngines.size());

            Topology topology = getTopology(ruleEngine);

            if (leader == null) {
                leader = topology.getLeader();
                clusterIdentifier = topology.getIdentifier();
            } else {
                Assert.assertEquals(leader.getIdentifier(), topology.getLeader().getIdentifier());
                Assert.assertEquals(clusterIdentifier, topology.getIdentifier());
            }
        }
    }
}
