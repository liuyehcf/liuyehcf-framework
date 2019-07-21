package com.github.liuyehcf.framework.rule.engine.test.runtime.trace.listener;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import com.github.liuyehcf.framework.rule.engine.test.runtime.trace.TestTraceBase;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestSelectListenerTrace extends TestTraceBase {

    @Test
    public void testSelectCascadedWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\"){\n" +
                "                printAction(content=\"actionB\"){\n" +
                "                    printAction(content=\"actionC\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSelectParallelWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\"),\n" +
                "            printAction(content=\"actionB\"),\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(2);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);

                trace = executionLink.getTraces().get(4);
                assertPrintCondition(trace, "conditionA", true);

                trace = executionLink.getTraces().get(5);
                assertPrintAction(trace, "action[ABC]");
            }
        });
    }

    @Test
    public void testSelectFirstTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=true)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSelectMiddleTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=true)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionB");

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSelectLastTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=true)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(2);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);

                trace = executionLink.getTraces().get(4);
                assertPrintCondition(trace, "condition[AB]", false);
            }
        });
    }

    @Test
    public void testSelectAllFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 3, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(2);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);

                trace = executionLink.getTraces().get(4);
                assertPrintCondition(trace, "condition[ABC]", false);
            }
        });
    }

    @Test
    public void testSelectLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(throwLinkTerminateCondition()){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(throwLinkTerminateCondition()){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionA\", output=true)) {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);

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
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(2);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);

                trace = executionLink.getTraces().get(4);
                assertThrowLinkTerminateCondition(trace);
            }
        });
    }

    @Test
    public void testParallelSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")],\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\"){\n" +
                "                printAction(content=\"actionE\")\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerF\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);

            for (ExecutionLink unreachableLink : executionInstance.getUnreachableLinks()) {
                assertExecutionLink(unreachableLink, 5);

                trace = unreachableLink.getTraces().get(0);
                assertStart(trace);

                trace = unreachableLink.getTraces().get(1);
                assertPrintListener(trace, "listener[AC]", ListenerEvent.before);

                trace = unreachableLink.getTraces().get(2);
                assertExclusiveGateway(trace);

                trace = unreachableLink.getTraces().get(3);
                assertPrintListener(trace, "listener[BD]", ListenerEvent.success);

                trace = unreachableLink.getTraces().get(4);
                assertPrintCondition(trace, "condition[AC]", false);
            }

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionB");

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionE");
        });
    }
}
