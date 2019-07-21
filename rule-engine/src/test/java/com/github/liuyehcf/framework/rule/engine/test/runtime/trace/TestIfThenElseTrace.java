package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestIfThenElseTrace extends TestTraceBase {

    @Test
    public void testSingleIfThenElse() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=${output1})){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            boolean output1 = RANDOM.nextBoolean();
            Map<String, Object> env = EnvBuilder.builder()
                    .put("output1", output1)
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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionA", output1);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");
        });
    }

    @Test
    public void testIfThenElseCascaded() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }else{\n" +
                "            if(printCondition(content=\"conditionC\", output=true)){\n" +
                "                printAction(content=\"actionA\")\n" +
                "            }else{\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
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

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testIfThenElseLinkException() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\"),\n" +
                "        throwLinkTerminateAction()\n" +
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
            assertPrintCondition(trace, "conditionA", false);

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
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testIfThenElseHardAndJoinAndJoinThen() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionB\")&,\n" +
                "            printAction(content=\"actionC\")&,\n" +
                "            printAction(content=\"actionD\")&\n" +
                "        }\n" +
                "    }else{\n" +
                "        join & {\n" +
                "            throwExceptionAction()&\n" +
                "        }\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionB\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&\n" +
                "        } then {\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }else{\n" +
                "        join & {\n" +
                "            printAction(content=\"actionE\")&,\n" +
                "            printAction(content=\"actionF\")&,\n" +
                "            printAction(content=\"actionG\")&,\n" +
                "            printAction(content=\"actionH\")&\n" +
                "        } then {\n" +
                "            printAction(content=\"actionI\")\n" +
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

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionB", false);

            for (int i = 2; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[EFGH]");
            }

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionI");
        });
    }

    @Test
    public void testIfThenElseSelect() {
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
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionE\", output=true)){\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }else{\n" +
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
            assertExecutionInstance(executionInstance, 2, 1, 0);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintCondition(trace, "conditionE", true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            executionLink = findLink(executionInstance, 5);

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

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

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
