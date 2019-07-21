package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestIfThenTrace extends TestTraceBase {

    @Test
    public void testSingleIf() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true))\n" +
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
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testParallelSingleIf() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)),\n" +
                "    if(printCondition(content=\"conditionB\", output=true)),\n" +
                "    if(printCondition(content=\"conditionC\", output=false))\n" +
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
                assertExecutionLink(executionLink, 2);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintCondition(trace, "condition[ABC]", null);
            }
        });
    }

    @Test
    public void testSingleIfNestedInAction() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testSingleIfNestedInIfThen() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false))\n" +
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
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", false);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenElseTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false))\n" +
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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", false);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenElseFlase() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        if(printCondition(content=\"conditionB\", output=true))\n" +
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
            assertPrintCondition(trace, "conditionB", true);
        });
    }

    @Test
    public void testSingleIfNestedInHardJoin() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)),\n" +
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
            assertExecutionInstance(executionInstance, 2, 0, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

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
    public void testSingleIfNestedInSoftJoin() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
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
            assertExecutionInstance(executionInstance, 2, 0, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

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
    public void testSingleIfNestedInOrJoin() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)),\n" +
                "        printAction(content=\"actionB\")&\n" +
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
            assertExecutionInstance(executionInstance, 2, 0, 1, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

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
    public void testSingleIfNestedInHardJoinThen() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)),\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
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
            assertExecutionInstance(executionInstance, 2, 0, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinThen() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true))\n" +
                "    } then {\n" +
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
            assertExecutionInstance(executionInstance, 2, 0, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinThen() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)),\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then  {\n" +
                "        printAction(content=\"actionC\")\n" +
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
            assertExecutionInstance(executionInstance, 2, 0, 1, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", false);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinThenThen() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinThenThen() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinThenThen() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then {\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 1, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(2);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSingleIfNestedInSelect() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=${output0})),\n" +
                "        if(printCondition(content=\"conditionB\", output=${output1})),\n" +
                "        if(printCondition(content=\"conditionC\", output=${output2})),\n" +
                "        if(printCondition(content=\"conditionD\", output=${output3})),\n" +
                "        if(printCondition(content=\"conditionE\", output=${output4}))\n" +
                "    }\n" +
                "}");

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

            Promise<ExecutionInstance> promise = startRule(rule, builder.build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            if (firstTrue == -1) {
                assertExecutionInstance(executionInstance, 5, 0, 0);

                for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                    executionLink = executionInstance.getLinks().get(i);
                    assertExecutionLink(executionLink, 3);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "condition[ABCDE]", false);
                }
            } else {
                // finishOperation may be earlier than markOperation
                assertExecutionInstance(executionInstance, firstTrue + 1, 0, 5 - firstTrue - 1, 0);

                for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                    executionLink = executionInstance.getLinks().get(i);
                    assertExecutionLink(executionLink, 3);

                    trace = executionLink.getTraces().get(0);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(1);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "condition[ABCDE]", null);
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
    public void testSingleIfThenTrue() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
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
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testSingleIfThenFalse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
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
    public void testIfThenCascaded() {
        Rule rule = compile("{\n" +
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
                assertPrintCondition(trace, "condition[A-E]", true);
            }

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testIfThenParalled() {
        Rule rule = compile("{\n" +
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
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 5, 0, 0);

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
    public void testIfThenLinkException() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        throwLinkTerminateAction(),\n" +
                "        printAction(content=\"actionA\")\n" +
                "    },\n" +
                "    throwLinkTerminateAction()\n" +
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
    public void testIfThenHardAndJoinAndJoinThen() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionB\")&,\n" +
                "            printAction(content=\"actionC\")&,\n" +
                "            printAction(content=\"actionD\")&\n" +
                "        }\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionB\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&\n" +
                "        } then {\n" +
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

            assertExecutionInstance(executionInstance, 2, 1, 0);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 2; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[BCD]");
            }

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionB", false);
        });
    }

    @Test
    public void testIfThenSelect() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionC\", output=true)){\n" +
                "                printAction(content=\"actionA\")\n" +
                "            },\n" +
                "            if(printCondition(content=\"conditionD\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionE\", output=false)){\n" +
                "        select {\n" +
                "            if(throwExceptionCondition()){\n" +
                "                throwExceptionAction()\n" +
                "            },\n" +
                "            if(throwExceptionCondition()){\n" +
                "                throwExceptionAction()\n" +
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
            assertExecutionInstance(executionInstance, 1, 2, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            executionLink = findUnreachableLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionE", false);

            executionLink = findUnreachableLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);
        });
    }
}
