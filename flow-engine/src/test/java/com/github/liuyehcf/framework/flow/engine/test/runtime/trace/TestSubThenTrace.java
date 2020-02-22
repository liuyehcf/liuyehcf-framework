package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.listener.DefaultListener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2019/7/5
 */
@SuppressWarnings("all")
public class TestSubThenTrace extends TestTraceBase {

    @Test
    public void testSubThenSingleAction() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
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

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertFlow(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenCascadeAction() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\"){\n" +
                "                printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertFlow(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testSubThenParallelAction() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\"),\n" +
                "        printAction(content=\"actionC\"),\n" +
                "        printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 3, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 10);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", true);

                trace = executionLink.getTraces().get(3);
                assertPrintCondition(trace, "conditionB", false);

                trace = executionLink.getTraces().get(4);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintCondition(trace, "conditionC", true);

                trace = executionLink.getTraces().get(6);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(7);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(8);
                assertFlow(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintAction(trace, "action[BCD]");
            }
        });
    }

    @Test
    public void testSubThenIf() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionE\", output=true)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionF\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
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
            assertExecutionInstance(executionInstance, 2, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 11);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", true);

                trace = executionLink.getTraces().get(3);
                assertPrintCondition(trace, "conditionB", false);

                trace = executionLink.getTraces().get(4);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintCondition(trace, "conditionC", true);

                trace = executionLink.getTraces().get(6);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(7);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(8);
                assertFlow(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintCondition(trace, "condition[EF]", null);

                trace = executionLink.getTraces().get(10);
                assertPrintAction(trace, "action[BC]");
            }
        });
    }

    @Test
    public void testSubThenJoin() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        join & {\n" +
                "            printAction(content=\"actionB\")&,\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        join {\n" +
                "            printAction(content=\"actionD\")&,\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        } then {\n" +
                "            printAction(content=\"actionF\")\n" +
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
            assertExecutionInstance(executionInstance, 2, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", true);

                trace = executionLink.getTraces().get(3);
                assertPrintCondition(trace, "conditionB", false);

                trace = executionLink.getTraces().get(4);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintCondition(trace, "conditionC", true);

                trace = executionLink.getTraces().get(6);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(7);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(8);
                assertFlow(trace);
            }

            executionLink = findLink(executionInstance, 12);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "action[BC]");

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "action[BC]");

            trace = executionLink.getTraces().get(11);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 13);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "action[DE]");

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "action[DE]");

            trace = executionLink.getTraces().get(11);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(12);
            assertPrintAction(trace, "actionF");
        });
    }

    @Test
    public void testSubThenSelect() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionE\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionF\", output=true)){\n" +
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
            assertExecutionInstance(executionInstance, 1, 2, 0);

            executionLink = findUnreachableLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            executionLink = findUnreachableLink(executionInstance, 11);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertFlow(trace);

            trace = executionLink.getTraces().get(9);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionE", false);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertFlow(trace);

            trace = executionLink.getTraces().get(9);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionF", true);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenWithListeners() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")] ){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    }[printListener(event=\"before\", content=\"listenerC\"), printListener(event=\"success\", content=\"listenerD\")],\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                }[printListener(event=\"before\", content=\"listenerE\"), printListener(event=\"success\", content=\"listenerF\")] then {\n" +
                "                    printAction(content=\"actionA\")[printListener(event=\"before\", content=\"listenerG\"), printListener(event=\"success\", content=\"listenerH\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerI\"), printListener(event=\"success\", content=\"listenerJ\")]then {\n" +
                "        printAction(content=\"actionB\")[printListener(event=\"before\", content=\"listenerK\"), printListener(event=\"success\", content=\"listenerL\")]\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerM\"), printListener(event=\"success\", content=\"listenerN\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 2);

            trace = executionInstance.getTraces().get(0);
            assertPrintListener(trace, "listenerM", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerN", ListenerEvent.success);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerI", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(7);
            assertPrintCondition(trace, "conditionD", false);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerJ", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 22);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerI", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(8);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionLink.getTraces().get(12);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerF", ListenerEvent.success);

            trace = executionLink.getTraces().get(14);
            assertPrintListener(trace, "listenerG", ListenerEvent.before);

            trace = executionLink.getTraces().get(15);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(16);
            assertPrintListener(trace, "listenerH", ListenerEvent.success);

            trace = executionLink.getTraces().get(17);
            assertPrintListener(trace, "listenerJ", ListenerEvent.success);

            trace = executionLink.getTraces().get(18);
            assertFlow(trace);

            trace = executionLink.getTraces().get(19);
            assertPrintListener(trace, "listenerK", ListenerEvent.before);

            trace = executionLink.getTraces().get(20);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(21);
            assertPrintListener(trace, "listenerL", ListenerEvent.success);
        });
    }

    @Test
    public void testSubThenSub() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    },\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    printAction(content=\"actionA\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        sub{\n" +
                "            printAction(content=\"actionB\")\n" +
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

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionD", false);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertFlow(trace);

            trace = executionLink.getTraces().get(9);
            assertStart(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(11);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubThenWithTrueConditionOnly() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    } then {\n" +
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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSubThenWithFalseConditionOnly() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false))\n" +
                "    } then {\n" +
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
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testParallelSubThen() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    },\n" +
                "    sub{\n" +
                "        printAction(content=\"actionC\")\n" +
                "    } then {\n" +
                "        printAction(content=\"actionD\")\n" +
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

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[AC]");

                trace = executionLink.getTraces().get(3);
                assertFlow(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintAction(trace, "action[BD]");
            }
        });
    }

    @Test
    public void testSubThenReachableWithNodeListener() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        Node subFlow = flow.getStart().getSuccessors().get(0);
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerC"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerD"}
        ));

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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(7);
            assertFlow(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenReachableWithMultiNodeListener() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [\n" +
                "        printListener(event=\"before\", content=\"listenerA\"), \n" +
                "        printListener(event=\"before\", content=\"listenerB\"),\n" +
                "        printListener(event=\"success\", content=\"listenerC\"),\n" +
                "        printListener(event=\"success\", content=\"listenerD\")\n" +
                "    ]then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        Node subFlow = flow.getStart().getSuccessors().get(0);
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerE"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerF"}
        ));

        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerG"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerH"}
        ));

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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerF", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.success);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(10);
            assertFlow(trace);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerG", ListenerEvent.success);

            trace = executionLink.getTraces().get(12);
            assertPrintListener(trace, "listenerH", ListenerEvent.success);

            trace = executionLink.getTraces().get(13);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenUnReachableWithNodeListener() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        Node subFlow = flow.getStart().getSuccessors().get(0);
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerC"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerD"}
        ));

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
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertFlow(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);
        });
    }

    @Test
    public void testSubThenUnReachableWithMultiNodeListener() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [\n" +
                "        printListener(event=\"before\", content=\"listenerA\"), \n" +
                "        printListener(event=\"before\", content=\"listenerB\"), \n" +
                "        printListener(event=\"success\", content=\"listenerC\"),\n" +
                "        printListener(event=\"success\", content=\"listenerD\")\n" +
                "    ]then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        Node subFlow = flow.getStart().getSuccessors().get(0);
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerE"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerF"}
        ));

        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerG"}
        ));
        subFlow.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subFlow.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerH"}
        ));

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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerE", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerF", ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.before);

            trace = executionLink.getTraces().get(5);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.success);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(9);
            assertFlow(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerG", ListenerEvent.success);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerH", ListenerEvent.success);
        });
    }

    @Test
    public void testSubThenUnreachable() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    } then {\n" +
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
    public void testSubThenUnKnownException() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        throwExceptionAction()\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testSubThenLinkException() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        throwLinkTerminateAction()\n" +
                "    } then {\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);
        });
    }

    @Test
    public void testSubThenLinkExceptionWithPropertyUpdate1() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        setPropertyAction(name=\"a\", value=1){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        },\n" +
                "        setPropertyAction(name=\"b\", value=2)\n" +
                "    } then{\n" +
                "        getPropertyAction(name=\"b\", expectedValue=2)\n" +
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
            assertSetPropertyAction(trace, "b", 2, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertFlow(trace);

            trace = executionLink.getTraces().get(4);
            assertGetPropertyAction(trace, "b", 2);

            Assert.assertEquals(1, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("b"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateAction(trace);

            Assert.assertEquals(1, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("a"));
            Assert.assertEquals(1, executionLink.getEnv().get("a"));
        });
    }

    @Test
    public void testSubLinkExceptionWithPropertyUpdate2() {
        Flow flow = compile("{\n" +
                "    sub{\n" +
                "        setPropertyAction(name=\"a\", value=1){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        },\n" +
                "        setPropertyAction(name=\"b\", value=2){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertFlow(trace);

            Assert.assertEquals(2, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("a"));
            Assert.assertEquals(1, executionLink.getEnv().get("a"));
            Assert.assertTrue(executionLink.getEnv().containsKey("b"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));
        });
    }
}
