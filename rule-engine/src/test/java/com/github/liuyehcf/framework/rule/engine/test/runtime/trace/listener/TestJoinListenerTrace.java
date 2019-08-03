package com.github.liuyehcf.framework.rule.engine.test.runtime.trace.listener;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import com.github.liuyehcf.framework.rule.engine.test.runtime.trace.TestTraceBase;
import org.junit.Test;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestJoinListenerTrace extends TestTraceBase {

    @Test
    public void testHardAndJoinSingleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testHardAndJoinSingleParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABC]");
            }

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testHardAndJoinCascadedAndParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
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
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABCDE]");
            }

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testHardAndJoinAndIfThenAndIfThenElseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testHardAndJoinConditionFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionInstance(executionInstance, 0, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (Objects.equals(ElementType.ACTION, trace.getType())) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertPrintCondition(trace, "conditionA", false);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionInstance(executionInstance, 0, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (Objects.equals("printAction", trace.getName())) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertThrowLinkTerminateAction(trace);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinWithNotSelectedNodesWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        throwLinkTerminateAction(),\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }else{\n" +
                "            throwExceptionAction()\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertPrintCondition(trace, "conditionA", false);
                }
            }
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[AB]");
            }

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
    public void testHardAndJoinNestedInIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testHardAndJoinNestedInSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testHardAndJoinNestedInHardAndJoinWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        join & {\n" +
                "            printAction(content=\"actionD\")&,\n" +
                "            printAction(content=\"actionE\"),\n" +
                "            printAction(content=\"actionF\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")],\n" +
                "        printAction(content=\"actionG\")&,\n" +
                "        printAction(content=\"actionH\")&\n" +
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
            assertExecutionInstance(executionInstance, 4, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionE");

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinSingleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinSingleParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABC]");
            }

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinCascadedAndParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
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
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABCDE]");
            }

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinAndIfThenAndIfThenElseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinConditionFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSoftAndJoinLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testSoftAndJoinWithNotSelectedNodesWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        throwLinkTerminateAction(),\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }else{\n" +
                "            throwExceptionAction()\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertPrintCondition(trace, "conditionA", false);
                }
            }
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[AB]");
            }

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
    public void testSoftAndJoinNestedInIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSoftAndJoinNestedInSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
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
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSoftAndJoinNestedInSoftAndJoinWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        join {\n" +
                "            printAction(content=\"actionD\")&,\n" +
                "            printAction(content=\"actionE\"),\n" +
                "            printAction(content=\"actionF\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")],\n" +
                "        printAction(content=\"actionG\")&,\n" +
                "        printAction(content=\"actionH\")&\n" +
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
            assertExecutionInstance(executionInstance, 4, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionE");

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testOrJoinSingleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testOrJoinSingleParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinCascadedAndParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            if (executionLink.getTraces().size() == 5) {
                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(3);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);
            } else {
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[BD]");

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[CE]");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(4);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinAndIfThenAndIfThenElseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 3, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);

            if (executionLink.getTraces().size() == 5) {
                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertPrintAction(trace, "actionA");
                } else {
                    assertPrintCondition(trace, "conditionA", true);
                }

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(3);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);
            } else {
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "condition[BC]", null);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[BC]");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(4);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinConditionFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 1, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);
        });
    }

    @Test
    public void testOrJoinLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 1, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);
        });
    }

    @Test
    public void testOrJoinWithNotSelectedNodesWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        throwLinkTerminateAction(),\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }else{\n" +
                "            throwExceptionAction()\n" +
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
            assertExecutionInstance(executionInstance, 1, 2, 3, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            if (executionLink.getTraces().size() == 5) {

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(3);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);
            } else {
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionB", true);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "actionB");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(4);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            if (executionInstance.getUnreachableLinks().size() > 0) {
                executionLink = executionInstance.getUnreachableLinks().get(0);
                assertExecutionLink(executionLink, 2, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", true);
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
    public void testOrJoinNestedInIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", true);
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testOrJoinNestedInSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
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
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testOrJoinNestedInOrJoinWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\"){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        join | {\n" +
                "            if(printCondition(content=\"conditionB\", output=false))&,\n" +
                "            printAction(content=\"actionC\"){\n" +
                "                printAction(content=\"actionD\"){\n" +
                "                    printAction(content=\"actionE\")&\n" +
                "                }\n" +
                "            }\n" +
                "        }[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")],\n" +
                "        printAction(content=\"actionF\"){\n" +
                "            printAction(content=\"actionG\")&\n" +
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
            assertExecutionInstance(executionInstance, 3, 0, 2, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() == 2) {
                    trace = executionLink.getTraces().get(1);

                    if (ElementType.CONDITION.equals(trace.getType())) {
                        assertPrintCondition(trace, "condition[AB]", false);
                    } else {
                        assertPrintListener(trace, "listenerF", ListenerEvent.success);
                    }
                } else if (executionLink.getTraces().size() == 3) {
                    // condition may be added when global listener is trigger
                    trace = executionLink.getTraces().get(1);
                    if (ElementType.CONDITION.equals(trace.getType())) {
                        assertPrintCondition(trace, "condition[AB]", false);
                    } else {
                        assertPrintListener(trace, "listenerF", ListenerEvent.success);
                    }

                    trace = executionLink.getTraces().get(2);
                    if (ElementType.CONDITION.equals(trace.getType())) {
                        assertPrintCondition(trace, "condition[AB]", false);
                    } else {
                        assertPrintListener(trace, "listenerF", ListenerEvent.success);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinThenSimpleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")] then {\n" +
                "        printAction(content=\"actionD\")\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 11);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenCascadeWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")] then {\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\"){\n" +
                "                if(printCondition(content=\"conditionD\", output=true)){\n" +
                "                    printAction(content=\"actionF\"){\n" +
                "                        if(printCondition(content=\"conditionE\", output=false)){\n" +
                "                            throwExceptionAction()\n" +
                "                        }else{\n" +
                "                            printAction(content=\"actionG\")\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 16);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(12);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(13);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(14);
            assertPrintCondition(trace, "conditionE", false);

            trace = executionLink.getTraces().get(15);
            assertPrintAction(trace, "actionG");
        });
    }

    @Test
    public void testHardAndJoinThenParallelWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")] then {\n" +
                "        printAction(content=\"actionD\"),\n" +
                "        printAction(content=\"actionE\"),\n" +
                "        printAction(content=\"actionF\")\n" +
                "    }\n" +
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
                assertExecutionLink(executionLink, 11);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(8);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);

                trace = executionLink.getTraces().get(10);
                assertPrintAction(trace, "action[DEF]");
            }
        });
    }

    @Test
    public void testHardAndJoinThenIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]then {\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\")\n" +
                "        }\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")] then {\n" +
                "        if(printCondition(content=\"conditionD\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertExecutionLink(executionLink, 11);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionD", false);
        });
    }

    @Test
    public void testHardAndJoinThenIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")]then {\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\")\n" +
                "        } else{\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")] then {\n" +
                "        if(printCondition(content=\"conditionD\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        } else{\n" +
                "            printAction(content=\"actionD\")\n" +
                "        }\n" +
                "    }\n" +
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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionD", false);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")] then {\n" +
                "        select{\n" +
                "            if(printCondition(content=\"conditionD\", output=${output1})){\n" +
                "                printAction(content=\"actionD\")\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionE\", output=${output2})){\n" +
                "                printAction(content=\"actionE\")\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionF\", output=${output3})){\n" +
                "                printAction(content=\"actionF\")\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]");

        executeTimes(() -> {
            EnvBuilder builder = EnvBuilder.builder();

            int trueNum = 0, falseNum = 0;

            for (int i = 1; i <= 3; i++) {
                boolean output = RANDOM.nextBoolean();
                builder.put("output" + i, output);

                if (output) {
                    trueNum++;
                } else {
                    falseNum++;
                }
            }

            Promise<ExecutionInstance> promise = startRule(rule, builder.build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            if (trueNum == 0) {
                assertExecutionInstance(executionInstance, 0, 3, 2);

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 12);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(7);
                    assertPrintListener(trace, "listenerC", ListenerEvent.before);

                    trace = executionLink.getTraces().get(8);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(9);
                    assertPrintListener(trace, "listenerD", ListenerEvent.success);

                    trace = executionLink.getTraces().get(10);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(11);
                    assertPrintCondition(trace, "condition[DEF]", false);
                }
            } else {
                assertExecutionInstance(executionInstance, 1, 0, 2, 2);

                executionLink = executionInstance.getLinks().get(0);
                assertExecutionLink(executionLink, 13);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerC", ListenerEvent.before);

                trace = executionLink.getTraces().get(8);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintListener(trace, "listenerD", ListenerEvent.success);

                trace = executionLink.getTraces().get(10);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(11);
                assertPrintCondition(trace, "condition[DEF]", true);

                trace = executionLink.getTraces().get(12);
                assertPrintAction(trace, "action[DEF]");

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 11, 14);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(7);
                    assertPrintListener(trace, "listenerC", ListenerEvent.before);

                    trace = executionLink.getTraces().get(8);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(9);
                    assertPrintListener(trace, "listenerD", ListenerEvent.success);

                    trace = executionLink.getTraces().get(10);
                    assertExclusiveGateway(trace);
                }
            }
        });
    }

    @Test
    public void testParallelHardAndJoinAndSoftAndJoinAndOrJoinWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    }[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")],\n" +
                "    join & {\n" +
                "        printAction(content=\"actionC\")&,\n" +
                "        printAction(content=\"actionD\")&,\n" +
                "        printAction(content=\"actionE\")&\n" +
                "    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")],\n" +
                "    join | {\n" +
                "        printAction(content=\"actionF\"){\n" +
                "            printAction(content=\"actionG\"){\n" +
                "                printAction(content=\"actionH\"){\n" +
                "                    printAction(content=\"actionI\")&\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerF\")]\n" +
                "}[printListener(event=\"before\", content=\"listenerG\"), printListener(event=\"success\", content=\"listenerH\")]");

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
            assertPrintListener(trace, "listenerG", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerH", ListenerEvent.success);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionH");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionI");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);
        });
    }
}
