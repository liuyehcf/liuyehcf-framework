package com.github.liuyehcf.framework.rule.engine.test.remote;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Topology;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
@SuppressWarnings("all")
public class TestSingleSeedTopology extends TestTopologyBase {

    @Test
    public void testContinuesBootWithNoInterval() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);
    }

    @Test
    public void testContinuesBootWithInterval() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(2);

        assertRuleEngines(ruleEngines);
    }

    @Test
    public void testSeedLastBoot() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = memberNum - 1; i >= 0; i--) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);
    }

    @Test
    public void testNormalShutdownContinuesly() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);

        Iterator<RuleEngine> iterator = ruleEngines.iterator();

        // escape seed node
        RuleEngine seed = iterator.next();

        int remainMemberNum = memberNum - 1;

        while (iterator.hasNext()) {
            RuleEngine next = iterator.next();
            iterator.remove();
            next.shutdownNow();

            TimeUnit.SECONDS.sleep(3);

            assertRuleEngines(ruleEngines);

            remainMemberNum--;
        }

        assertTopology(seed, 1);
        seed.shutdownNow();
    }

    @Test
    public void testNormalShutdownConcurrently() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);

        ruleEngines.subList(1, ruleEngines.size()).parallelStream().forEach(RuleEngine::shutdownNow);

        TimeUnit.SECONDS.sleep(2);

        RuleEngine seed = ruleEngines.get(0);

        assertTopology(seed, 1);
        seed.shutdownNow();
    }

    @Test
    public void testLeaderContinuesShutdown() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);

        while (!ruleEngines.isEmpty()) {
            Iterator<RuleEngine> iterator = ruleEngines.iterator();

            boolean hasLeader = false;

            while (iterator.hasNext()) {
                RuleEngine next = iterator.next();

                Topology topology = getTopology(next);

                if (!topology.isSelfLeader()) {
                    continue;
                }
                hasLeader = true;

                iterator.remove();
                next.shutdownNow();

                System.err.println(String.format("%s shutdown now", next.getProperties().getSelfConfig()));

                break;
            }

            Assert.assertTrue(hasLeader);

            TimeUnit.SECONDS.sleep(8);

            assertRuleEngines(ruleEngines);
        }
    }

    @Test
    public void testNormalRandomShutdownAndReboot() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);

        for (int shutdownNum = 1; shutdownNum < memberNum; shutdownNum++) {
            Collections.shuffle(ruleEngines);

            System.err.println(String.format("shutdown %d members", shutdownNum));

            List<RuleEngine> shutdownRuleEngines = Lists.newArrayList();
            List<RuleEngine> remainRuleEngines = Lists.newArrayList();

            int tmp = 0;
            for (RuleEngine ruleEngine : ruleEngines) {
                RuleProperties properties = ruleEngine.getProperties();
                if (properties.isSeed()) {
                    remainRuleEngines.add(ruleEngine);
                } else if (tmp < shutdownNum) {
                    tmp++;
                    shutdownRuleEngines.add(ruleEngine);
                } else {
                    remainRuleEngines.add(ruleEngine);
                }
            }

            Assert.assertEquals(shutdownNum, shutdownRuleEngines.size());
            Assert.assertEquals(memberNum - shutdownNum, remainRuleEngines.size());
            System.err.println(String.format("shutdownNum=%d, remainNum=%d",
                    shutdownRuleEngines.size(), remainRuleEngines.size()));

            shutdownRuleEngines.parallelStream().forEach(RuleEngine::shutdownNow);

            TimeUnit.SECONDS.sleep(8);

            ruleEngines = remainRuleEngines;

            assertRuleEngines(ruleEngines);

            for (RuleEngine shutdownRuleEngine : shutdownRuleEngines) {
                ruleEngines.add(reCreateRuleEngine(shutdownRuleEngine));
            }

            System.err.println(String.format("reboot %d members", shutdownNum));

            TimeUnit.SECONDS.sleep(8);

            assertRuleEngines(ruleEngines);
        }
    }

    @Test
    public void testSeedRandomShutdownAndReboot() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);

        for (int shutdownNum = 1; shutdownNum < memberNum; shutdownNum++) {
            Collections.shuffle(ruleEngines);

            System.err.println(String.format("shutdown %d members", shutdownNum));

            RuleEngine shutdownSeedEngine = null;
            List<RuleEngine> shutdownNormalEngines = Lists.newArrayList();
            List<RuleEngine> remainRuleEngines = Lists.newArrayList();

            int tmp = 0;
            for (RuleEngine ruleEngine : ruleEngines) {
                RuleProperties properties = ruleEngine.getProperties();
                if (properties.isSeed()) {
                    shutdownSeedEngine = ruleEngine;
                } else if (tmp < shutdownNum - 1) {
                    tmp++;
                    shutdownNormalEngines.add(ruleEngine);
                } else {
                    remainRuleEngines.add(ruleEngine);
                }
            }

            Assert.assertNotNull(shutdownSeedEngine);

            Assert.assertEquals(shutdownNum, shutdownNormalEngines.size() + 1);
            Assert.assertEquals(memberNum - shutdownNum, remainRuleEngines.size());
            System.err.println(String.format("shutdownNum=%d, remainNum=%d",
                    shutdownNormalEngines.size() + 1, remainRuleEngines.size()));

            shutdownSeedEngine.shutdownNow();
            shutdownNormalEngines.parallelStream().forEach(RuleEngine::shutdownNow);

            TimeUnit.SECONDS.sleep(8);

            ruleEngines = remainRuleEngines;

            assertRuleEngines(ruleEngines);

            System.err.println("reboot seed");
            ruleEngines.add(reCreateRuleEngine(shutdownSeedEngine));

            TimeUnit.SECONDS.sleep(8);

            assertRuleEngines(ruleEngines);

            System.err.println("reboot normal members");
            for (RuleEngine shutdownRuleEngine : shutdownNormalEngines) {
                ruleEngines.add(reCreateRuleEngine(shutdownRuleEngine));
            }

            System.err.println(String.format("reboot %d members", shutdownNum));

            TimeUnit.SECONDS.sleep(8);

            assertRuleEngines(ruleEngines);
        }
    }

    @Test
    public void testSeedReboot() throws Exception {
        int memberNum = 10;

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));
        }

        TimeUnit.SECONDS.sleep(3);

        assertRuleEngines(ruleEngines);

        Iterator<RuleEngine> iterator = ruleEngines.iterator();

        RuleEngine seed = iterator.next();
        iterator.remove();
        seed.shutdownNow();

        System.err.println(String.format("%s shutdown now", seed.getProperties().getSelfConfig()));

        TimeUnit.SECONDS.sleep(8);

        assertRuleEngines(ruleEngines);

        RuleProperties properties = new RuleProperties();
        properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000));

        ruleEngines.add(createRuleEngine(properties));

        TimeUnit.SECONDS.sleep(8);

        assertRuleEngines(ruleEngines);
    }
}
