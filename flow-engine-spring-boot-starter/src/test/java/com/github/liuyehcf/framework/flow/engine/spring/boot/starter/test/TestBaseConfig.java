package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
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
    private FlowEngine defaultFlowEngine;

    @Resource
    private FlowEngine specialFlowEngine;

    @Test
    public void testComponentAnnotation() {
        Promise<ExecutionInstance> promise = defaultFlowEngine.startFlow(new ExecutionCondition("{\n" +
                "    if(componentCondition(output=true)){\n" +
                "        componentAction()[componentListener(event=\"before\")]\n" +
                "    }\n" +
                "}"));

        System.out.println(JSON.toJSONString(promise.get()));
    }

    @Test
    public void testDelegateAnnotation() {
        Promise<ExecutionInstance> promise = defaultFlowEngine.startFlow(new ExecutionCondition("{\n" +
                "    if(multi.name.condition(output=true)){\n" +
                "        multi/name/action()[multi.name.listener(event=\"before\"), multi/name/listener(event=\"success\")]\n" +
                "    }\n" +
                "}"));

        System.out.println(JSON.toJSONString(promise.get()));
    }

    @Test
    public void testMisMatchAction() {
        try {
            Promise<ExecutionInstance> promise = defaultFlowEngine.startFlow(new ExecutionCondition("{\n" +
                    "    specialAction()\n" +
                    "}"));

            promise.get();
        } catch (FlowException e) {
            Assert.assertEquals("[ 011, PROMISE ] - promise failed - unregistered action 'specialAction'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void testMisMatchCondition() {
        try {
            Promise<ExecutionInstance> promise = defaultFlowEngine.startFlow(new ExecutionCondition("{\n" +
                    "    if(specialCondition()){\n" +
                    "        specialAction()\n" +
                    "    }\n" +
                    "}"));

            promise.get();
        } catch (FlowException e) {
            Assert.assertEquals("[ 011, PROMISE ] - promise failed - unregistered condition 'specialCondition'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void testMisMatchListener() {
        try {
            Promise<ExecutionInstance> promise = defaultFlowEngine.startFlow(new ExecutionCondition("{\n" +
                    "    if(specialCondition()[specialListener(event=\"before\")]){\n" +
                    "        specialAction()\n" +
                    "    }\n" +
                    "}"));

            promise.get();
        } catch (FlowException e) {
            Assert.assertEquals("[ 011, PROMISE ] - promise failed - unregistered listener 'specialListener'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void testSpecialFlow() {
        Promise<ExecutionInstance> promise = specialFlowEngine.startFlow(new ExecutionCondition("{\n" +
                "    if(specialCondition()[specialListener(event=\"before\")]){\n" +
                "        specialAction()\n" +
                "    }\n" +
                "}"));

        System.out.println(promise.get());
    }
}
