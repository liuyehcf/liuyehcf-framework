package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.test.runtime.listener.ScopeListener;
import org.junit.Test;

import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2019/8/30
 */
public class TestListenerScope extends TestRuntimeBase {

    @Test
    public void testActionBefore() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    printAction(content=\"actionA\")[scopeListener(event=\"before\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_BEFORE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testActionSuccess() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    printAction(content=\"actionA\")[scopeListener(event=\"success\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_SUCCESS_TRIGGERED.contains(namespace));
    }

    @Test
    public void testActionFailure() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    throwLinkTerminateAction()[scopeListener(event=\"failure\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_FAILURE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testConditionBefore() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    if(printCondition(content=\"condition\", output=true)[scopeListener(event=\"before\", namespace=${namespace}, scope=\"node\")])\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_BEFORE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testConditionSuccess() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    if(printCondition(content=\"condition\", output=true)[scopeListener(event=\"success\", namespace=${namespace}, scope=\"node\")])\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_SUCCESS_TRIGGERED.contains(namespace));
    }

    @Test
    public void testConditionFailure() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    if(throwLinkTerminateCondition()[scopeListener(event=\"failure\", namespace=${namespace}, scope=\"node\")])\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_FAILURE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testJoinGatewayBefore() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[scopeListener(event=\"before\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_BEFORE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testJoinGatewaySuccess() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[scopeListener(event=\"success\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_SUCCESS_TRIGGERED.contains(namespace));
    }

    @Test
    public void testExclusiveGatewayBefore() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    }[scopeListener(event=\"before\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_BEFORE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testExclusiveGatewaySuccess() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    }[scopeListener(event=\"success\", namespace=${namespace}, scope=\"node\")]\n" +
                "}", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_SUCCESS_TRIGGERED.contains(namespace));
    }

    @Test
    public void testGlobalBefore() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    printAction(content=\"actionA\")\n" +
                "}[scopeListener(event=\"before\", namespace=${namespace}, scope=\"global\")]", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_BEFORE_TRIGGERED.contains(namespace));
    }

    @Test
    public void testGlobalSuccess() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    printAction(content=\"actionA\")\n" +
                "}[scopeListener(event=\"success\", namespace=${namespace}, scope=\"global\")]", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertTrue(ScopeListener.IS_SUCCESS_TRIGGERED.contains(namespace));
    }

    @Test
    public void testGlobalFailure() {
        String namespace = UUID.randomUUID().toString();

        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    throwExceptionAction()\n" +
                "}[scopeListener(event=\"failure\", namespace=${namespace}, scope=\"global\")]", EnvBuilder.builder().put("namespace", namespace).build());

        promise.sync();

        assertPromise(promise, false, true, false, true);

        Assert.assertTrue(ScopeListener.IS_FAILURE_TRIGGERED.contains(namespace));
    }
}
