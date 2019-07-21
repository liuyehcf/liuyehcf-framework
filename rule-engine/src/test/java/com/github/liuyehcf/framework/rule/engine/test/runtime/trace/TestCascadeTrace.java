package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

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
public class TestCascadeTrace extends TestTraceBase {

    @Test
    public void testCascadeSingle() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\")\n" +
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
            assertPrintAction(trace, "actionA");
        });
    }

    @Test
    public void testCascadeMultiple() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\"){\n" +
                "                printAction(content=\"actionD\"){\n" +
                "                    printAction(content=\"actionE\")\n" +
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
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            for (int i = 1; i <= 5; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[A-E]");
            }
        });
    }

    @Test
    public void testCascadeIfThenTrue() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=true)){\n" +
                "                printAction(content=\"actionC\"){\n" +
                "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                        printAction(content=\"actionD\")\n" +
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

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testCascadeIfThenFalse() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testCascadeIfThenElseTrue() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=true)){\n" +
                "                printAction(content=\"actionC\"){\n" +
                "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                        printAction(content=\"actionD\")\n" +
                "                    }\n" +
                "                }\n" +
                "            }else{\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testCascadeIfThenElseFalse() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }else{\n" +
                "                printAction(content=\"actionC\"){\n" +
                "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "                        printAction(content=\"actionD\")\n" +
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

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");
        });
    }

    @Test
    public void testCascadeLinkException() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            throwLinkTerminateAction(){\n" +
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
            assertExecutionInstance(executionInstance, 0, 1, 0);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateAction(trace);
        });
    }
}
