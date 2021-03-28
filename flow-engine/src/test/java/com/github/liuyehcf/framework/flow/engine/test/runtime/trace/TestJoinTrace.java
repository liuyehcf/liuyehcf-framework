package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
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
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
            assertExecutionInstance(executionInstance, 0, 2, 0);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (ElementType.ACTION.equals(trace.getType())) {
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
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
                if (ElementType.ACTION.equals(trace.getType())) {
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertPrintCondition(trace, "conditionA", false);
                }
            }
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenTrue() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
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
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testHardAndJoinNestedInSelect() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
    public void testHardAndJoinActionWithJoinMarkAndNormalSuccessors() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")& {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinActionWithJoinMarkAndJoinSuccessors() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")& {\n" +
                "            printAction(content=\"actionB\")& {\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSingleIfWithTrueJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSingleIfWithTrueJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&\n" +
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
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testHardAndJoinSingleIfWithFalseJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~&\n" +
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
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testHardAndJoinSingleIfWithFalseJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinIfThenWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testHardAndJoinIfThenWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
    public void testHardAndJoinIfThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinIfThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
    public void testHardAndJoinIfThenWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testHardAndJoinIfThenWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinIfThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 2, 0);
        });
    }

    @Test
    public void testHardAndJoinIfThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
    public void testHardAndJoinIfThenElseWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(2);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 2, 0);
        });
    }

    @Test
    public void testHardAndJoinIfThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testHardAndJoinSubWithTrueJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSubWithTrueJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }&\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testHardAndJoinSubWithFalseJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~&\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testHardAndJoinSubWithFalseJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinSubThenWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinSubThenWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 2, 0);
        });
    }

    @Test
    public void testHardAndJoinSubThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                assertJoinGateway(trace);
            } else {
                assertPrintAction(trace, "actionD");
            }
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 2, 0);
        });
    }

    @Test
    public void testHardAndJoinSubThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSoftAndJoinSingle() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
                if (ElementType.ACTION.equals(trace.getType())) {
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertPrintCondition(trace, "conditionA", false);
                }
            }
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenTrue() {
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
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
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenAndIfThenElse() {
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, builder.build());

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
    public void testSoftAndJoinActionWithJoinMarkAndNormalSuccessors() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")& {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinActionWithJoinMarkAndJoinSuccessors() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")& {\n" +
                "            printAction(content=\"actionB\")& {\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSingleIfWithTrueJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSingleIfWithTrueJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&\n" +
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
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSoftAndJoinSingleIfWithFalseJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~&\n" +
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
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testSoftAndJoinSingleIfWithFalseJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSoftAndJoinIfThenWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
    public void testSoftAndJoinIfThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
    public void testSoftAndJoinIfThenWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSoftAndJoinIfThenWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(2);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinIfThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(2);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testSoftAndJoinSubWithTrueJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubWithTrueJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }&\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubWithFalseJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~&\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubWithFalseJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                assertJoinGateway(trace);
            } else {
                assertPrintAction(trace, "actionD");
            }
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testSoftAndJoinSubThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(3);
                assertFlow(trace);

                trace = executionLink.getTraces().get(4);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionD");
                }
            }
        });
    }

    @Test
    public void testOrJoinSingle() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
                if (ElementType.ACTION.equals(trace.getType())) {
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
                    if (ElementType.ACTION.equals(trace.getType())) {
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
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
                    if (ElementType.ACTION.equals(trace.getType())) {
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
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
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
        Flow flow = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testOrJoinNestedInSelect() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
    public void testOrJoinJoinActionWithJoinMarkAndNormalSuccessors() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")& {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinActionWithJoinMarkAndJoinSuccessors() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")& {\n" +
                "            printAction(content=\"actionB\")& {\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            for (int i = 2; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "action[BC]");
                }
            }
        });
    }

    @Test
    public void testOrJoinSingleIfWithTrueJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinSingleIfWithTrueJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&\n" +
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
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testOrJoinSingleIfWithFalseJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~&\n" +
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
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testOrJoinSingleIfWithFalseJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinIfThenWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testOrJoinIfThenWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
    public void testOrJoinIfThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "action[AB]");
                }
            }
        });
    }

    @Test
    public void testOrJoinIfThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
    public void testOrJoinIfThenWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testOrJoinIfThenWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinIfThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            for (int i = 3; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionB");
                }
            }
        });
    }

    @Test
    public void testOrJoinIfThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinIfThenElseWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testOrJoinIfThenElseWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinIfThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "action[AB]");
                }
            }
        });
    }

    @Test
    public void testOrJoinIfThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinIfThenElseWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testOrJoinIfThenElseWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\") {\n" +
                "                printAction(content=\"actionB\")\n" +
                "            }\n" +
                "        } else {\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(2);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testOrJoinIfThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=true))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            for (int i = 3; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionB");
                }
            }
        });
    }

    @Test
    public void testOrJoinIfThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))~& {\n" +
                "            printAction(content=\"actionA\")& {\n" +
                "                printAction(content=\"actionB\")&\n" +
                "            }\n" +
                "        } else {\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(2);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testOrJoinSubWithTrueJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinSubWithTrueJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }&\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testOrJoinSubWithFalseJoinMarkOutputTrue() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~&\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testOrJoinSubWithFalseJoinMarkOutputFalse() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinSubThenWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinSubThenWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testOrJoinSubThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            for (int i = 4; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "action[BC]");
                }
            }
        });
    }

    @Test
    public void testOrJoinSubThenWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testOrJoinSubThenWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinSubThenWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinSubThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            for (int i = 5; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionC");
                }
            }
        });
    }

    @Test
    public void testOrJoinSubThenWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);
        });
    }

    @Test
    public void testOrJoinSubThenElseWithTrueJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinSubThenElseWithTrueJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testOrJoinSubThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            for (int i = 4; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "action[BC]");
                }
            }
        });
    }

    @Test
    public void testOrJoinSubThenElseWithTrueJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);

            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testOrJoinSubThenElseWithFalseJoinMarkAndNormalSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testOrJoinSubThenElseWithFalseJoinMarkAndNormalSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                assertJoinGateway(trace);
            } else {
                assertPrintAction(trace, "actionD");
            }
        });
    }

    @Test
    public void testOrJoinSubThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputTrue() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            for (int i = 4; i < executionLink.getTraces().size(); i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "action[BC]");
                }
            }
        });
    }

    @Test
    public void testOrJoinSubThenElseWithFalseJoinMarkAndJoinMarkSuccessorsOutputFalse() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        sub {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)) {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            } \n" +
                "        }~& then {\n" +
                "            printAction(content=\"actionB\")&{\n" +
                "                printAction(content=\"actionC\")&\n" +
                "            }\n" +
                "        } else {\n" +
                "            printAction(content=\"actionD\")\n" +
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

            for (int i = 0; i < 2; i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(3);
                assertFlow(trace);

                trace = executionLink.getTraces().get(4);
                if (ElementType.JOIN_GATEWAY.equals(trace.getType())) {
                    assertJoinGateway(trace);
                } else {
                    assertPrintAction(trace, "actionD");
                }
            }
        });
    }

    @Test
    public void testParallelHardAndJoinAndSoftAndJoinAndOrJoin() {
        Flow flow = compile("{\n" +
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
            Promise<ExecutionInstance> promise = startFlow(flow, null);

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
