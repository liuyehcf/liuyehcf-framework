package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
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
    private RuleEngine engine;

    @Test
    public void test1() {
        Promise<ExecutionInstance> promise = engine.startRule("{\n" +
                "    if(componentCondition(output=true)){\n" +
                "        componentAction()[componentListener(event=\"before\")]\n" +
                "    }\n" +
                "}", null);

        System.out.println(JSON.toJSONString(promise.get()));
    }

    @Test
    public void test2() {
        Promise<ExecutionInstance> promise = engine.startRule("{\n" +
                "    if(multi.name.condition(output=true)){\n" +
                "        multi/name/action()[multi.name.listener(event=\"before\"), multi/name/listener(event=\"success\")]\n" +
                "    }\n" +
                "}", null);

        System.out.println(JSON.toJSONString(promise.get()));
    }

}
