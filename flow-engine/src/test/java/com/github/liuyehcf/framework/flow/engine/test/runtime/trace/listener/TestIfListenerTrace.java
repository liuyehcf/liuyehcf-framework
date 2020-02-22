package com.github.liuyehcf.framework.flow.engine.test.runtime.trace.listener;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import com.github.liuyehcf.framework.flow.engine.test.runtime.trace.TestTraceBase;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestIfListenerTrace extends TestTraceBase {

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

    @Test
    public void testSingleIfNestedInActionWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenElseTrueWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    } else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenElseFlaseWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        if(printCondition(content=\"conditionB\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinThenWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (ElementType.LISTENER.equals(trace.getType())) {
                    assertPrintListener(trace, "listenerA", ListenerEvent.before);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "conditionA", true);

                    trace = executionLink.getTraces().get(3);
                    assertPrintListener(trace, "listenerB", ListenerEvent.success);
                } else {
                    assertPrintAction(trace, "actionA");

                    trace = executionLink.getTraces().get(2);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertPrintAction(trace, "actionB");
                }
            }
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinThenWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (ElementType.LISTENER.equals(trace.getType())) {
                    assertPrintListener(trace, "listenerA", ListenerEvent.before);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "conditionA", true);

                    trace = executionLink.getTraces().get(3);
                    assertPrintListener(trace, "listenerB", ListenerEvent.success);
                } else {
                    assertPrintAction(trace, "actionA");

                    trace = executionLink.getTraces().get(2);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertPrintAction(trace, "actionB");
                }
            }
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinThenWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then  {\n" +
                "        printAction(content=\"actionC\")\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (ElementType.LISTENER.equals(trace.getType())) {
                    assertPrintListener(trace, "listenerA", ListenerEvent.before);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "conditionA", false);

                    trace = executionLink.getTraces().get(3);
                    assertPrintListener(trace, "listenerB", ListenerEvent.success);
                } else {
                    assertPrintAction(trace, "action[AB]");

                    trace = executionLink.getTraces().get(2);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinThenThen() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
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

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinThenThen() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
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

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinThenThen() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        });
    }

    @Test
    public void testSingleIfNestedInSelectWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=${output0})[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]),\n" +
                "        if(printCondition(content=\"conditionB\", output=${output1})[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]),\n" +
                "        if(printCondition(content=\"conditionC\", output=${output2})[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerF\")]),\n" +
                "        if(printCondition(content=\"conditionD\", output=${output3})[printListener(event=\"before\", content=\"listenerG\"), printListener(event=\"success\", content=\"listenerH\")]),\n" +
                "        if(printCondition(content=\"conditionE\", output=${output4})[printListener(event=\"before\", content=\"listenerI\"), printListener(event=\"success\", content=\"listenerJ\")])\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerK\"), printListener(event=\"success\", content=\"listenerL\")]");

        executeTimes(() -> {
            EnvBuilder builder = EnvBuilder.builder();
            int firstTrue = -1;
            for (int i = 0; i < 5; i++) {
                boolean nextOutput = RANDOM.nextBoolean();
                if (firstTrue == -1 && nextOutput) {
                    firstTrue = i;
                }
                builder.put("output" + i, nextOutput);
            }

            Promise<ExecutionInstance> promise = startFlow(flow, builder.build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerK", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerL", ListenerEvent.success);

            if (firstTrue == -1) {
                assertExecutionInstance(executionInstance, 5, 0, 2);

                for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                    executionLink = executionInstance.getLinks().get(i);
                    assertExecutionLink(executionLink, 5);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintListener(trace, "listener[ACEGI]", ListenerEvent.before);

                    trace = executionLink.getTraces().get(3);
                    assertPrintCondition(trace, "condition[ABCDE]", false);

                    trace = executionLink.getTraces().get(4);
                    assertPrintListener(trace, "listener[BDFHJ]", ListenerEvent.success);
                }
            } else {
                // finishOperation may be earlier than markOperation
                assertExecutionInstance(executionInstance, firstTrue + 1, 0, 5 - firstTrue - 1, 2);

                for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                    executionLink = executionInstance.getLinks().get(i);
                    assertExecutionLink(executionLink, 5);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintListener(trace, "listener[ACEGI]", ListenerEvent.before);

                    trace = executionLink.getTraces().get(3);
                    assertPrintCondition(trace, "condition[ABCDE]", null);

                    trace = executionLink.getTraces().get(4);
                    assertPrintListener(trace, "listener[BDFHJ]", ListenerEvent.success);
                }

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 2);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertExclusiveGateway(trace);
                }
            }
        });
    }

    @Test
    public void testSingleIfThenTrueWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionA\")\n" +
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
    public void testSingleIfThenFalseWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testIfThenCascadedWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            if(printCondition(content=\"conditionC\", output=true)){\n" +
                "                if(printCondition(content=\"conditionD\", output=true)){\n" +
                "                    if(printCondition(content=\"conditionE\", output=true)){\n" +
                "                        printAction(content=\"actionA\")\n" +
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

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintCondition(trace, "condition[A-E]", true);
            }

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testIfThenParalledWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        printAction(content=\"actionB\")\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionC\", output=true)){\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        printAction(content=\"actionD\")\n" +
                "    },\n" +
                "    printAction(content=\"actionE\")\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                if (executionLink.getTraces().size() == 2) {
                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintAction(trace, "actionE");
                } else if (executionLink.getTraces().size() == 3) {
                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintCondition(trace, "condition[AC]", true);

                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "action[BD]");
                } else {
                    assertExecutionLink(executionLink, 4);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintCondition(trace, "condition[AC]", true);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "condition[BD]", true);

                    trace = executionLink.getTraces().get(3);
                    assertPrintAction(trace, "action[AC]");
                }
            }
        });
    }

    @Test
    public void testIfThenLinkExceptionWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        throwLinkTerminateAction(),\n" +
                "        printAction(content=\"actionA\")\n" +
                "    },\n" +
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
            assertExecutionInstance(executionInstance, 1, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            executionLink = findUnreachableLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateAction(trace);

            executionLink = findUnreachableLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testSingleIfThenElseWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=${output1})){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            boolean output1 = RANDOM.nextBoolean();
            Map<String, Object> env = EnvBuilder.builder()
                    .put("output1", output1)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", output1);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");
        });
    }

    @Test
    public void testIfThenElseCascadedWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            if(printCondition(content=\"conditionC\", output=true)){\n" +
                "                printAction(content=\"actionA\")\n" +
                "            }else{\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testIfThenElseLinkExceptionWithGlobalListener() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\"),\n" +
                "        throwLinkTerminateAction()\n" +
                "    },\n" +
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
            assertExecutionInstance(executionInstance, 1, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            executionLink = findUnreachableLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateAction(trace);

            executionLink = findUnreachableLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);
        });
    }
}
