package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestSelectTrace extends TestTraceBase {

    @Test
    public void testSelectCascaded() {
        Flow flow = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\"){\n" +
                "                printAction(content=\"actionB\"){\n" +
                "                    printAction(content=\"actionC\")\n" +
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
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSelectParallel() {
        Flow flow = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\"),\n" +
                "            printAction(content=\"actionB\"),\n" +
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
            assertExecutionInstance(executionInstance, 3, 0, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionA", true);

                trace = executionLink.getTraces().get(3);
                assertPrintAction(trace, "action[ABC]");
            }
        });
    }

    @Test
    public void testSelectFirstTrue() {
        Flow flow = compile("{\n" +
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
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSelectMiddleTrue() {
        Flow flow = compile("{\n" +
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
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

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
    public void testSelectLastTrue() {
        Flow flow = compile("{\n" +
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
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[AB]", false);
            }
        });
    }

    @Test
    public void testSelectAllFalse() {
        Flow flow = compile("{\n" +
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
            assertExecutionInstance(executionInstance, 0, 3, 0);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[ABC]", false);
            }
        });
    }

    @Test
    public void testSelectLinkException() {
        Flow flow = compile("{\n" +
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
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertThrowLinkTerminateCondition(trace);
            }
        });
    }

    @Test
    public void testSelectManyConditions() {
        Flow flow = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=${output1})){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=${output2})){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionC\", output=${output3})){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionD\", output=${output4})){\n" +
                "            printAction(content=\"actionD\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionE\", output=${output5})){\n" +
                "            printAction(content=\"actionE\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionF\", output=${output6})){\n" +
                "            printAction(content=\"actionF\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionG\", output=${output7})){\n" +
                "            printAction(content=\"actionG\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionH\", output=${output8})){\n" +
                "            printAction(content=\"actionH\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionI\", output=${output9})){\n" +
                "            printAction(content=\"actionI\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionJ\", output=${output10})){\n" +
                "            printAction(content=\"actionJ\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            EnvBuilder builder = EnvBuilder.builder();

            int firstTrueIndex = -1;

            for (int i = 1; i <= 10; i++) {
                boolean output = RANDOM.nextBoolean();
                builder.put("output" + i, output);

                if (firstTrueIndex == -1
                        && output) {
                    firstTrueIndex = i - 1;
                }
            }

            Promise<ExecutionInstance> promise = startFlow(flow, builder.build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, firstTrueIndex != -1 ? 1 : 0, firstTrueIndex == -1 ? 10 : firstTrueIndex, 0);

            if (firstTrueIndex != -1) {
                executionLink = executionInstance.getLinks().get(0);
                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[A-J]", true);

                trace = executionLink.getTraces().get(3);
                assertPrintAction(trace, "action[A-J]");
            }

            for (int i = 0; i < firstTrueIndex; i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[A-J]", false);
            }
        });
    }

    @Test
    public void testParallelSelect() {
        Flow flow = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    },\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\"){\n" +
                "                printAction(content=\"actionE\")\n" +
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
            assertExecutionInstance(executionInstance, 2, 2, 0);

            for (ExecutionLink unreachableLink : executionInstance.getUnreachableLinks()) {
                assertExecutionLink(unreachableLink, 3);

                trace = unreachableLink.getTraces().get(0);
                assertStart(trace);

                trace = unreachableLink.getTraces().get(1);
                assertExclusiveGateway(trace);

                trace = unreachableLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[AC]", false);
            }

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionE");
        });
    }
}
