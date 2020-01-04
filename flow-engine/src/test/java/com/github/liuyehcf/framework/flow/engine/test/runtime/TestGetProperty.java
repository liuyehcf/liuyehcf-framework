package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class TestGetProperty extends TestRuntimeBase {

    @Test
    public void testAction1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=1)\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testAction2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=2)\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testAction3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=1)\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testAction4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=2)\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testAction5() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b\",expectedValue=2)\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("[ 062, PROPERTY ] - propertyName='a.b' - Null property value for 'a.b' on bean class 'class java.util.HashMap'", promise.cause().getMessage());
    }

    @Test
    public void testAction6() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", Maps.newHashMap())
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b\",expectedValue=2)[setPropertyListener(event=\"before\", name=\"a.b\", value=2)]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", true)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(getPropertyCondition(name=\"a\",expectedValue=true,output=true)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", true)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(getPropertyCondition(name=\"a\",expectedValue=false,output=true)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testCondition3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", true)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(getPropertyCondition(name=\"a.b.c\",expectedValue=true,output=true)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", true)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(getPropertyCondition(name=\"a.b.c\",expectedValue=false,output=true)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testCondition5() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(getPropertyCondition(name=\"a.b.c\",expectedValue=false,output=true)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("[ 062, PROPERTY ] - propertyName='a.b.c' - Null property value for 'a.b.c' on bean class 'class java.util.HashMap'", promise.cause().getMessage());
    }

    @Test
    public void testListener1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 15)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{    \n" +
                        "    printAction(content=\"test\")[getPropertyListener(event=\"before\",name=\"a\",expectedValue=0xf)]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 16)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{    \n" +
                        "    printAction(content=\"test\")[getPropertyListener(event=\"before\",name=\"a\",expectedValue=0xf)]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testListener3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 7)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{    \n" +
                        "    printAction(content=\"test\")[getPropertyListener(event=\"before\",name=\"a.b.c\",expectedValue=07)]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 8)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{    \n" +
                        "    printAction(content=\"test\")[getPropertyListener(event=\"before\",name=\"a.b.c\",expectedValue=07)]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testListener5() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{    \n" +
                        "    printAction(content=\"test\")[getPropertyListener(event=\"before\",name=\"a.b.c\",expectedValue=07)]\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("[ 062, PROPERTY ] - propertyName='a.b.c' - Null property value for 'a.b.c' on bean class 'class java.util.HashMap'", promise.cause().getMessage());
    }
}
