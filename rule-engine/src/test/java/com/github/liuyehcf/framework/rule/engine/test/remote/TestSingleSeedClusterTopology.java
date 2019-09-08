package com.github.liuyehcf.framework.rule.engine.test.remote;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultRuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNodeConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterTopology;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.ClusterEventLoop;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.SerializeType;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
@SuppressWarnings("all")
public class TestSingleSeedClusterTopology extends TestRemoteBase {

    private static final String HOST = "127.0.0.1";

    @Test
    public void testContinuesBootWithNoInterval() throws Exception {
        int nodeNum = 10;
        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < nodeNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(new DefaultRuleEngine(properties));
        }

        TimeUnit.SECONDS.sleep(2);

        for (RuleEngine ruleEngine : ruleEngines) {
            ClusterTopology topology = getTopology(ruleEngine);
            Assert.assertEquals(nodeNum, topology.activeNum());
        }

        for (RuleEngine ruleEngine : ruleEngines) {
            ruleEngine.shutdownNow();
        }
    }

    @Test
    public void testContinuesBootWithInterval() throws Exception {
        int nodeNum = 10;
        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < nodeNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(new DefaultRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(2);

        for (RuleEngine ruleEngine : ruleEngines) {
            ClusterTopology topology = getTopology(ruleEngine);
            Assert.assertEquals(nodeNum, topology.activeNum());
        }

        for (RuleEngine ruleEngine : ruleEngines) {
            ruleEngine.shutdownNow();
        }
    }

    @Test
    public void testSeedLastBoot() throws Exception {
        int nodeNum = 10;
        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = nodeNum - 1; i >= 0; i--) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(new DefaultRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(2);

        for (RuleEngine ruleEngine : ruleEngines) {
            ClusterTopology topology = getTopology(ruleEngine);
            Assert.assertEquals(nodeNum, topology.activeNum());
        }

        for (RuleEngine ruleEngine : ruleEngines) {
            ruleEngine.shutdownNow();
        }
    }

    @Test
    public void testNormalShutdownContinuesly() throws Exception {
        int nodeNum = 10;
        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < nodeNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(new DefaultRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(1);

        for (RuleEngine ruleEngine : ruleEngines) {
            ClusterTopology topology = getTopology(ruleEngine);
            Assert.assertEquals(nodeNum, topology.activeNum());
        }

        Iterator<RuleEngine> iterator = ruleEngines.iterator();

        // escape seed node
        RuleEngine seed = iterator.next();

        int remainNodeNum = nodeNum - 1;

        while (iterator.hasNext()) {
            RuleEngine next = iterator.next();
            iterator.remove();
            next.shutdownNow();

            TimeUnit.MILLISECONDS.sleep(1500);

            for (RuleEngine ruleEngine : ruleEngines) {
                ClusterTopology topology = getTopology(ruleEngine);
                Assert.assertEquals(remainNodeNum, topology.activeNum());
            }

            remainNodeNum--;
        }

        ClusterTopology topology = getTopology(seed);
        Assert.assertEquals(1, topology.activeNum());
        seed.shutdownNow();
    }

    @Test
    public void testNormalShutdownConcurrently() throws Exception {
        int nodeNum = 10;
        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < nodeNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(new DefaultRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(1);

        for (RuleEngine ruleEngine : ruleEngines) {
            ClusterTopology topology = getTopology(ruleEngine);
            Assert.assertEquals(nodeNum, topology.activeNum());
        }

        ruleEngines.subList(1, ruleEngines.size()).parallelStream().forEach(RuleEngine::shutdownNow);

        TimeUnit.SECONDS.sleep(2);

        RuleEngine seed = ruleEngines.get(0);

        ClusterTopology topology = getTopology(seed);
        Assert.assertEquals(1, topology.activeNum());
        seed.shutdownNow();
    }

    private ClusterConfig createClusterConfig(int[] seedPorts, int port) {
        ClusterConfig clusterConfig = new ClusterConfig();

        List<ClusterNodeConfig> seeds = Lists.newArrayList();

        for (int seedPort : seedPorts) {
            ClusterNodeConfig seed = new ClusterNodeConfig();
            seed.setHost(HOST);
            seed.setPort(seedPort);
            seeds.add(seed);
        }

        ClusterNodeConfig self = new ClusterNodeConfig();
        self.setHost(HOST);
        self.setPort(port);

        clusterConfig.setSeeds(seeds);
        clusterConfig.setSelf(self);
        clusterConfig.setSerializeType(SerializeType.hessian.name());
        clusterConfig.setHeartbeatInterval(1);
        clusterConfig.setHeartbeatRetryInterval(1);
        clusterConfig.setTopologyKeepAliveInterval(1);

        return clusterConfig;
    }

    private ClusterTopology getTopology(RuleEngine ruleEngine) {
        try {
            Field eventLoopField = ruleEngine.getClass().getDeclaredField("clusterEventLoop");

            eventLoopField.setAccessible(true);

            ClusterEventLoop clusterEventLoop = (ClusterEventLoop) eventLoopField.get(ruleEngine);

            Field topologyField = clusterEventLoop.getClass().getDeclaredField("topology");

            topologyField.setAccessible(true);

            return (ClusterTopology) topologyField.get(clusterEventLoop);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
