package com.github.liuyehcf.framework.rule.engine.test.remote;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chenfeng.hcf
 * @date 2019/9/13
 */
@SuppressWarnings("all")
public class TestAllSeedTopology extends TestTopologyBase {

    @Test
    public void testContinuesBootWithNoInterval() throws Exception {
        int maxMemberNum = 15;

        for (int memberNum = 1; memberNum <= maxMemberNum; memberNum++) {
            System.err.println(String.format("memberNum=%d", memberNum));

            List<RuleEngine> ruleEngines = Lists.newArrayList();
            int[] seedPorts = getSeedPorts(memberNum);

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
