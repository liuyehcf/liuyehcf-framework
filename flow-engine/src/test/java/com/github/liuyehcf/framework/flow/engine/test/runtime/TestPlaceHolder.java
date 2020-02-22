package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class TestPlaceHolder extends TestRuntimeBase {

    @Test
    public void testGrammarLevel() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .put("b", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testStringLevel() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .put("b", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=\"${b}\")\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testNull() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=\"${b}\")\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testError() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=\"${b.c}\")\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("[ 063, PLACE_HOLDER ] - Null property value for 'b.c' on bean class 'class java.util.HashMap'", promise.cause().getMessage());
    }

    @Test
    public void testInt1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100)
                .put("b", 100)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testInt2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100)
                .put("b.c.d", 100)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testInt3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100)
                .put("b", 99)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testInt4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100)
                .put("b.c.d", 99)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testLong1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100L)
                .put("b", 100L)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testLong2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100L)
                .put("b.c.d", 100L)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testLong3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100L)
                .put("b", 99L)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testLong4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100L)
                .put("b.c.d", 99L)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testFloat1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100.f)
                .put("b", 100.f)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testFloat2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100.f)
                .put("b.c.d", 100.f)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testFloat3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100.f)
                .put("b", 99.f)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testFloat4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100.f)
                .put("b.c.d", 99.f)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testDouble1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100.2d)
                .put("b", 100.2d)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testDouble2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100.2d)
                .put("b.c.d", 100.2d)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testDouble3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100.2d)
                .put("b", 99.2d)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testDouble4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", 100.2d)
                .put("b.c.d", 99.2d)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testString1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 100)
                .put("b", 100)
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testString2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", "test")
                .put("b.c.d", "test")
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testString3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", "test")
                .put("b", "test1")
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a\",expectedValue=${b})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }

    @Test
    public void testString4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a.b.c", "test")
                .put("b.c.d", "test1")
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    getPropertyAction(name=\"a.b.c\",expectedValue=${b.c.d})\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("property value not equals", promise.cause().getMessage());
    }
}
