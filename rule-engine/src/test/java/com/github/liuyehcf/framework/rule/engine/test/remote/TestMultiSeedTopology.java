package com.github.liuyehcf.framework.rule.engine.test.remote;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.util.SplitUtils;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/9/14
 */
public class TestMultiSeedTopology extends TestTopologyBase {

    @Test
    public void testContinuesBootWithNoInterval() throws Exception {
        int maxMemberNum = 10;

        for (int memberNum = 1; memberNum <= maxMemberNum; memberNum++) {
            for (int seedNum = 1; seedNum <= memberNum; seedNum++) {
                System.err.println(String.format("memberNum=%d, seedNum=%d", memberNum, seedNum));

                List<RuleEngine> ruleEngines = Lists.newArrayList();
                int[] seedPorts = getSeedPorts(memberNum, seedNum);

                for (int j = 0; j < memberNum; j++) {
                    RuleProperties properties = new RuleProperties();
                    properties.setClusterConfig(createClusterConfig(seedPorts, 10000 + j));

                    ruleEngines.add(createRuleEngine(properties));
                }

                TimeUnit.SECONDS.sleep(8);

                assertRuleEngines(ruleEngines);

                ruleEngines.parallelStream().forEach(RuleEngine::shutdownNow);
            }
        }
    }

    @Test
    public void testTwoIsolatedIslandsMerge1() throws Exception {
        testIslandsMerge(2, 1);
    }

    @Test
    public void testTwoIsolatedIslandsMerge2() throws Exception {
        testIslandsMerge(2, 10);
    }

    @Test
    public void testThreeIsolatedIslandsMerges() throws Exception {
        testIslandsMerge(3, 5);
    }

    @Test
    public void testShutdownAndReboot() throws Exception {
        int memberNum = 15;
        int[] seedPorts = new int[]{10000, 10005, 10010};

        List<RuleEngine> ruleEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            RuleProperties properties = new RuleProperties();
            properties.setClusterConfig(createClusterConfig(seedPorts, 10000 + i));

            ruleEngines.add(createRuleEngine(properties));
        }

        TimeUnit.SECONDS.sleep(8);

        assertRuleEngines(ruleEngines);

        int cycleTime = 10;
        int rebootNum = 2;

        for (int time = 0; time < cycleTime; time++) {
            System.err.println(String.format("\rrestart[%d/%d]", time, cycleTime));

            Collections.shuffle(ruleEngines);

            List<List<RuleEngine>> segments = SplitUtils.split(ruleEngines, rebootNum);
            int batch = 0;

            for (List<RuleEngine> segment : segments) {
                System.err.println(String.format("\rcycle[%d/%d], batch[%d/%d] is stopping, size=%d", time, cycleTime, batch, segments.size(), segment.size()));

                ruleEngines.removeAll(segment);

                segment.parallelStream().forEach(RuleEngine::shutdownNow);

                TimeUnit.SECONDS.sleep(8);

                assertRuleEngines(ruleEngines);

                System.err.println(String.format("\rcycle[%d/%d], batch[%d/%d] is starting, size=%d", time, cycleTime, batch, segments.size(), segment.size()));

                for (RuleEngine ruleEngine : segment) {
                    ruleEngines.add(reCreateRuleEngine(ruleEngine));
                }

                TimeUnit.SECONDS.sleep(8);

                assertRuleEngines(ruleEngines);

                batch++;
            }
        }
    }

    private void testIslandsMerge(int islandNum, int memberOfEachIsland) throws Exception {
        int bridgeSeedPort = 10000;

        int[][] islandsPorts = new int[islandNum][2];

        for (int i = 0; i < islandNum; i++) {
            islandsPorts[i] = new int[]{bridgeSeedPort, bridgeSeedPort + memberOfEachIsland * (i + 1)};
            System.err.println(String.format("seed port of island[%d]: %s", i, Arrays.toString(islandsPorts[i])));
        }

        List<List<RuleEngine>> islandRuleEngines = Lists.newArrayList();

        for (int i = 0; i < islandNum; i++) {
            List<RuleEngine> ruleEngines = Lists.newArrayList();

            System.err.println(String.format("create island [%d]", i));

            for (int j = 0; j < memberOfEachIsland; j++) {
                RuleProperties properties = new RuleProperties();

                int[] seedPorts = islandsPorts[i];
                int memberPort = bridgeSeedPort + memberOfEachIsland * (i + 1) + j;

                System.err.println(String.format("\rcreate island member: seedPorts=%s, memberPort=%d", Arrays.toString(seedPorts), memberPort));

                properties.setClusterConfig(createClusterConfig(seedPorts, memberPort));

                ruleEngines.add(createRuleEngine(properties));
            }

            System.err.println();

            islandRuleEngines.add(ruleEngines);
        }

        TimeUnit.SECONDS.sleep(8);

        for (int i = 0; i < islandNum; i++) {
            assertRuleEngines(islandRuleEngines.get(i));
        }

        RuleProperties properties = new RuleProperties();
        properties.setClusterConfig(createClusterConfig(new int[]{bridgeSeedPort}, bridgeSeedPort));
        RuleEngine bridgeSeed = createRuleEngine(properties);

        List<RuleEngine> allMembers = Lists.newArrayList();
        for (int i = 0; i < islandNum; i++) {
            allMembers.addAll(islandRuleEngines.get(i));
        }
        allMembers.add(bridgeSeed);

        System.err.println("bridging isolated islands");
        TimeUnit.SECONDS.sleep(8);

        assertRuleEngines(allMembers);
    }
}
