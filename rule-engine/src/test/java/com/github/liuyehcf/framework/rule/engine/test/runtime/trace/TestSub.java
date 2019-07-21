package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.DefaultListener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
@SuppressWarnings("all")
public class TestSub extends TestTraceBase {

    @Test
    public void testSubSingleWithSingleAction() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertRule(trace);
        });
    }

    @Test
    public void testSubSingleWithCascadeAction() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\"){\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertRule(trace);
        });
    }

    @Test
    public void testSubSingleWithParallelAction() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\"),\n" +
                "        printAction(content=\"actionB\"),\n" +
                "        printAction(content=\"actionC\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(5);
            assertRule(trace);
        });
    }

    @Test
    public void testSubSingleWithIf() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i < 6; i++) {
                trace = executionLink.getTraces().get(i);

                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "action[AB]");
                } else {
                    assertPrintCondition(trace, "condition[AB]", null);
                }
            }

            trace = executionLink.getTraces().get(6);
            assertRule(trace);
        });
    }

    @Test
    public void testSubSingleWithJoin() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        },\n" +
                "        join {\n" +
                "            printAction(content=\"actionC\")&,\n" +
                "            printAction(content=\"actionD\")&\n" +
                "        } then {\n" +
                "            printAction(content=\"actionD\")\n" +
                "        }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i < 9; i++) {
                trace = executionLink.getTraces().get(i);

                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "action[ABCDE]");
                } else {
                    assertJoinGateway(trace);
                }
            }

            trace = executionLink.getTraces().get(9);
            assertRule(trace);
        });
    }

    @Test
    public void testSubSingleWithSelect() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                printAction(content=\"actionA\")\n" +
                "            }\n" +
                "        }\n" +
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
            assertExecutionInstance(executionInstance, 1, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertRule(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubSingleWithSub() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        sub{\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertRule(trace);

            trace = executionLink.getTraces().get(5);
            assertRule(trace);
        });
    }

    @Test
    public void testSubWithListeners() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] ){\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                "                    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")],\n" +
                "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                        throwExceptionAction()&\n" +
                "                    }\n" +
                "                }[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")] then {\n" +
                "                    printAction(content=\"actionA\")[printListener(event=\"start\", content=\"listenerG\"), printListener(event=\"end\", content=\"listenerH\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } [printListener(event=\"start\", content=\"listenerI\"), printListener(event=\"end\", content=\"listenerJ\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerM\"), printListener(event=\"end\", content=\"listenerN\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerM", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerI", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(8);
            assertPrintCondition(trace, "conditionD", false);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 21);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerM", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerI", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(12);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(13);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(14);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            trace = executionLink.getTraces().get(15);
            assertPrintListener(trace, "listenerG", ListenerEvent.start);

            trace = executionLink.getTraces().get(16);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(17);
            assertPrintListener(trace, "listenerH", ListenerEvent.end);

            trace = executionLink.getTraces().get(18);
            assertPrintListener(trace, "listenerJ", ListenerEvent.end);

            trace = executionLink.getTraces().get(19);
            assertRule(trace);

            trace = executionLink.getTraces().get(20);
            assertPrintListener(trace, "listenerN", ListenerEvent.end);
        });
    }

    @Test
    public void testSubNestedInAction() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        sub{\n" +
                "            printAction(content=\"actionB\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertRule(trace);
        });
    }

    @Test
    public void testSubNestedInIfThenTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertRule(trace);
        });
    }

    @Test
    public void testSubNestedInIfThenFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
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
            assertExecutionInstance(executionInstance, 0, 1);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubNestedInIfThenElseTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertRule(trace);
        });
    }

    @Test
    public void testSubNestedInIfThenElseFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        sub {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

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
    public void testSubNestedInSelect() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            sub {\n" +
                "                printAction(content=\"actionA\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertRule(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubNestedInJoin() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

        });
    }

    @Test
    public void testSubNestedInJoinThen() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }&\n" +
                "    } then {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionB\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(7);
            assertRule(trace);
        });
    }

    @Test
    public void testSubWithTrueConditionOnly() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);
        });
    }

    @Test
    public void testSubWithFalseConditionOnly() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false))\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);
        });
    }

    @Test
    public void testParallelSub() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    },\n" +
                "    sub{\n" +
                "        printAction(content=\"actionB\")\n" +
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
            assertExecutionInstance(executionInstance, 2, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[AB]");

                trace = executionLink.getTraces().get(3);
                assertRule(trace);
            }
        });
    }

    @Test
    public void testSubReachableWithNodeListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "}");

        Node subRule = rule.getStart().getSuccessors().get(0);
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.NODE,
                ListenerEvent.start,
                new String[]{"event", "content"},
                new Object[]{"start", "listenerC"}
        ));
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.NODE,
                ListenerEvent.end,
                new String[]{"event", "content"},
                new Object[]{"end", "listenerD"}
        ));

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertRule(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubUnReachableWithNodeListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "}");

        Node subRule = rule.getStart().getSuccessors().get(0);
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.NODE,
                ListenerEvent.start,
                new String[]{"event", "content"},
                new Object[]{"start", "listenerC"}
        ));
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.NODE,
                ListenerEvent.end,
                new String[]{"event", "content"},
                new Object[]{"end", "listenerD"}
        ));

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertRule(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubUnKnownException() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        throwExceptionAction()\n" +
                "    } \n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testSubLinkException() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        throwLinkTerminateAction()\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);
        });
    }

    @Test
    public void testSubLinkExceptionWithPropertyUpdate1() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        setPropertyAction(name=\"a\", value=1){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction(),\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        },\n" +
                "        setPropertyAction(name=\"b\", value=2)\n" +
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
            assertExecutionInstance(executionInstance, 1, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "b", 2, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

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
        Rule rule = compile("{\n" +
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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertRule(trace);

            Assert.assertEquals(2, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("a"));
            Assert.assertEquals(1, executionLink.getEnv().get("a"));
            Assert.assertTrue(executionLink.getEnv().containsKey("b"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));
        });
    }
}
