package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class TestSetProperty extends TestRuntimeBase {

    @Test
    public void testAction1() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    setPropertyAction(name=\"a\",value=\"test\"){\n" +
                        "        getPropertyAction(name=\"a\",expectedValue=${a})\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testAction2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b", Maps.newHashMap())
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    setPropertyAction(name=\"a.b.c\",value=\"test\"){\n" +
                        "        getPropertyAction(name=\"a.b.c\",expectedValue=${a.b.c})\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition1() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(setPropertyCondition(name=\"a\",value=\"test\",output=true)){\n" +
                        "        getPropertyAction(name=\"a\",expectedValue=${a})\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b", Maps.newHashMap())
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(setPropertyCondition(name=\"a.b.c\",value=\"test\",output=true)){\n" +
                        "        getPropertyAction(name=\"a.b.c\",expectedValue=${a.b.c})\n" +
                        "    }else{\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b", Maps.newHashMap())
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(setPropertyCondition(name=\"a.b.c\",value=\"test\",output=false)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }else{\n" +
                        "        getPropertyAction(name=\"a.b.c\",expectedValue=${a.b.c})\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener1() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=\"${a}\")[setPropertyListener(event=\"start\",name=\"a\",value=\"b\")]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener2() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=\"b\")[setPropertyListener(event=\"end\",name=\"a\",value=\"b\")]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testListener3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b", Maps.newHashMap())
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=\"${a.b.c}\")[setPropertyListener(event=\"start\",name=\"a.b.c\",value=\"test\")]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener4() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=\"${a.b.c}\")[setPropertyListener(event=\"end\",name=\"a.b.c\",alue=\"test\")]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("[ 063, PLACE_HOLDER ] - Null property value for 'a.b.c' on bean class 'class java.util.HashMap'", promise.cause().getMessage());
    }
}
