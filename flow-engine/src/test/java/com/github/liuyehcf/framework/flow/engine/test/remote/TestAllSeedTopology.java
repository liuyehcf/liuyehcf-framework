package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/9/13
 */
@SuppressWarnings("all")
public class TestAllSeedTopology extends TestTopologyBase {

    @Test
    public void testContinuesBootWithNoInterval() throws Exception {
        int maxMemberNum = 15;

        for (int memberNum = 1; memberNum <= maxMemberNum; memberNum++) {
            System.err.println(String.format("memberNum=%d", memberNum));

            List<FlowEngine> flowEngines = Lists.newArrayList();
            int[] seedPorts = getSeedPorts(memberNum);

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
