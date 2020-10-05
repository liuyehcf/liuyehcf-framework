package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.runtime.exception.LinkExecutionTerminateException;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import com.github.liuyehcf.framework.flow.engine.test.runtime.action.MarkAction;
import com.github.liuyehcf.framework.flow.engine.test.runtime.listener.MarkListener;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2020/10/2
 */
public class TestPauseTrace extends TestTraceBase {

    // action
    @Test
    public void testActionPauseEnd() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause})\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseAction(trace, timeout, false, true, null);
            long timestamp1 = trace.getStartTimestamp();

            long timestamp2 = executionInstance.getEndTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testActionPauseSuccessWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}) {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseAction(trace, timeout, false, true, null);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
            long timestamp2 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testActionPauseSuccessWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}) [printListener(event=\"success\", content=\"listenerA\")] {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}\n");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseAction(trace, timeout, false, true, null);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.success);
            long timestamp2 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testActionPauseCancelWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause}) {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);
        }, 100);
    }

    @Test
    public void testActionPauseCancelWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause}) [markListener(event=\"success\", id=${id}), markListener(event=\"failure\", id=${id})] {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "} [markListener(event=\"success\", id=${globalId}), markListener(event=\"failure\", id=${globalId})]");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            String id = UUID.randomUUID().toString();
            String globalId = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .put("globalId", globalId)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(globalId));
            Assert.assertTrue(MarkListener.FAILURE_CACHE.contains(globalId));
        }, 100);
    }

    @Test
    public void testActionPauseLinkTerminationFailureWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    join {\n" +
                        "        pauseAction(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause})&,\n" +
                        "        printAction(content=\"actionA\")&\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new LinkExecutionTerminateException())
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
            long timestamp2 = trace.getStartTimestamp();

            executionLink = executionInstance.getUnreachableLinks().get(0);
            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseAction(trace, timeout, false, false, LinkExecutionTerminateException.class);
            long timestamp1 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testActionPauseLinkTerminationFailureWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause})[markListener(event=\"success\", id=${id}), markListener(event=\"failure\", id=${id})] {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new LinkExecutionTerminateException())
                    .put("id", id)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseAction(trace, timeout, false, false, LinkExecutionTerminateException.class);
            long timestamp1 = trace.getStartTimestamp();

            long timestamp2 = executionInstance.getEndTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));
        }, 100);
    }

    @Test
    public void testActionPauseUnknownFailureWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause}) {\n" +
                        "        sub {\n" +
                        "            printAction(content=\"actionA\")\n" +
                        "        }\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new RuntimeException())
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);
        }, 100);
    }

    @Test
    public void testActionPauseUnknownFailureWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    pauseAction(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause}) [markListener(event=\"success\", id=${id}), markListener(event=\"failure\", id=${id})] {\n" +
                        "        select {\n" +
                        "            if(printCondition(content=\"conditionA\", output=true))\n" +
                        "        }\n" +
                        "    }\n" +
                        "}[markListener(event=\"success\", id=${globalId}), markListener(event=\"failure\", id=${globalId})]");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();
            String globalId = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new RuntimeException())
                    .put("id", id)
                    .put("globalId", globalId)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(globalId));
            Assert.assertTrue(MarkListener.FAILURE_CACHE.contains(globalId));
        }, 100);
    }

    // condition
    @Test
    public void testConditionPauseEnd() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}, output=true))\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseCondition(trace, timeout, false, true, null, true);
            long timestamp1 = trace.getStartTimestamp();

            long timestamp2 = executionInstance.getEndTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testConditionPauseSuccessWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}, output=true)) {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseCondition(trace, timeout, false, true, null, true);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
            long timestamp2 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testConditionPauseSuccessWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}, output=true) [printListener(event=\"success\", content=\"listenerA\")] ){\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}\n");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseCondition(trace, timeout, false, true, null, true);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.success);
            long timestamp2 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testConditionPauseCancelWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause})) {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);
        }, 100);
    }

    @Test
    public void testConditionPauseCancelWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause}, output=false) [markListener(event=\"success\", id=${id}), markListener(event=\"failure\", id=${id})]) {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "} [markListener(event=\"success\", id=${globalId}), markListener(event=\"failure\", id=${globalId})]");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            String id = UUID.randomUUID().toString();
            String globalId = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .put("globalId", globalId)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(globalId));
            Assert.assertTrue(MarkListener.FAILURE_CACHE.contains(globalId));
        }, 100);
    }

    @Test
    public void testConditionPauseLinkTerminationFailureWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    join {\n" +
                        "        if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause}, output=true))&,\n" +
                        "        printAction(content=\"actionA\")&\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new LinkExecutionTerminateException())
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
            long timestamp2 = trace.getStartTimestamp();

            executionLink = executionInstance.getUnreachableLinks().get(0);
            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseCondition(trace, timeout, false, false, LinkExecutionTerminateException.class, true);
            long timestamp1 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testConditionPauseLinkTerminationFailureWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause}, output=true)[markListener(event=\"success\", id=${id}), markListener(event=\"failure\", id=${id})]) {\n" +
                        "        printAction(content=\"actionA\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new LinkExecutionTerminateException())
                    .put("id", id)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseCondition(trace, timeout, false, false, LinkExecutionTerminateException.class, true);
            long timestamp1 = trace.getStartTimestamp();

            long timestamp2 = executionInstance.getEndTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));
        }, 100);
    }

    @Test
    public void testConditionPauseUnknownFailureWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause}, output=true)) {\n" +
                        "        sub {\n" +
                        "            printAction(content=\"actionA\")\n" +
                        "        }\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new RuntimeException())
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);
        }, 100);
    }

    @Test
    public void testConditionPauseUnknownFailureWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    if (pauseCondition(pause=${timeout}, isCancel=false, isSuccess=false, cause=${cause}, output=true) [markListener(event=\"success\", id=${id}), markListener(event=\"failure\", id=${id})]) {\n" +
                        "        select {\n" +
                        "            if(printCondition(content=\"conditionA\", output=true))\n" +
                        "        }\n" +
                        "    }\n" +
                        "}[markListener(event=\"success\", id=${globalId}), markListener(event=\"failure\", id=${globalId})]");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();
            String globalId = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("cause", new RuntimeException())
                    .put("id", id)
                    .put("globalId", globalId)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(globalId));
            Assert.assertTrue(MarkListener.FAILURE_CACHE.contains(globalId));
        }, 100);
    }

    // listener
    @Test
    public void testSuccessListenerPauseEnd() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [pauseListener(event=\"success\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause})]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.success);
            long timestamp1 = trace.getStartTimestamp();

            long timestamp2 = executionInstance.getEndTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testFailureListenerPauseEnd() {
        Flow flow = compile(
                "{\n" +
                        "    throwLinkTerminateAction() [pauseListener(event=\"failure\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause})]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateAction(trace);

            trace = executionLink.getTraces().get(2);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.failure);
            long timestamp1 = trace.getStartTimestamp();

            long timestamp2 = executionInstance.getEndTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testBeforeListenerPauseSuccessWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [\n" +
                        "        pauseListener(event=\"before\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}),\n" +
                        "        printListener(event=\"before\" , content=\"listenerA\")\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.before);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);
            long timestamp2 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testBeforeListenerPauseSuccessWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [\n" +
                        "        pauseListener(event=\"before\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause})\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.before);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
            long timestamp2 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testSuccessListenerPauseSuccessWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [\n" +
                        "        pauseListener(event=\"success\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}),\n" +
                        "        printListener(event=\"success\" , content=\"listenerA\")\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.success);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.success);
            long timestamp2 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testSuccessListenerPauseSuccessWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [\n" +
                        "        pauseListener(event=\"success\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause})\n" +
                        "    ] {\n" +
                        "        printAction(content=\"actionB\")\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.success);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
            long timestamp2 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testFailureListenerPauseSuccessWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    throwLinkTerminateAction() [\n" +
                        "        pauseListener(event=\"failure\" , pause=${timeout}, isCancel=false, isSuccess=true, cause=${cause}),\n" +
                        "        printListener(event=\"failure\" , content=\"listenerA\")\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateAction(trace);

            trace = executionLink.getTraces().get(2);
            assertPauseListener(trace, timeout, false, true, null, ListenerEvent.failure);
            long timestamp1 = trace.getStartTimestamp();

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.failure);
            long timestamp2 = trace.getStartTimestamp();

            Assert.assertTrue(timestamp2 - timestamp1 >= timeout);
        }, 100);
    }

    @Test
    public void testBeforeListenerPauseCancelWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [\n" +
                        "        pauseListener(event=\"before\" , pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause}),\n" +
                        "        markListener(event=\"before\" , id=${id})\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.BEFORE_CACHE.contains(id));
        }, 100);
    }

    @Test
    public void testBeforeListenerPauseCancelWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    markAction(id=${id}) [pauseListener(event=\"before\" , pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause})] \n" +
                        "}\n");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkAction.CACHE.contains(id));
        }, 100);
    }

    @Test
    public void testSuccessListenerPauseCancelWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [\n" +
                        "        pauseListener(event=\"success\" , pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause}),\n" +
                        "        markListener(event=\"success\" , id=${id})\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.SUCCESS_CACHE.contains(id));
        }, 100);
    }

    @Test
    public void testSuccessListenerPauseCancelWithNode() {
        Flow flow = compile(
                "{\n" +
                        "    printAction(content=\"actionA\") [pauseListener(event=\"success\" , pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause})] {\n" +
                        "        markAction(id=${id})\n" +
                        "    }\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .build());

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkAction.CACHE.contains(id));
        }, 100);
    }

    // todo 问题在于，当throwLinkTerminateAction抛出异常后，将节点标记为不可达的动作与pause同步进行了，以至于流程结束的动作早于pause 被 Cancel
    @Test
    public void testFailureListenerPauseCancelWithListener() {
        Flow flow = compile(
                "{\n" +
                        "    throwLinkTerminateAction() [\n" +
                        "        pauseListener(event=\"failure\" , pause=${timeout}, isCancel=true, isSuccess=false, cause=${cause}),\n" +
                        "        markListener(event=\"failure\" , id=${id})\n" +
                        "    ]\n" +
                        "}");

        executeTimes(() -> {
            long timeout = RANDOM.nextInt(100);
            String id = UUID.randomUUID().toString();

            Promise<ExecutionInstance> promise = startFlow(flow, EnvBuilder
                    .builder()
                    .put("timeout", timeout)
                    .put("id", id)
                    .build());

            promise.sync();

            assertPromise(promise, false, true, false, true);

            Assert.assertFalse(MarkListener.FAILURE_CACHE.contains(id));
        }, 100);
    }

    @Test
    public void testFailureListenerPauseCancelWithNode() {

    }

    // global listener
}