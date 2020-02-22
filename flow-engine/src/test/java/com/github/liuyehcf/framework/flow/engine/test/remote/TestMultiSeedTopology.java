package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.common.tools.collection.SplitUtils;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
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

                List<FlowEngine> flowEngines = Lists.newArrayList();
                int[] seedPorts = getSeedPorts(memberNum, seedNum);

                for (int j = 0; j < memberNum; j++) {
                    FlowProperties properties = new FlowProperties();
                    properties.setClusterConfig(createClusterConfig(seedPorts, 10000 + j));

                    flowEngines.add(createFlowEngine(properties));
                }

                TimeUnit.SECONDS.sleep(8);

                assertFlowEngines(flowEngines);

                flowEngines.parallelStream().forEach(FlowEngine::shutdownNow);
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

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(seedPorts, 10000 + i));

            flowEngines.add(createFlowEngine(properties));
        }

        TimeUnit.SECONDS.sleep(8);

        assertFlowEngines(flowEngines);

        int cycleTime = 10;
        int rebootNum = 2;

        for (int time = 0; time < cycleTime; time++) {
            System.err.println(String.format("\rrestart[%d/%d]", time, cycleTime));

            Collections.shuffle(flowEngines);

            List<List<FlowEngine>> segments = SplitUtils.split(flowEngines, rebootNum);
            int batch = 0;

            for (List<FlowEngine> segment : segments) {
                System.err.println(String.format("\rcycle[%d/%d], batch[%d/%d] is stopping, size=%d", time, cycleTime, batch, segments.size(), segment.size()));

                flowEngines.removeAll(segment);

                segment.parallelStream().forEach(FlowEngine::shutdownNow);

                TimeUnit.SECONDS.sleep(8);

                assertFlowEngines(flowEngines);

                System.err.println(String.format("\rcycle[%d/%d], batch[%d/%d] is starting, size=%d", time, cycleTime, batch, segments.size(), segment.size()));

                for (FlowEngine flowEngine : segment) {
                    flowEngines.add(reCreateFlowEngine(flowEngine));
                }

                TimeUnit.SECONDS.sleep(8);

                assertFlowEngines(flowEngines);

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

        List<List<FlowEngine>> islandFlowEngines = Lists.newArrayList();

        for (int i = 0; i < islandNum; i++) {
            List<FlowEngine> flowEngines = Lists.newArrayList();

            System.err.println(String.format("create island [%d]", i));

            for (int j = 0; j < memberOfEachIsland; j++) {
                FlowProperties properties = new FlowProperties();

                int[] seedPorts = islandsPorts[i];
                int memberPort = bridgeSeedPort + memberOfEachIsland * (i + 1) + j;

                System.err.println(String.format("\rcreate island member: seedPorts=%s, memberPort=%d", Arrays.toString(seedPorts), memberPort));

                properties.setClusterConfig(createClusterConfig(seedPorts, memberPort));

                flowEngines.add(createFlowEngine(properties));
            }

            System.err.println();

            islandFlowEngines.add(flowEngines);
        }

        TimeUnit.SECONDS.sleep(8);

        for (int i = 0; i < islandNum; i++) {
            assertFlowEngines(islandFlowEngines.get(i));
        }

        FlowProperties properties = new FlowProperties();
        properties.setClusterConfig(createClusterConfig(new int[]{bridgeSeedPort}, bridgeSeedPort));
        FlowEngine bridgeSeed = createFlowEngine(properties);

        List<FlowEngine> allMembers = Lists.newArrayList();
        for (int i = 0; i < islandNum; i++) {
            allMembers.addAll(islandFlowEngines.get(i));
        }
        allMembers.add(bridgeSeed);

        System.err.println("bridging isolated islands");
        TimeUnit.SECONDS.sleep(8);

        assertFlowEngines(allMembers);
    }
}
