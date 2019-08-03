package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestJoinTrace extends TestTraceBase {

    @Test
    public void testHardAndJoinSingle() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSingleParalleld() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABC]");
            }

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinCascadedAndParalleld() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABCDE]");
            }

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinAndIfThenAndIfThenElse() {
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
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinConditionFalse() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 2, 0);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (trace.getType().equals(ElementType.ACTION)) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertPrintCondition(trace, "conditionA", false);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinLinkException() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 2, 0);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (trace.getName().equals("printAction")) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertThrowLinkTerminateAction(trace);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinWithNotSelectedNodes() {
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
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

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
    public void testHardAndJoinNestedInIfThenTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[AB]");
            }

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenElseTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenElseFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testHardAndJoinNestedInSelect() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testHardAndJoinNestedInHardAndJoin() {
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
                "        },\n" +
                "        printAction(content=\"actionG\")&,\n" +
                "        printAction(content=\"actionH\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 4, 0, 0);

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

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinEnvMerge() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"b.c\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d.e\",value=5)&\n" +
                "        }\n" +
                "    } [\n" +
                "        getPropertyListener(event=\"before\", name=\"a\", expectedValue=1),\n" +
                "        getPropertyListener(event=\"success\", name=\"b.c\", expectedValue=2),\n" +
                "        getPropertyListener(event=\"before\", name=\"c\", expectedValue=3),\n" +
                "        getPropertyListener(event=\"success\", name=\"d.e\", expectedValue=5)\n" +
                "      ]\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("b", Maps.newHashMap())
                    .put("d", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSingle() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSingleParalleld() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABC]");
            }

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinCascadedAndParalleld() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABCDE]");
            }

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinAndIfThenAndIfThenElse() {
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
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinConditionFalse() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSoftAndJoinLinkException() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testSoftAndJoinWithNotSelectedNodes() {
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
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

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
    public void testSoftAndJoinNestedInIfThenTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[AB]");
            }

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenElseTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenElseFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenAndIfThenElse() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionB\")&,\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        }\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionD\")&,\n" +
                "            printAction(content=\"actionE\")&,\n" +
                "            printAction(content=\"actionF\")&\n" +
                "        }\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionC\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        join {\n" +
                "            printAction(content=\"actionG\")&,\n" +
                "            printAction(content=\"actionH\")&,\n" +
                "            printAction(content=\"actionI\")&,\n" +
                "            printAction(content=\"actionJ\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            assertExecutionInstance(executionInstance, 4, 0, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[BC]");
            }

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionB", true);

            for (int i = 2; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[DEF]");
            }

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionC", false);

            for (int i = 2; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[GHIJ]");
            }

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinNestedInSelect() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinNestedInSoftAndJoin() {
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
                "        },\n" +
                "        printAction(content=\"actionG\")&,\n" +
                "        printAction(content=\"actionH\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 4, 0, 0);

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

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinWithRandomCondition() {
        Rule rule = compile("{\n" +
                "    join{\n" +
                "        if(printCondition(content=\"conditionA\", output=${output1}))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=${output2}))&,\n" +
                "        if(printCondition(content=\"conditionC\", output=${output3}))&,\n" +
                "        if(printCondition(content=\"conditionD\", output=${output4}))&,\n" +
                "        if(printCondition(content=\"conditionE\", output=${output5}))&,\n" +
                "        if(printCondition(content=\"conditionF\", output=${output6}))&,\n" +
                "        if(printCondition(content=\"conditionG\", output=${output7}))&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            EnvBuilder builder = EnvBuilder.builder();
            int trueNum = 0;
            int falseNum = 0;
            for (int i = 1; i <= 7; i++) {
                boolean b = RANDOM.nextBoolean();
                builder.put("output" + i, b);

                if (b) {
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

            if (trueNum == 0) {
                assertExecutionInstance(executionInstance, 0, 7, 0);

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 2);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertPrintCondition(trace, "condition[A-G]", false);
                }
            } else {
                assertExecutionInstance(executionInstance, 1, falseNum, 0);

                executionLink = executionInstance.getLinks().get(0);
                assertExecutionLink(executionLink, 2 + trueNum);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                for (int i = 1; i < trueNum + 1; i++) {
                    trace = executionLink.getTraces().get(i);
                    assertPrintCondition(trace, "condition[A-G]", null);
                }

                trace = executionLink.getTraces().get(trueNum + 1);
                assertJoinGateway(trace);
            }
        });
    }

    @Test
    public void testSoftAndJoinEnvMerge1() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"b.c\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d.e\",value=5)&\n" +
                "        }\n" +
                "    } [\n" +
                "        getPropertyListener(event=\"before\", name=\"a\", expectedValue=1),\n" +
                "        getPropertyListener(event=\"success\", name=\"b.c\", expectedValue=2),\n" +
                "        getPropertyListener(event=\"before\", name=\"c\", expectedValue=3),\n" +
                "        getPropertyListener(event=\"success\", name=\"d.e\", expectedValue=5)\n" +
                "      ]\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("b", Maps.newHashMap())
                    .put("d", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinEnvMerge2() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(setPropertyCondition(name=\"a\",value=1, output=false))&,\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d.e\",value=5)&\n" +
                "        }\n" +
                "    } [\n" +
                "        getPropertyListener(event=\"before\", name=\"a\"),\n" +
                "        getPropertyListener(event=\"before\", name=\"c\", expectedValue=3),\n" +
                "        getPropertyListener(event=\"success\", name=\"d.e\", expectedValue=5)\n" +
                "      ]\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("b", Maps.newHashMap())
                    .put("d", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinEnvMerge3() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(setPropertyCondition(name=\"a\",value=1, output=true)){\n" +
                "            throwLinkTerminateAction()&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d.e\",value=5)&\n" +
                "        }\n" +
                "    } [\n" +
                "        getPropertyListener(event=\"before\", name=\"a\"),\n" +
                "        getPropertyListener(event=\"before\", name=\"c\", expectedValue=3),\n" +
                "        getPropertyListener(event=\"success\", name=\"d.e\", expectedValue=5)\n" +
                "      ]\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("b", Maps.newHashMap())
                    .put("d", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinEnvMerge4() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(setPropertyCondition(name=\"a\",value=\"123\", output=false))&,\n" +
                "        printAction(content=\"actionA\"){\n" +
                "            if(setPropertyCondition(name=\"b\",value=\"456\", output=false)){\n" +
                "                throwExceptionAction()&\n" +
                "            }\n" +
                "        },\n" +
                "        if(setPropertyCondition(name=\"c\",value=\"789\", output=true)){\n" +
                "            getPropertyAction(name=\"c\", expectedValue=\"789\")&\n" +
                "        }\n" +
                "    } [\n" +
                "        getPropertyListener(event=\"success\", name=\"a\"), \n" +
                "        getPropertyListener(event=\"success\", name=\"b\"),\n" +
                "        getPropertyListener(event=\"success\", name=\"c\", expectedValue=\"789\")\n" +
                "    ]\n" +
                "}");

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

            assertExecutionInstance(executionInstance, 1, 2, 0);

            executionLink = findUnreachableLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetPropertyCondition(trace, "a", "123", false, PropertyUpdateType.CREATE, null);

            executionLink = findUnreachableLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertSetPropertyCondition(trace, "b", "456", false, PropertyUpdateType.CREATE, null);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetPropertyCondition(trace, "c", "789", true, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(2);
            assertGetPropertyAction(trace, "c", "789");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertGetPropertyListener(trace, "a", null, ListenerEvent.success);

            trace = executionLink.getTraces().get(5);
            assertGetPropertyListener(trace, "b", null, ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertGetPropertyListener(trace, "c", "789", ListenerEvent.success);
        });
    }

    @Test
    public void testOrJoinSingle() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinSingleParalleld() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    assertPrintAction(trace, "action[ABC]");
                }
            }
        });
    }

    @Test
    public void testOrJoinCascadedAndParalleld() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            if (executionLink.getTraces().size() == 3) {
                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(2);
                assertJoinGateway(trace);
            } else {
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[BD]");

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[CE]");

                trace = executionLink.getTraces().get(3);
                assertJoinGateway(trace);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    assertPrintAction(trace, "action[ABD]");
                }

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "action[CE]");
                }
            }
        });
    }

    @Test
    public void testOrJoinAndIfThenAndIfThenElse() {
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
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 3, 0);

            executionLink = executionInstance.getLinks().get(0);

            if (executionLink.getTraces().size() == 3) {
                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertPrintAction(trace, "actionA");
                } else {
                    assertPrintCondition(trace, "conditionA", true);
                }

                trace = executionLink.getTraces().get(2);
                assertJoinGateway(trace);
            } else {
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "condition[BC]", null);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[BC]");

                trace = executionLink.getTraces().get(3);
                assertJoinGateway(trace);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (trace.getType().equals(ElementType.ACTION)) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertPrintCondition(trace, "condition[ABC]", null);
                    }
                }

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "action[BC]");
                }
            }
        });
    }

    @Test
    public void testOrJoinConditionFalse() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 1, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            if (executionLink.getTraces().size() > 1) {
                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", false);
            }
        });
    }

    @Test
    public void testOrJoinLinkException() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 1, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            if (executionLink.getTraces().size() > 1) {
                trace = executionLink.getTraces().get(1);
                assertThrowLinkTerminateAction(trace);
            }
        });
    }

    @Test
    public void testOrJoinWithNotSelectedNodes() {
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
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2, 3, 0);

            executionLink = executionInstance.getLinks().get(0);
            if (executionLink.getTraces().size() == 3) {
                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(2);
                assertJoinGateway(trace);
            } else {
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionB", true);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "actionB");

                trace = executionLink.getTraces().get(3);
                assertJoinGateway(trace);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (trace.getType().equals(ElementType.ACTION)) {
                        if (trace.getName().equals("printAction")) {
                            assertPrintAction(trace, "actionA");
                        } else {
                            assertThrowLinkTerminateAction(trace);
                        }
                    } else {
                        assertPrintCondition(trace, "condition[AB]", null);
                    }
                }

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "actionB");
                }
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            if (executionInstance.getUnreachableLinks().size() > 0) {
                executionLink = executionInstance.getUnreachableLinks().get(0);
                assertExecutionLink(executionLink, 2, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", true);

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "action[AB]");
                }
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testOrJoinNestedInIfThenElseTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", true);

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "action[AB]");
                }
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenElseFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testOrJoinNestedInSelect() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

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
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinNestedInOrJoin() {
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
                "        },\n" +
                "        printAction(content=\"actionF\"){\n" +
                "            printAction(content=\"actionG\")&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 2, 0);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    assertPrintCondition(trace, "condition[AB]", false);
                }
            }
        });
    }

    @Test
    public void testOrJoinExecutionOrder() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(sleepCondition(timeout=1000000000L, output=true))&,\n" +
                "        if(sleepCondition(timeout=1000000000L, output=true))&,\n" +
                "        if(sleepCondition(timeout=1234567L, output=true))&,\n" +
                "        if(sleepCondition(timeout=1000000000L, output=true))&,\n" +
                "        if(sleepCondition(timeout=1000L, output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    } [\n" +
                "        printListener(event=\"before\", content=\"listenerA\"),\n" +
                "        printListener(event=\"success\", content=\"listenerB\")\n" +
                "    ]\n" +
                "}");

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

            assertExecutionInstance(executionInstance, 1, 4, 0);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSleepCondition(trace, 1234567L, true);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);
        }, 10);
    }

    @Test
    public void testOrJoinEnvMerge1() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        setPropertyAction(name=\"a\",value=1)&,\n" +
                "        setPropertyAction(name=\"b\",value=2)&,\n" +
                "        setPropertyAction(name=\"c\",value=3)&,\n" +
                "        setPropertyAction(name=\"d\",value=4)&,\n" +
                "        setPropertyAction(name=\"e\",value=5)&,\n" +
                "        setPropertyAction(name=\"f\",value=6)&\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            assertExecutionInstance(executionInstance, 1, 0, 5, 0);

            executionLink = findLink(executionInstance, 3);
            Assert.assertEquals(1, executionLink.getEnv().size());
        });
    }

    @Test
    public void testOrJoinEnvMerge2() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"b\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d\",value=4)&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;
            Map<String, Object> env;

            executionInstance = promise.get();

            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = findLink(executionInstance, 4);

            env = executionLink.getEnv();
            Assert.assertEquals(2, env.size());
            Assert.assertTrue(env.containsKey("a") && env.containsKey("b")
                    || env.containsKey("c") && env.containsKey("d"));
        });
    }

    @Test
    public void testOrJoinEnvMerge3() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(setPropertyCondition(name=\"a\", value=1, output=false))&,\n" +
                "        if(setPropertyCondition(name=\"b\", value=2, output=false))&,\n" +
                "        if(setPropertyCondition(name=\"c\", value=3, output=false))&,\n" +
                "        if(setPropertyCondition(name=\"d\", value=4, output=false))&,\n" +
                "        if(setPropertyCondition(name=\"e\", value=5, output=true))&,\n" +
                "        if(setPropertyCondition(name=\"f\", value=6, output=false))&,\n" +
                "        if(setPropertyCondition(name=\"g\", value=7, output=false))&\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;
            Map<String, Object> env;

            executionInstance = promise.get();

            assertExecutionInstance(executionInstance, 1, 0, 6, 0);

            executionLink = findLink(executionInstance, 3);

            env = executionLink.getEnv();
            Assert.assertEquals(1, env.size());
            Assert.assertTrue(env.containsKey("e"));
        });
    }

    @Test
    public void testParallelHardAndJoinAndSoftAndJoinAndOrJoin() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    },\n" +
                "    join & {\n" +
                "        printAction(content=\"actionC\")&,\n" +
                "        printAction(content=\"actionD\")&,\n" +
                "        printAction(content=\"actionE\")&\n" +
                "    },\n" +
                "    join | {\n" +
                "        printAction(content=\"actionF\"){\n" +
                "            printAction(content=\"actionG\"){\n" +
                "                printAction(content=\"actionH\"){\n" +
                "                    printAction(content=\"actionI\")&\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

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
            assertJoinGateway(trace);
        });
    }
}
