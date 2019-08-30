package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

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
 * @date 2019/7/9
 */
@SuppressWarnings("all")
public class TestSubThenElseTrace extends TestTraceBase {

    @Test
    public void testSubThenElseTrueSingleAction() {
        Rule rule = compile("{\n" +
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
                "    } else{\n" +
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
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseFalseSingleAction() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else{\n" +
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
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseTrueCascadeAction() {
        Rule rule = compile("{\n" +
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
                "    } else{\n" +
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
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testSubThenElseFalseCascadeAction() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\"){\n" +
                "                printAction(content=\"actionD\")\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testSubThenElseTrueParallelAction() {
        Rule rule = compile("{\n" +
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
                "    } else{\n" +
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
                assertRule(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintAction(trace, "action[BCD]");
            }
        });
    }

    @Test
    public void testSubThenElseFalseParallelAction() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        printAction(content=\"actionB\"),\n" +
                "        printAction(content=\"actionC\"),\n" +
                "        printAction(content=\"actionD\")\n" +
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

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 5);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(3);
                assertRule(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintAction(trace, "action[BCD]");
            }
        });
    }

    @Test
    public void testSubThenElseTrueIf() {
        Rule rule = compile("{\n" +
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
                "    } else {\n" +
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
                assertRule(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintCondition(trace, "condition[EF]", null);

                trace = executionLink.getTraces().get(10);
                assertPrintAction(trace, "action[BC]");
            }
        });
    }

    @Test
    public void testSubThenElseFalseIf() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else {\n" +
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
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(3);
                assertRule(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintCondition(trace, "condition[EF]", null);

                trace = executionLink.getTraces().get(5);
                assertPrintAction(trace, "action[BC]");
            }
        });
    }

    @Test
    public void testSubThenElseTrueJoin() {
        Rule rule = compile("{\n" +
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
                "    } else {\n" +
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
                assertRule(trace);
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
    public void testSubThenElseFalseJoin() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else {\n" +
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
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", false);

                trace = executionLink.getTraces().get(3);
                assertRule(trace);
            }

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[BC]");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "action[BC]");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[DE]");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "action[DE]");

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionF");
        });
    }

    @Test
    public void testSubThenElseTrueSelect() {
        Rule rule = compile("{\n" +
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
                "    } else{\n" +
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
            assertRule(trace);

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
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionF", true);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseFalseSelect() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else{\n" +
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
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionE", false);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionF", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseTrueWithListeners() {
        Rule rule = compile("{\n" +
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
                "    } else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerM\"), printListener(event=\"success\", content=\"listenerN\")]");

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
            assertRule(trace);

            trace = executionLink.getTraces().get(19);
            assertPrintListener(trace, "listenerK", ListenerEvent.before);

            trace = executionLink.getTraces().get(20);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(21);
            assertPrintListener(trace, "listenerL", ListenerEvent.success);
        });
    }

    @Test
    public void testSubThenElseFalseWithListeners() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")] ){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        printAction(content=\"actionB\")[printListener(event=\"before\", content=\"listenerK\"), printListener(event=\"success\", content=\"listenerL\")]\n" +
                "    }\n" +
                "}[printListener(event=\"before\", content=\"listenerM\"), printListener(event=\"success\", content=\"listenerN\")]");

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
            assertPrintListener(trace, "listenerM", ListenerEvent.before);

            trace = executionInstance.getTraces().get(1);
            assertPrintListener(trace, "listenerN", ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 11);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintListener(trace, "listenerI", ListenerEvent.before);

            trace = executionLink.getTraces().get(2);
            assertStart(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.before);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.success);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerJ", ListenerEvent.success);

            trace = executionLink.getTraces().get(7);
            assertRule(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerK", ListenerEvent.before);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerL", ListenerEvent.success);
        });
    }

    @Test
    public void testSubThenElseTrueSub() {
        Rule rule = compile("{\n" +
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
                "    } else {\n" +
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
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertStart(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(11);
            assertRule(trace);
        });
    }

    @Test
    public void testSubThenElseFalseSub() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
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
                "        throwExceptionAction()\n" +
                "    } else {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertStart(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(6);
            assertRule(trace);
        });
    }

    @Test
    public void testSubThenElseWithTrueConditionOnly() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } else{\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSubThenElseWithFalseConditionOnly() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false))\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } else{\n" +
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
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testParallelSubThenElse() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }else {\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    sub{\n" +
                "        printAction(content=\"actionC\")\n" +
                "    } then {\n" +
                "        printAction(content=\"actionD\")\n" +
                "    } else{\n" +
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
                assertRule(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintAction(trace, "action[BD]");
            }
        });
    }

    @Test
    public void testSubThenElseReachableWithNodeListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    } else {\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        Node subRule = rule.getStart().getSuccessors().get(0);
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerC"}
        ));
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerD"}
        ));

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
            assertRule(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseUnReachableWithNodeListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }\n" +
                "    } [printListener(event=\"before\", content=\"listenerA\"), printListener(event=\"success\", content=\"listenerB\")]then {\n" +
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        Node subRule = rule.getStart().getSuccessors().get(0);
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.before,
                new String[]{"event", "content"},
                new Object[]{"before", "listenerC"}
        ));
        subRule.addListener(new DefaultListener(UUID.randomUUID().toString(),
                subRule.getId(),
                "printListener",
                ListenerScope.node,
                ListenerEvent.success,
                new String[]{"event", "content"},
                new Object[]{"success", "listenerD"}
        ));

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
            assertExecutionLink(executionLink, 9);

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
            assertRule(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.success);

            trace = executionLink.getTraces().get(8);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseUnreachable() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } else{\n" +
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
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseUnKnownException() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        throwExceptionAction()\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testSubThenElseLinkException() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        throwLinkTerminateAction()\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    } else{\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);

            trace = executionLink.getTraces().get(3);
            assertRule(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSubThenElseLinkExceptionWithPropertyUpdate1() {
        Rule rule = compile("{\n" +
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
                "    } else{\n" +
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
            assertRule(trace);

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
    public void testSubLinkElseExceptionWithPropertyUpdate2() {
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
                "    } then {\n" +
                "        throwExceptionAction()\n" +
                "    }else {\n" +
                "        getPropertyAction(name=\"a\", expectedValue=1){\n" +
                "            getPropertyAction(name=\"b\", expectedValue=2)\n" +
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
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertRule(trace);

            trace = executionLink.getTraces().get(7);
            assertGetPropertyAction(trace, "a", 1);

            trace = executionLink.getTraces().get(8);
            assertGetPropertyAction(trace, "b", 2);

            Assert.assertEquals(2, executionLink.getEnv().size());
            Assert.assertTrue(executionLink.getEnv().containsKey("a"));
            Assert.assertEquals(1, executionLink.getEnv().get("a"));
            Assert.assertTrue(executionLink.getEnv().containsKey("b"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));
        });
    }
}
