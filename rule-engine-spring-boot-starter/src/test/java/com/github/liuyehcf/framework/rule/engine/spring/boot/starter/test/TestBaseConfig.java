package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestApplication.class})
public class TestBaseConfig {

    @Resource
    private RuleEngine defaultRuleEngine;

    @Resource
    private RuleEngine specialRuleEngine;

    @Test
    public void testComponentAnnotation() {
        Promise<ExecutionInstance> promise = defaultRuleEngine.startRule("{\n" +
                "    if(componentCondition(output=true)){\n" +
                "        componentAction()[componentListener(event=\"before\")]\n" +
                "    }\n" +
                "}", null);

        System.out.println(JSON.toJSONString(promise.get()));
    }

    @Test
    public void testDelegateAnnotation() {
        Promise<ExecutionInstance> promise = defaultRuleEngine.startRule("{\n" +
                "    if(multi.name.condition(output=true)){\n" +
                "        multi/name/action()[multi.name.listener(event=\"before\"), multi/name/listener(event=\"success\")]\n" +
                "    }\n" +
                "}", null);

        System.out.println(JSON.toJSONString(promise.get()));
    }

    @Test
    public void testMisMatchAction() {
        try {
            Promise<ExecutionInstance> promise = defaultRuleEngine.startRule("{\n" +
                    "    specialAction()\n" +
                    "}", null);

            promise.get();
        } catch (RuleException e) {
            Assert.assertEquals("[ 011, PROMISE ] - promise failed - unregistered action 'specialAction'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void testMisMatchCondition() {
        try {
            Promise<ExecutionInstance> promise = defaultRuleEngine.startRule("{\n" +
                    "    if(specialCondition()){\n" +
                    "        specialAction()\n" +
                    "    }\n" +
                    "}", null);

            promise.get();
        } catch (RuleException e) {
            Assert.assertEquals("[ 011, PROMISE ] - promise failed - unregistered condition 'specialCondition'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void testMisMatchListener() {
        try {
            Promise<ExecutionInstance> promise = defaultRuleEngine.startRule("{\n" +
                    "    if(specialCondition()[specialListener(event=\"before\")]){\n" +
                    "        specialAction()\n" +
                    "    }\n" +
                    "}", null);

            promise.get();
        } catch (RuleException e) {
            Assert.assertEquals("[ 011, PROMISE ] - promise failed - unregistered listener 'specialListener'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void testSpecialRule() {
        Promise<ExecutionInstance> promise = specialRuleEngine.startRule("{\n" +
                "    if(specialCondition()[specialListener(event=\"before\")]){\n" +
                "        specialAction()\n" +
                "    }\n" +
                "}", null);

        System.out.println(promise.get());
    }
}
