package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.Topology;
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

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);
    }

    @Test
    public void testContinuesBootWithInterval() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(2);

        assertFlowEngines(flowEngines);
    }

    @Test
    public void testSeedLastBoot() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = memberNum - 1; i >= 0; i--) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);
    }

    @Test
    public void testNormalShutdownContinuesly() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);

        Iterator<FlowEngine> iterator = flowEngines.iterator();

        // escape seed node
        FlowEngine seed = iterator.next();

        int remainMemberNum = memberNum - 1;

        while (iterator.hasNext()) {
            FlowEngine next = iterator.next();
            iterator.remove();
            next.shutdownNow();

            TimeUnit.SECONDS.sleep(3);

            assertFlowEngines(flowEngines);

            remainMemberNum--;
        }

        assertTopology(seed, 1);
        seed.shutdownNow();
    }

    @Test
    public void testNormalShutdownConcurrently() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);

        flowEngines.subList(1, flowEngines.size()).parallelStream().forEach(FlowEngine::shutdownNow);

        TimeUnit.SECONDS.sleep(2);

        FlowEngine seed = flowEngines.get(0);

        assertTopology(seed, 1);
        seed.shutdownNow();
    }

    @Test
    public void testLeaderContinuesShutdown() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);

        while (!flowEngines.isEmpty()) {
            Iterator<FlowEngine> iterator = flowEngines.iterator();

            boolean hasLeader = false;

            while (iterator.hasNext()) {
                FlowEngine next = iterator.next();

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

            assertFlowEngines(flowEngines);
        }
    }

    @Test
    public void testNormalRandomShutdownAndReboot() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);

        for (int shutdownNum = 1; shutdownNum < memberNum; shutdownNum++) {
            Collections.shuffle(flowEngines);

            System.err.println(String.format("shutdown %d members", shutdownNum));

            List<FlowEngine> shutdownFlowEngines = Lists.newArrayList();
            List<FlowEngine> remainFlowEngines = Lists.newArrayList();

            int tmp = 0;
            for (FlowEngine flowEngine : flowEngines) {
                FlowProperties properties = flowEngine.getProperties();
                if (properties.isSeed()) {
                    remainFlowEngines.add(flowEngine);
                } else if (tmp < shutdownNum) {
                    tmp++;
                    shutdownFlowEngines.add(flowEngine);
                } else {
                    remainFlowEngines.add(flowEngine);
                }
            }

            Assert.assertEquals(shutdownNum, shutdownFlowEngines.size());
            Assert.assertEquals(memberNum - shutdownNum, remainFlowEngines.size());
            System.err.println(String.format("shutdownNum=%d, remainNum=%d",
                    shutdownFlowEngines.size(), remainFlowEngines.size()));

            shutdownFlowEngines.parallelStream().forEach(FlowEngine::shutdownNow);

            TimeUnit.SECONDS.sleep(8);

            flowEngines = remainFlowEngines;

            assertFlowEngines(flowEngines);

            for (FlowEngine shutdownFlowEngine : shutdownFlowEngines) {
                flowEngines.add(reCreateFlowEngine(shutdownFlowEngine));
            }

            System.err.println(String.format("reboot %d members", shutdownNum));

            TimeUnit.SECONDS.sleep(8);

            assertFlowEngines(flowEngines);
        }
    }

    @Test
    public void testSeedRandomShutdownAndReboot() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));

            TimeUnit.MILLISECONDS.sleep(100);
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);

        for (int shutdownNum = 1; shutdownNum < memberNum; shutdownNum++) {
            Collections.shuffle(flowEngines);

            System.err.println(String.format("shutdown %d members", shutdownNum));

            FlowEngine shutdownSeedEngine = null;
            List<FlowEngine> shutdownNormalEngines = Lists.newArrayList();
            List<FlowEngine> remainFlowEngines = Lists.newArrayList();

            int tmp = 0;
            for (FlowEngine flowEngine : flowEngines) {
                FlowProperties properties = flowEngine.getProperties();
                if (properties.isSeed()) {
                    shutdownSeedEngine = flowEngine;
                } else if (tmp < shutdownNum - 1) {
                    tmp++;
                    shutdownNormalEngines.add(flowEngine);
                } else {
                    remainFlowEngines.add(flowEngine);
                }
            }

            Assert.assertNotNull(shutdownSeedEngine);

            Assert.assertEquals(shutdownNum, shutdownNormalEngines.size() + 1);
            Assert.assertEquals(memberNum - shutdownNum, remainFlowEngines.size());
            System.err.println(String.format("shutdownNum=%d, remainNum=%d",
                    shutdownNormalEngines.size() + 1, remainFlowEngines.size()));

            shutdownSeedEngine.shutdownNow();
            shutdownNormalEngines.parallelStream().forEach(FlowEngine::shutdownNow);

            TimeUnit.SECONDS.sleep(8);

            flowEngines = remainFlowEngines;

            assertFlowEngines(flowEngines);

            System.err.println("reboot seed");
            flowEngines.add(reCreateFlowEngine(shutdownSeedEngine));

            TimeUnit.SECONDS.sleep(8);

            assertFlowEngines(flowEngines);

            System.err.println("reboot normal members");
            for (FlowEngine shutdownFlowEngine : shutdownNormalEngines) {
                flowEngines.add(reCreateFlowEngine(shutdownFlowEngine));
            }

            System.err.println(String.format("reboot %d members", shutdownNum));

            TimeUnit.SECONDS.sleep(8);

            assertFlowEngines(flowEngines);
        }
    }

    @Test
    public void testSeedReboot() throws Exception {
        int memberNum = 10;

        List<FlowEngine> flowEngines = Lists.newArrayList();

        for (int i = 0; i < memberNum; i++) {
            FlowProperties properties = new FlowProperties();
            properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000 + i));

            flowEngines.add(createFlowEngine(properties));
        }

        TimeUnit.SECONDS.sleep(3);

        assertFlowEngines(flowEngines);

        Iterator<FlowEngine> iterator = flowEngines.iterator();

        FlowEngine seed = iterator.next();
        iterator.remove();
        seed.shutdownNow();

        System.err.println(String.format("%s shutdown now", seed.getProperties().getSelfConfig()));

        TimeUnit.SECONDS.sleep(8);

        assertFlowEngines(flowEngines);

        FlowProperties properties = new FlowProperties();
        properties.setClusterConfig(createClusterConfig(new int[]{10000}, 10000));

        flowEngines.add(createFlowEngine(properties));

        TimeUnit.SECONDS.sleep(8);

        assertFlowEngines(flowEngines);
    }
}
