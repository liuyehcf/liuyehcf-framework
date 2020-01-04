package com.github.liuyehcf.framework.flow.engine.test.runtime.trace.listener;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import com.github.liuyehcf.framework.flow.engine.test.runtime.listener.PrintListener;
import com.github.liuyehcf.framework.flow.engine.test.runtime.listener.ThrowExceptionListener;
import com.github.liuyehcf.framework.flow.engine.test.runtime.listener.ThrowLinkTerminateListener;
import com.github.liuyehcf.framework.flow.engine.test.runtime.trace.TestTraceBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestCommonListenerTrace extends TestTraceBase {

    @Test
    public void testActionResultListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[successResultListener(event=\"success\")]\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
            assertSuccessResultListener(trace, null, ListenerEvent.success);
        });
    }

    @Test
    public void testConditionResultListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)[successResultListener(event=\"success\", result=true)]){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionB\", output=false)[successResultListener(event=\"success\", result=false)]){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertSuccessResultListener(trace, true, ListenerEvent.success);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(2);
            assertSuccessResultListener(trace, false, ListenerEvent.success);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testJoinGatewayResultListener() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[successResultListener(event=\"success\")],\n" +
                "    join  {\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    }[successResultListener(event=\"success\")],\n" +
                "    join | {\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }[successResultListener(event=\"success\")],\n" +
                "\n" +
                "    join & {\n" +
                "        printAction(content=\"actionD\")&\n" +
                "    }[successResultListener(event=\"success\")] then {\n" +
                "        printAction(content=\"actionE\")\n" +
                "    },\n" +
                "    join  {\n" +
                "        printAction(content=\"actionF\")&\n" +
                "    }[successResultListener(event=\"success\")] then{\n" +
                "        printAction(content=\"actionG\")\n" +
                "    },\n" +
                "    join | {\n" +
                "        printAction(content=\"actionH\")&\n" +
                "    }[successResultListener(event=\"success\")] then {\n" +
                "        printAction(content=\"actionI\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 6, 0, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                if (executionLink.getTraces().size() == 4) {
                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintAction(trace, "action[ABC]");

                    trace = executionLink.getTraces().get(2);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertSuccessResultListener(trace, null, ListenerEvent.success);
                } else {
                    assertExecutionLink(executionLink, 5);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintAction(trace, "action[DFH]");

                    trace = executionLink.getTraces().get(2);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertSuccessResultListener(trace, null, ListenerEvent.success);

                    trace = executionLink.getTraces().get(4);
                    assertPrintAction(trace, "action[EGI]");
                }
            }

        });
    }

    @Test
    public void testExclusiveGatewayResultListener() {
        Flow flow = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    }[successResultListener(event=\"success\")],\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionB\", output=false))\n" +
                "    }[successResultListener(event=\"success\")]\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertSuccessResultListener(trace, null, ListenerEvent.success);

                trace = executionLink.getTraces().get(3);
                assertPrintCondition(trace, "condition[AB]", null);
            }
        });
    }

    @Test
    public void testSubFlowResultListener() {
        Flow flow = compile("{\n" +
                "    sub {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    }[successResultListener(event=\"success\", result=true)],\n" +
                "    sub {\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }[successResultListener(event=\"success\", result=false)]\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 0);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertSuccessResultListener(trace, true, ListenerEvent.success);

            trace = executionLink.getTraces().get(5);
            assertFlow(trace);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(3);
            assertSuccessResultListener(trace, false, ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertFlow(trace);
        });
    }

    @Test
    public void testGlobalTrueResultListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}[successResultListener(event=\"success\", result=true)]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertSuccessResultListener(trace, true, ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testGlobalFalseResultListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[successResultListener(event=\"success\", result=false)]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 1);

            trace = executionInstance.getTraces().get(0);
            assertSuccessResultListener(trace, false, ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testActionWithBeforeExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[throwExceptionListener(event=\"before\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithMultiBeforeExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"before\", content=\"listenerA\"), throwExceptionListener(event=\"before\"), printListener(event=\"before\", content=\"listenerB\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithBeforeLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[throwLinkTerminateListener(event=\"before\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
            assertThrowLinkTerminateListener(trace, ListenerEvent.before);
        });
    }

    @Test
    public void testActionWithMultiBeforeLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"before\", content=\"listenerA\"), throwLinkTerminateListener(event=\"before\"), printListener(event=\"before\", content=\"listenerB\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateListener(trace, ListenerEvent.before);
        });
    }

    @Test
    public void testActionWithSuccessExceptionListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[throwExceptionListener(event=\"success\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithMultiSuccessExceptionListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[printListener(event=\"success\", content=\"listenerA\"), throwExceptionListener(event=\"success\"), printListener(event=\"success\", content=\"listenerB\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithSuccessLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[throwLinkTerminateListener(event=\"success\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateListener(trace, ListenerEvent.success);
        });
    }

    @Test
    public void testActionWithMultiSuccessLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[printListener(event=\"success\", content=\"listenerA\"), throwLinkTerminateListener(event=\"success\"), printListener(event=\"success\", content=\"listenerB\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.success);

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateListener(trace, ListenerEvent.success);
        });
    }

    @Test
    public void testActionWithGlobalBeforeExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwExceptionListener(event=\"before\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithGlobalMultiFirstBeforeExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwExceptionListener(event=\"before\", namespace=${namespace}), printListener(event=\"before\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"before\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.BEFORE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.BEFORE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(0, printBeforeCounter.get());

            ThrowExceptionListener.BEFORE_COUNTER.remove(namespace);
            PrintListener.BEFORE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiMiddleBeforeExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"before\", content=\"listenerA\", namespace=${namespace}), throwExceptionListener(event=\"before\", namespace=${namespace}), printListener(event=\"before\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.BEFORE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.BEFORE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(1, printBeforeCounter.get());

            ThrowExceptionListener.BEFORE_COUNTER.remove(namespace);
            PrintListener.BEFORE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiLastBeforeExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"before\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"before\", content=\"listenerB\", namespace=${namespace}), throwExceptionListener(event=\"before\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.BEFORE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.BEFORE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(2, printBeforeCounter.get());

            ThrowExceptionListener.BEFORE_COUNTER.remove(namespace);
            PrintListener.BEFORE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalBeforeLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwLinkTerminateListener(event=\"before\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertThrowLinkTerminateListener(trace, ListenerEvent.before);
        });
    }

    @Test
    public void testActionWithGlobalMultiBeforeFirstLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwLinkTerminateListener(event=\"before\"), printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"before\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertThrowLinkTerminateListener(trace, ListenerEvent.before);
        });
    }

    @Test
    public void testActionWithGlobalMultiBeforeMiddleLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"),throwLinkTerminateListener(event=\"before\"), printListener(event=\"before\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertThrowLinkTerminateListener(trace, ListenerEvent.before);
        });
    }

    @Test
    public void testActionWithGlobalMultiBeforeLastLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"before\", content=\"listenerB\"), throwLinkTerminateListener(event=\"before\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 0, 3);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.before);

            trace = executionInstance.getTraces().get(2);
            assertThrowLinkTerminateListener(trace, ListenerEvent.before);
        });
    }

    @Test
    public void testActionWithGlobalSuccessExceptionListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[throwExceptionListener(event=\"success\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithGlobalMultiFirstSuccessExceptionListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[throwExceptionListener(event=\"success\", namespace=${namespace}), printListener(event=\"success\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"success\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.SUCCESS_COUNTER.put(namespace, exceptionCounter);
            PrintListener.SUCCESS_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(0, printBeforeCounter.get());

            ThrowExceptionListener.SUCCESS_COUNTER.remove(namespace);
            PrintListener.SUCCESS_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiMiddleSuccessExceptionListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[printListener(event=\"success\", content=\"listenerA\", namespace=${namespace}), throwExceptionListener(event=\"success\", namespace=${namespace}), printListener(event=\"success\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.SUCCESS_COUNTER.put(namespace, exceptionCounter);
            PrintListener.SUCCESS_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(1, printBeforeCounter.get());

            ThrowExceptionListener.SUCCESS_COUNTER.remove(namespace);
            PrintListener.SUCCESS_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiLastSuccessExceptionListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[printListener(event=\"success\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"success\", content=\"listenerB\", namespace=${namespace}), throwExceptionListener(event=\"success\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.SUCCESS_COUNTER.put(namespace, exceptionCounter);
            PrintListener.SUCCESS_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(2, printBeforeCounter.get());

            ThrowExceptionListener.SUCCESS_COUNTER.remove(namespace);
            PrintListener.SUCCESS_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalSuccessLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[throwLinkTerminateListener(event=\"success\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertThrowLinkTerminateListener(trace, ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[ABC]");
            }
        });
    }

    @Test
    public void testActionWithGlobalMultiFirstSuccessLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[throwLinkTerminateListener(event=\"success\"), printListener(event=\"success\", content=\"listenerA\"),printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertThrowLinkTerminateListener(trace, ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[ABC]");
            }
        });
    }

    @Test
    public void testActionWithGlobalMultiMiddleSuccessLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[printListener(event=\"success\", content=\"listenerA\"),throwLinkTerminateListener(event=\"success\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.success);

            trace = executionInstance.getTraces().get(1);
            assertThrowLinkTerminateListener(trace, ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[ABC]");
            }
        });
    }

    @Test
    public void testActionWithGlobalMultiLastSuccessLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[printListener(event=\"success\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\"),throwLinkTerminateListener(event=\"success\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 3);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.success);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionInstance.getTraces().get(2);
            assertThrowLinkTerminateListener(trace, ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[ABC]");
            }
        });
    }

    @Test
    public void testActionWithFailurePrintListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger counter = new AtomicInteger();
            PrintListener.FAILURE_COUNTER.put(namespace, counter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            assertEquals(1, counter.get());

            assertNotNull(PrintListener.FAILURE_COUNTER.remove(namespace));
        });
    }

    @Test
    public void testActionWithMultiFailurePrintListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerC\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(3, printBeforeCounter.get());

            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[throwExceptionListener(event=\"failure\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger counter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, counter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            assertEquals(1, counter.get());

            assertNotNull(ThrowExceptionListener.FAILURE_COUNTER.remove(namespace));
        });
    }

    @Test
    public void testActionWithMultiFirstFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[throwExceptionListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(0, printBeforeCounter.get());

            ThrowExceptionListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithMultiMiddleFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), throwExceptionListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(1, printBeforeCounter.get());

            ThrowExceptionListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithMultiLastFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace}), throwExceptionListener(event=\"failure\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(2, printBeforeCounter.get());

            ThrowExceptionListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[throwLinkTerminateListener(event=\"failure\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger counter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, counter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            assertEquals(1, counter.get());

            assertNotNull(ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace));
        });
    }

    @Test
    public void testActionWithMultiFirstFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[throwLinkTerminateListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(0, printBeforeCounter.get());

            ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithMultiMiddleFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), throwLinkTerminateListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(1, printBeforeCounter.get());

            ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithMultiLastFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace}), throwLinkTerminateListener(event=\"failure\", namespace=${namespace})]\n" +
                "}");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(2, printBeforeCounter.get());

            ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwExceptionListener(event=\"failure\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger counter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, counter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            assertEquals(1, counter.get());

            assertNotNull(ThrowExceptionListener.FAILURE_COUNTER.remove(namespace));
        });
    }

    @Test
    public void testActionWithGlobalMultiFirstFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwExceptionListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(0, printBeforeCounter.get());

            ThrowExceptionListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiMiddleFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), throwExceptionListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(1, printBeforeCounter.get());

            ThrowExceptionListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiLastFailureExceptionListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace}), throwExceptionListener(event=\"failure\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowExceptionListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(2, printBeforeCounter.get());

            ThrowExceptionListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwLinkTerminateListener(event=\"failure\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger counter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, counter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            assertEquals(1, counter.get());

            assertNotNull(ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace));
        });
    }

    @Test
    public void testActionWithGlobalMultiFirstFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwLinkTerminateListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(0, printBeforeCounter.get());

            ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiMiddleFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), throwLinkTerminateListener(event=\"failure\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(1, printBeforeCounter.get());

            ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testActionWithGlobalMultiLastFailureLinkTerminationListener() {
        Flow flow = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[printListener(event=\"failure\", content=\"listenerA\", namespace=${namespace}), printListener(event=\"failure\", content=\"listenerB\", namespace=${namespace}), throwLinkTerminateListener(event=\"failure\", namespace=${namespace})]");

        executeTimes(() -> {
            String namespace = UUID.randomUUID().toString();

            AtomicInteger exceptionCounter = new AtomicInteger();
            AtomicInteger printBeforeCounter = new AtomicInteger();
            ThrowLinkTerminateListener.FAILURE_COUNTER.put(namespace, exceptionCounter);
            PrintListener.FAILURE_COUNTER.put(namespace, printBeforeCounter);

            Map<String, Object> env = EnvBuilder.builder()
                    .put("namespace", namespace)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, false, true);

            Assert.assertEquals(1, exceptionCounter.get());
            Assert.assertEquals(2, printBeforeCounter.get());

            ThrowLinkTerminateListener.FAILURE_COUNTER.remove(namespace);
            PrintListener.FAILURE_COUNTER.remove(namespace);
        });
    }

    @Test
    public void testCascadeMultipleWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\"){\n" +
                "                printAction(content=\"actionD\"){\n" +
                "                    printAction(content=\"actionE\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[A-E]");
            }
        });
    }

    @Test
    public void testCascadeIfThenTrueWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=true)){\n" +
                "                printAction(content=\"actionC\"){\n" +
                "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                        printAction(content=\"actionD\")\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testCascadeIfThenFalseWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testCascadeIfThenElseTrueWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=true)){\n" +
                "                printAction(content=\"actionC\"){\n" +
                "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                        printAction(content=\"actionD\")\n" +
                "                    }\n" +
                "                }\n" +
                "            }else{\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testCascadeIfThenElseFalseWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                printAction(content=\"actionC\"){\n" +
                "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                        printAction(content=\"actionD\")\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testCascadeLinkExceptionWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testParallelMultipleWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerF\")],\n" +
                "    printAction(content=\"actionB\")[printListener(event=\"before\", content=\"listenerB\"), printListener(event=\"success\", content=\"listenerG\")],\n" +
                "    printAction(content=\"actionC\")[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerH\")],\n" +
                "    printAction(content=\"actionD\")[printListener(event=\"before\", content=\"listenerD\"), printListener(event=\"success\", content=\"listenerI\")],\n" +
                "    printAction(content=\"actionE\")[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerJ\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerK\"), printListener(event=\"success\", content=\"listenerL\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 5, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerK", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerL", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintListener(trace, "listener[A-E]", ListenerEvent.before);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[A-E]");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listener[F-J]", ListenerEvent.success);
            }
        });
    }

    @Test
    public void testParallelWithCascadeWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\"),\n" +
                "            printAction(content=\"actionD\"){\n" +
                "                printAction(content=\"actionE\")\n" +
                "            }\n" +
                "        },\n" +
                "        printAction(content=\"actionF\")\n" +
                "    },\n" +
                "    printAction(content=\"actionG\")\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 4, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionG");

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionF");

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionE");
        });
    }

    @Test
    public void testParallelIfThenTrueWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testParallelLinkExceptionWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    throwLinkTerminateAction(),\n" +
                "    throwLinkTerminateAction(),\n" +
                "    throwLinkTerminateAction(),\n" +
                "    printAction(content=\"actionB\"),\n" +
                "    throwLinkTerminateAction()\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 4, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[AB]");
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertThrowLinkTerminateAction(trace);
            }
        });
    }

    @Test
    public void testParallelIfThenElseTrueWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testParallelIfThenElseFalseWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testParallelIfThenAndIfThenElseLinkExceptionWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    throwLinkTerminateAction(),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        throwLinkTerminateAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "        throwLinkTerminateAction()\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionC\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        throwLinkTerminateAction()\n" +
                "    },\n" +
                "    if(throwLinkTerminateCondition()){\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(throwLinkTerminateCondition()){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    printAction(content=\"actionA\")\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 6, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                if (executionLink.getTraces().size() == 3) {
                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintCondition(trace, "condition[A-C]", null);

                    trace = executionLink.getTraces().get(2);
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertExecutionLink(executionLink, 2);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    if (trace.getType().equals(ElementType.ACTION)) {
                        assertThrowLinkTerminateAction(trace);
                    } else {
                        assertThrowLinkTerminateCondition(trace);
                    }
                }
            }
        });
    }

    @Test
    public void testSingleIfWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testParallelSingleIfWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]),\n" +
                "    if(printCondition(content=\"conditionB\", output=true)[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]),\n" +
                "    if(printCondition(content=\"conditionC\", output=false)[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerF\")])\n" +
                "}[printListener(event=\"before\", content=\"listenerG\"), printListener(event=\"success\", content=\"listenerH\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerG", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerH", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintListener(trace, "listener[ACE]", ListenerEvent.before);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[ABC]", null);

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listener[BDF]", ListenerEvent.success);
            }
        });
    }
}
