package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestJoinThenTrace extends TestTraceBase {

    @Test
    public void testHardAndJoinThenSimple() {
        Flow flow = compile("{\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenCascade() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(10);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(12);
            assertPrintCondition(trace, "conditionE", false);

            trace = executionLink.getTraces().get(13);
            assertPrintAction(trace, "actionG");
        });
    }

    @Test
    public void testHardAndJoinThenParallel() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
                "        printAction(content=\"actionD\"),\n" +
                "        printAction(content=\"actionE\"),\n" +
                "        printAction(content=\"actionF\")\n" +
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

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 9);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(7);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(8);
                assertPrintAction(trace, "action[DEF]");
            }
        });
    }

    @Test
    public void testHardAndJoinThenIfThenTrue() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenIfThenFalse() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
                "        if(printCondition(content=\"conditionD\", output=false)){\n" +
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
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintCondition(trace, "conditionD", false);
        });
    }

    @Test
    public void testHardAndJoinThenIfThenElseTrue() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\")\n" +
                "        } else{\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenIfThenElseFalse() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
                "        if(printCondition(content=\"conditionD\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        } else{\n" +
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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintCondition(trace, "conditionD", false);

            trace = executionLink.getTraces().get(9);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testHardAndJoinThenSelect() {
        Flow flow = compile("{\n" +
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
                "    } then {\n" +
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
                "}");

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

            Promise<ExecutionInstance> promise = startFlow(flow, builder.build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            if (trueNum == 0) {
                assertExecutionInstance(executionInstance, 0, 3, 0);

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 10);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(7);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(8);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(9);
                    assertPrintCondition(trace, "condition[DEF]", false);
                }
            } else {
                assertExecutionInstance(executionInstance, 1, 0, 2, 0);

                executionLink = executionInstance.getLinks().get(0);
                assertExecutionLink(executionLink, 11);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(7);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(8);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(9);
                assertPrintCondition(trace, "condition[DEF]", true);

                trace = executionLink.getTraces().get(10);
                assertPrintAction(trace, "action[DEF]");

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 9, 11);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(7);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(8);
                    assertExclusiveGateway(trace);
                }
            }
        });
    }

    @Test
    public void testJoinThenSelect1() {
        Flow flow = compile("{\n" +
                "    join{\n" +
                "        select{\n" +
                "            if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=false))&,\n" +
                "            if(printCondition(content=\"conditionC\", output=false))&,\n" +
                "            if(printCondition(content=\"conditionD\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionE\", output=false))&,\n" +
                "            if(printCondition(content=\"conditionF\", output=false))&,\n" +
                "            if(printCondition(content=\"conditionG\", output=false))&\n" +
                "        }\n" +
                "    } then {\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a.b", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 3, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[A-C]", false);
            }
        });
    }

    @Test
    public void testHardAndJoinThen1() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"a\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"c\",value=5)&\n" +
                "        }\n" +
                "    } then {\n" +
                "        getPropertyAction(name=\"a\",expectedValue=2){\n" +
                "            getPropertyAction(name=\"c\",expectedValue=5)\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);
        });
    }

    @Test
    public void testHardAndJoinThen2() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"test1\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"test2\")&\n" +
                "        }\n" +
                "    } [setPropertyListener(event=\"before\", name=\"a.b.c\", value=\"111\")] then {\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            setPropertyAction(name=\"a.b.c\", value=\"222\") [setPropertyListener(event=\"success\", name=\"a.b.c\", value=\"333\")]\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a.b", Maps.newHashMap())
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
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 3; i++) {
                trace = executionLink.getTraces().get(i);
                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "test[12]");
                } else {
                    assertPrintCondition(trace, "conditionA", true);
                }
            }

            trace = executionLink.getTraces().get(4);
            assertSetPropertyListener(trace, "a.b.c", "111", ListenerEvent.before, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(7);
            assertSetPropertyAction(trace, "a.b.c", "222", PropertyUpdateType.UPDATE, "111");

            trace = executionLink.getTraces().get(8);
            assertSetPropertyListener(trace, "a.b.c", "333", ListenerEvent.success, PropertyUpdateType.UPDATE, "222");
        });
    }

    @Test
    public void testHardAndJoinThen3() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"test1\"){\n" +
                "            printAction(content=\"test2\")&,\n" +
                "            printAction(content=\"test3\")&\n" +
                "        },\n" +
                "        printAction(content=\"test4\")&\n" +
                "    } then {\n" +
                "        printAction(content=\"test5\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a.b", Maps.newHashMap())
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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "test[1234]");
            }

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "test5");
        });
    }

    @Test
    public void testHardAndJoinThen4() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"b.c\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d.e\",value=5)&\n" +
                "        }\n" +
                "    } then {\n" +
                "        getPropertyAction(name=\"a\",expectedValue=1){\n" +
                "            getPropertyAction(name=\"b.c\",expectedValue=2){\n" +
                "                getPropertyAction(name=\"c\",expectedValue=3){\n" +
                "                    getPropertyAction(name=\"d.e\",expectedValue=5)\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("b", Maps.newHashMap())
                    .put("d", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);
        });
    }

    @Test
    public void testHardAndJoinThen5() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"b.c\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"d.e\",value=5)&\n" +
                "        }\n" +
                "    } then {\n" +
                "        getPropertyAction(name=\"a\",expectedValue=1){\n" +
                "            getPropertyAction(name=\"b.c\",expectedValue=2){\n" +
                "                getPropertyAction(name=\"c\",expectedValue=3){\n" +
                "                    getPropertyAction(name=\"d.e\",expectedValue=5)\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        getPropertyAction(name=\"a\",expectedValue=1){\n" +
                "            getPropertyAction(name=\"b.c\",expectedValue=2){\n" +
                "                getPropertyAction(name=\"c\",expectedValue=3){\n" +
                "                    getPropertyAction(name=\"d.e\",expectedValue=5)\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        getPropertyAction(name=\"a\",expectedValue=1){\n" +
                "            getPropertyAction(name=\"b.c\",expectedValue=2){\n" +
                "                getPropertyAction(name=\"c\",expectedValue=3){\n" +
                "                    getPropertyAction(name=\"d.e\",expectedValue=5)\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        getPropertyAction(name=\"a\",expectedValue=1){\n" +
                "            getPropertyAction(name=\"b.c\",expectedValue=2){\n" +
                "                getPropertyAction(name=\"c\",expectedValue=3){\n" +
                "                    getPropertyAction(name=\"d.e\",expectedValue=5)\n" +
                "                }\n" +
                "            }\n" +
                "        }       \n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("d", Maps.newHashMap())
                    .put("b", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);
        });
    }

    @Test
    public void testHardAndJoinThen6() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        setPropertyAction(name=\"a\",value=1){\n" +
                "            setPropertyAction(name=\"a\",value=2)&\n" +
                "        },\n" +
                "        setPropertyAction(name=\"c\",value=3){\n" +
                "            setPropertyAction(name=\"c\",value=5)&\n" +
                "        }\n" +
                "    } then {\n" +
                "        getPropertyAction(name=\"a\",expectedValue=2){\n" +
                "            getPropertyAction(name=\"c\",expectedValue=5)\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);
        });
    }

    @Test
    public void testHardAndJoinThen7() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then {\n" +
                "        printAction(content=\"actionC\")\n" +
                "    }\n" +
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
            assertExecutionInstance(executionInstance, 0, 2, 0);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 1, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                if (executionLink.getTraces().size() > 1) {
                    trace = executionLink.getTraces().get(1);
                    if (ElementType.ACTION.equals(trace.getType())) {
                        assertPrintAction(trace, "actionB");
                    } else {
                        assertPrintCondition(trace, "conditionA", false);
                    }
                }
            }
        });
    }

    @Test
    public void testSoftAndJoinThen1() {
        Flow flow = compile("{\n" +
                "    join{\n" +
                "        if(printCondition(content=\"conditionA\", output=${output1}))&,\n" +
                "        if(printCondition(content=\"conditionB\", output=${output2}))&,\n" +
                "        if(printCondition(content=\"conditionC\", output=${output3}))&,\n" +
                "        if(printCondition(content=\"conditionD\", output=${output4}))&,\n" +
                "        if(printCondition(content=\"conditionE\", output=${output5}))&,\n" +
                "        if(printCondition(content=\"conditionF\", output=${output6}))&,\n" +
                "        if(printCondition(content=\"conditionG\", output=${output7}))&\n" +
                "    }  then {\n" +
                "        printAction(content=\"actionA\")\n" +
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
                assertExecutionLink(executionLink, 3 + trueNum);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                for (int i = 1; i < trueNum + 1; i++) {
                    trace = executionLink.getTraces().get(i);
                    assertPrintCondition(trace, "condition[A-G]", null);
                }

                trace = executionLink.getTraces().get(trueNum + 1);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(trueNum + 2);
                assertPrintAction(trace, "actionA");
            }
        });
    }

    @Test
    public void testLinkTerminator3() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(throwLinkTerminateCondition())&,\n" +
                "        if(throwLinkTerminateCondition())&\n" +
                "    } then {\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
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
            assertExecutionInstance(executionInstance, 0, 3, 0);

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
                        assertThrowLinkTerminateCondition(trace);
                    }
                }
            }
        });
    }

    @Test
    public void testLinkTerminator4() {
        Flow flow = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(throwLinkTerminateCondition())&,\n" +
                "        if(throwLinkTerminateCondition())&\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
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

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertThrowLinkTerminateCondition(trace);
            }
        });
    }

    @Test
    public void testLinkTerminator5() {
        Flow flow = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(throwLinkTerminateCondition())&,\n" +
                "        if(throwLinkTerminateCondition())&\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

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
                        assertThrowLinkTerminateCondition(trace);
                    }
                }
            }
        });
    }
}
