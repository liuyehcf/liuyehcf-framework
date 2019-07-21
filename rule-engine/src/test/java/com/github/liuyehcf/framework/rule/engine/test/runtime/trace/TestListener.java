package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import org.junit.Test;

import java.util.Map;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestListener extends TestTraceBase {

    @Test
    public void testActionWithStartExceptionListener() {
        Rule rule = compile("{\n" +
                "    throwExceptionAction()[throwExceptionListener(event=\"start\")]{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithStartLinkTerminationListener() {
        Rule rule = compile("{\n" +
                "    throwExceptionAction()[throwLinkTerminateListener(event=\"start\")]{\n" +
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
            assertExecutionInstance(executionInstance, 0, 1);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertThrowLinkTerminateListener(trace, ListenerEvent.start);
        });
    }

    @Test
    public void testActionWithEndLinkTerminationListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\")[throwLinkTerminateListener(event=\"end\")]{\n" +
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
            assertExecutionInstance(executionInstance, 0, 1);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateListener(trace, ListenerEvent.end);
        });
    }

    @Test
    public void testActionWithGlobalStartLinkTerminationListener() {
        Rule rule = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwLinkTerminateListener(event=\"start\")]");

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
            assertExecutionLink(executionLink, 1);

            trace = executionLink.getTraces().get(0);
            assertThrowLinkTerminateListener(trace, ListenerEvent.start);
        });
    }

    @Test
    public void testActionWithGlobalEndLinkTerminationListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[throwLinkTerminateListener(event=\"end\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertStart(trace);

                trace = executionLink.getTraces().get(1);
                assertPrintAction(trace, "action[ABC]");

                trace = executionLink.getTraces().get(2);
                assertThrowLinkTerminateListener(trace, ListenerEvent.end);
            }
        });
    }

    @Test
    public void testActionWithGlobalStartExceptionListener() {
        Rule rule = compile("{\n" +
                "    throwExceptionAction()\n" +
                "}[throwExceptionListener(event=\"start\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testActionWithGlobalEndExceptionListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "\tprintAction(content=\"actionB\"),\n" +
                "\tprintAction(content=\"actionC\")\n" +
                "}[throwExceptionListener(event=\"end\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, false, true);
        });
    }

    @Test
    public void testCascadeMultipleWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i <= 6; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[A-E]");
            }

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testCascadeIfThenTrueWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testCascadeIfThenFalseWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testCascadeIfThenElseTrueWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testCascadeIfThenElseFalseWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(6);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testCascadeLinkExceptionWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            throwLinkTerminateAction(){\n" +
                "                throwExceptionAction()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testParallelMultipleWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\")[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerF\")],\n" +
                "    printAction(content=\"actionB\")[printListener(event=\"start\", content=\"listenerB\"), printListener(event=\"end\", content=\"listenerG\")],\n" +
                "    printAction(content=\"actionC\")[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerH\")],\n" +
                "    printAction(content=\"actionD\")[printListener(event=\"start\", content=\"listenerD\"), printListener(event=\"end\", content=\"listenerI\")],\n" +
                "    printAction(content=\"actionE\")[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerJ\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerK\"), printListener(event=\"end\", content=\"listenerL\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 5, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerK", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listener[A-E]", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertPrintAction(trace, "action[A-E]");

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listener[F-J]", ListenerEvent.end);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerL", ListenerEvent.end);
            }
        });
    }

    @Test
    public void testParallelWithCascadeWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\"),\n" +
                "            printAction(content=\"actionD\"){\n" +
                "                printAction(content=\"actionE\")\n" +
                "            }\n" +
                "        },\n" +
                "        printAction(content=\"actionF\")\n" +
                "    },\n" +
                "    printAction(content=\"actionG\")\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 4, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findLink(executionInstance, 7);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testParallelIfThenTrueWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testParallelLinkExceptionWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    throwLinkTerminateAction(),\n" +
                "    throwLinkTerminateAction(),\n" +
                "    throwLinkTerminateAction(),\n" +
                "    printAction(content=\"actionB\"),\n" +
                "    throwLinkTerminateAction()\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 4);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                assertExecutionLink(executionLink, 4);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[AB]");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertThrowLinkTerminateAction(trace);
            }
        });
    }

    @Test
    public void testParallelIfThenElseTrueWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testParallelIfThenElseFalseWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"),\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testParallelIfThenAndIfThenElseLinkExceptionWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    throwLinkTerminateAction(),\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        throwLinkTerminateAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionB\", output=true)){\n" +
                "        throwLinkTerminateAction()\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(printCondition(content=\"conditionC\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        throwLinkTerminateAction()\n" +
                "    },\n" +
                "    if(throwLinkTerminateCondition()){\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    if(throwLinkTerminateCondition()){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    },\n" +
                "    printAction(content=\"actionA\")\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionInstance(executionInstance, 1, 6);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                if (executionLink.getTraces().size() == 4) {
                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "condition[A-C]", null);

                    trace = executionLink.getTraces().get(3);
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertExecutionLink(executionLink, 3);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    if (trace.getType().equals(ElementType.ACTION)) {
                        assertThrowLinkTerminateAction(trace);
                    } else {
                        assertThrowLinkTerminateCondition(trace);
                    }
                }
            }
        });
    }

    @Test
    public void testSingleIfWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testParallelSingleIfWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]),\n" +
                "    if(printCondition(content=\"conditionB\", output=true)[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]),\n" +
                "    if(printCondition(content=\"conditionC\", output=false)[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")])\n" +
                "}[printListener(event=\"start\", content=\"listenerG\"), printListener(event=\"end\", content=\"listenerH\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerG", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listener[ACE]", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertPrintCondition(trace, "condition[ABC]", null);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listener[BDF]", ListenerEvent.end);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerH", ListenerEvent.end);
            }
        });
    }

    @Test
    public void testSingleIfNestedInActionWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenElseTrueWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        if(printCondition(content=\"conditionB\", output=false)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    } else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInIfThenElseFlaseWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        if(printCondition(content=\"conditionB\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 1);

            executionLink = findLink(executionInstance, 6);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinThenWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                if (ElementType.LISTENER.equals(trace.getType())) {
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(3);
                    assertPrintCondition(trace, "conditionA", true);

                    trace = executionLink.getTraces().get(4);
                    assertPrintListener(trace, "listenerB", ListenerEvent.end);

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);
                } else {
                    assertPrintAction(trace, "actionA");

                    trace = executionLink.getTraces().get(3);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(4);
                    assertPrintAction(trace, "actionB");

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);
                }
            }
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinThenWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    } then {\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                if (ElementType.LISTENER.equals(trace.getType())) {
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(3);
                    assertPrintCondition(trace, "conditionA", true);

                    trace = executionLink.getTraces().get(4);
                    assertPrintListener(trace, "listenerB", ListenerEvent.end);

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);
                } else {
                    assertPrintAction(trace, "actionA");

                    trace = executionLink.getTraces().get(3);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(4);
                    assertPrintAction(trace, "actionB");

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);
                }
            }
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinThenWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]),\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then  {\n" +
                "        printAction(content=\"actionC\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 1);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                if (ElementType.LISTENER.equals(trace.getType())) {
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(3);
                    assertPrintCondition(trace, "conditionA", false);

                    trace = executionLink.getTraces().get(4);
                    assertPrintListener(trace, "listenerB", ListenerEvent.end);

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);
                } else {
                    assertPrintAction(trace, "action[AB]");

                    trace = executionLink.getTraces().get(3);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(4);
                    assertPrintAction(trace, "actionC");

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);
                }
            }
        });
    }

    @Test
    public void testSingleIfNestedInHardJoinThenThen() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInSoftJoinThenThen() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInOrJoinThenThen() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    } then {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfNestedInSelectWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=${output0})[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]),\n" +
                "        if(printCondition(content=\"conditionB\", output=${output1})[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]),\n" +
                "        if(printCondition(content=\"conditionC\", output=${output2})[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]),\n" +
                "        if(printCondition(content=\"conditionD\", output=${output3})[printListener(event=\"start\", content=\"listenerG\"), printListener(event=\"end\", content=\"listenerH\")]),\n" +
                "        if(printCondition(content=\"conditionE\", output=${output4})[printListener(event=\"start\", content=\"listenerI\"), printListener(event=\"end\", content=\"listenerJ\")])\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerK\"), printListener(event=\"end\", content=\"listenerL\")]");

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
                assertExecutionInstance(executionInstance, 5, 0);

                for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                    executionLink = executionInstance.getLinks().get(i);
                    assertExecutionLink(executionLink, 7);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerK", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertPrintListener(trace, "listener[ACEGI]", ListenerEvent.start);

                    trace = executionLink.getTraces().get(4);
                    assertPrintCondition(trace, "condition[ABCDE]", false);

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listener[BDFHJ]", ListenerEvent.end);

                    trace = executionLink.getTraces().get(6);
                    assertPrintListener(trace, "listenerL", ListenerEvent.end);
                }
            } else {
                // finishOperation may be earlier than markOperation
                assertExecutionInstance(executionInstance, firstTrue + 1, 0, 5 - firstTrue - 1);

                for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                    executionLink = executionInstance.getLinks().get(i);
                    assertExecutionLink(executionLink, 7);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerK", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(3);
                    assertPrintListener(trace, "listener[ACEGI]", ListenerEvent.start);

                    trace = executionLink.getTraces().get(4);
                    assertPrintCondition(trace, "condition[ABCDE]", null);

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listener[BDFHJ]", ListenerEvent.end);

                    trace = executionLink.getTraces().get(6);
                    assertPrintListener(trace, "listenerL", ListenerEvent.end);
                }

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 3);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerK", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertExclusiveGateway(trace);
                }
            }
        });
    }

    @Test
    public void testSingleIfThenTrueWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSingleIfThenFalseWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testIfThenCascadedWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i <= 6; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintCondition(trace, "condition[A-E]", true);
            }

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testIfThenParalledWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 5, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);

                if (executionLink.getTraces().size() == 4) {
                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintAction(trace, "actionE");

                    trace = executionLink.getTraces().get(3);
                    assertPrintListener(trace, "listenerB", ListenerEvent.end);
                } else if (executionLink.getTraces().size() == 5) {
                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "condition[AC]", true);

                    trace = executionLink.getTraces().get(3);
                    assertPrintAction(trace, "action[BD]");

                    trace = executionLink.getTraces().get(4);
                    assertPrintListener(trace, "listenerB", ListenerEvent.end);
                } else {
                    assertExecutionLink(executionLink, 6);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(2);
                    assertPrintCondition(trace, "condition[AC]", true);

                    trace = executionLink.getTraces().get(3);
                    assertPrintCondition(trace, "condition[BD]", true);

                    trace = executionLink.getTraces().get(4);
                    assertPrintAction(trace, "action[AC]");

                    trace = executionLink.getTraces().get(5);
                    assertPrintListener(trace, "listenerB", ListenerEvent.end);
                }
            }
        });
    }

    @Test
    public void testIfThenLinkExceptionWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        throwLinkTerminateAction(),\n" +
                "        printAction(content=\"actionA\")\n" +
                "    },\n" +
                "    throwLinkTerminateAction()\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findUnreachableLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);

            executionLink = findUnreachableLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testSingleIfThenElseWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=${output1})){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }else{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", output1);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testIfThenElseCascadedWithGlobalListener() {
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", false);

            trace = executionLink.getTraces().get(4);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testIfThenElseLinkExceptionWithGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        throwExceptionAction()\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\"),\n" +
                "        throwLinkTerminateAction()\n" +
                "    },\n" +
                "    throwLinkTerminateAction()\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = findUnreachableLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);

            executionLink = findUnreachableLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testHardAndJoinSingleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinSingleParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABC]");
            }

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinCascadedAndParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 11);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i <= 6; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABCDE]");
            }

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinAndIfThenAndIfThenElseWithGatewayAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinConditionFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 2);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 3);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    if (Objects.equals(ElementType.ACTION, trace.getType())) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertPrintCondition(trace, "conditionA", false);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 2);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 3);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                if (executionLink.getTraces().size() > 2) {
                    trace = executionLink.getTraces().get(2);
                    if (Objects.equals("printAction", trace.getName())) {
                        assertPrintAction(trace, "actionA");
                    } else {
                        assertThrowLinkTerminateAction(trace);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinWithNotSelectedNodesWithGatewayAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertPrintCondition(trace, "conditionA", false);
                }
            }
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 3; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[AB]");
            }

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join & {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinNestedInIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join & {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinNestedInSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testHardAndJoinNestedInHardAndJoinWithGatewayAndGlobalListener() {
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
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")],\n" +
                "        printAction(content=\"actionG\")&,\n" +
                "        printAction(content=\"actionH\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 4, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinSingleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinSingleParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABC]");
            }

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinCascadedAndParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 11);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            for (int i = 2; i <= 6; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[ABCDE]");
            }

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(8);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinAndIfThenAndIfThenElseWithGatewayAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinConditionFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSoftAndJoinLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertThrowLinkTerminateAction(trace);
        });
    }

    @Test
    public void testSoftAndJoinWithNotSelectedNodesWithGatewayAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertThrowLinkTerminateAction(trace);
                } else {
                    assertPrintCondition(trace, "conditionA", false);
                }
            }
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            for (int i = 3; i <= 4; i++) {
                trace = executionLink.getTraces().get(i);
                assertPrintAction(trace, "action[AB]");
            }

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinNestedInIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinNestedInSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSoftAndJoinNestedInSoftAndJoinWithGatewayAndGlobalListener() {
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
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")],\n" +
                "        printAction(content=\"actionG\")&,\n" +
                "        printAction(content=\"actionH\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 4, 0);

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[DF]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[AGH]");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);
        });
    }

    @Test
    public void testOrJoinSingleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testOrJoinSingleParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&,\n" +
                "        printAction(content=\"actionC\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 4);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinCascadedAndParalleldWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\"){\n" +
                "            printAction(content=\"actionC\")&\n" +
                "        },\n" +
                "        printAction(content=\"actionD\"){\n" +
                "            printAction(content=\"actionE\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            executionLink = executionInstance.getLinks().get(0);
            if (executionLink.getTraces().size() == 7) {
                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(4);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(6);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            } else {
                assertExecutionLink(executionLink, 8);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "action[BD]");

                trace = executionLink.getTraces().get(3);
                assertPrintAction(trace, "action[CE]");

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(5);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(6);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 5);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinAndIfThenAndIfThenElseWithGatewayAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 3);

            executionLink = executionInstance.getLinks().get(0);

            if (executionLink.getTraces().size() == 7) {
                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                if (trace.getType().equals(ElementType.ACTION)) {
                    assertPrintAction(trace, "actionA");
                } else {
                    assertPrintCondition(trace, "conditionA", true);
                }

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(4);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(6);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            } else {
                assertExecutionLink(executionLink, 8);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "condition[BC]", null);

                trace = executionLink.getTraces().get(3);
                assertPrintAction(trace, "action[BC]");

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(5);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(6);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 5);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinConditionFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        if(printCondition(content=\"conditionA\", output=false))&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);
        });
    }

    @Test
    public void testOrJoinLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        throwLinkTerminateAction()&,\n" +
                "        printAction(content=\"actionA\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 7);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 2, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);
        });
    }

    @Test
    public void testOrJoinWithNotSelectedNodesWithGatewayAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2, 3);

            executionLink = executionInstance.getLinks().get(0);
            if (executionLink.getTraces().size() == 7) {
                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintAction(trace, "actionA");

                trace = executionLink.getTraces().get(3);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(4);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(6);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            } else {
                assertExecutionLink(executionLink, 8);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintCondition(trace, "conditionB", true);

                trace = executionLink.getTraces().get(3);
                assertPrintAction(trace, "actionB");

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(5);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(6);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            }

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 5);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionB\")&\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 3, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);
        });
    }

    @Test
    public void testOrJoinNestedInIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testOrJoinNestedInIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        join | {\n" +
                "            printAction(content=\"actionA\")&,\n" +
                "            printAction(content=\"actionA\")&\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 3, 5);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);
            }
        });
    }

    @Test
    public void testOrJoinNestedInIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        join | {\n" +
                "            throwExceptionAction()&,\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testOrJoinNestedInSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join | {\n" +
                "        select {\n" +
                "            if(printCondition(content=\"conditionA\", output=true))&,\n" +
                "            if(printCondition(content=\"conditionB\", output=true))&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testOrJoinNestedInOrJoinWithGatewayAndGlobalListener() {
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
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")],\n" +
                "        printAction(content=\"actionF\"){\n" +
                "            printAction(content=\"actionG\")&\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0, 2);

            executionLink = findLink(executionInstance, 5);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 2, 4);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerE", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                if (executionLink.getTraces().size() == 3) {
                    trace = executionLink.getTraces().get(2);

                    if (ElementType.CONDITION.equals(trace.getType())) {
                        assertPrintCondition(trace, "condition[AB]", false);
                    } else {
                        assertPrintListener(trace, "listenerF", ListenerEvent.end);
                    }
                } else if (executionLink.getTraces().size() == 4) {
                    // condition may be added when global listener is trigger
                    trace = executionLink.getTraces().get(2);
                    if (ElementType.CONDITION.equals(trace.getType())) {
                        assertPrintCondition(trace, "condition[AB]", false);
                    } else {
                        assertPrintListener(trace, "listenerF", ListenerEvent.end);
                    }

                    trace = executionLink.getTraces().get(3);
                    if (ElementType.CONDITION.equals(trace.getType())) {
                        assertPrintCondition(trace, "condition[AB]", false);
                    } else {
                        assertPrintListener(trace, "listenerF", ListenerEvent.end);
                    }
                }
            }
        });
    }

    @Test
    public void testHardAndJoinThenSimpleWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] then {\n" +
                "        printAction(content=\"actionD\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 13);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(12);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinThenCascadeWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    } [printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] then {\n" +
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 18);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(12);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(13);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(14);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(15);
            assertPrintCondition(trace, "conditionE", false);

            trace = executionLink.getTraces().get(16);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(17);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinThenParallelWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] then {\n" +
                "        printAction(content=\"actionD\"),\n" +
                "        printAction(content=\"actionE\"),\n" +
                "        printAction(content=\"actionF\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 13);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(8);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(9);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(10);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(11);
                assertPrintAction(trace, "action[DEF]");

                trace = executionLink.getTraces().get(12);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            }
        });
    }

    @Test
    public void testHardAndJoinThenIfThenTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    } [printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]then {\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\")\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(12);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinThenIfThenFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] then {\n" +
                "        if(printCondition(content=\"conditionD\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintCondition(trace, "conditionD", false);
        });
    }

    @Test
    public void testHardAndJoinThenIfThenElseTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    } [printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]then {\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\")\n" +
                "        } else{\n" +
                "            throwExceptionAction()\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(12);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinThenIfThenElseFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] then {\n" +
                "        if(printCondition(content=\"conditionD\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        } else{\n" +
                "            printAction(content=\"actionD\")\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(11);
            assertPrintCondition(trace, "conditionD", false);

            trace = executionLink.getTraces().get(12);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testHardAndJoinThenSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] then {\n" +
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
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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

            Promise<ExecutionInstance> promise = startRule(rule, builder.build());

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();

            if (trueNum == 0) {
                assertExecutionInstance(executionInstance, 0, 3);

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 13);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(8);
                    assertPrintListener(trace, "listenerC", ListenerEvent.start);

                    trace = executionLink.getTraces().get(9);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(10);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);

                    trace = executionLink.getTraces().get(11);
                    assertExclusiveGateway(trace);

                    trace = executionLink.getTraces().get(12);
                    assertPrintCondition(trace, "condition[DEF]", false);
                }
            } else {
                assertExecutionInstance(executionInstance, 1, 0, 2);

                executionLink = executionInstance.getLinks().get(0);
                assertExecutionLink(executionLink, 15);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(8);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(9);
                assertJoinGateway(trace);

                trace = executionLink.getTraces().get(10);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(11);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(12);
                assertPrintCondition(trace, "condition[DEF]", true);

                trace = executionLink.getTraces().get(13);
                assertPrintAction(trace, "action[DEF]");

                trace = executionLink.getTraces().get(14);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);

                for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                    executionLink = executionInstance.getUnreachableLinks().get(i);
                    assertExecutionLink(executionLink, 12, 15);

                    trace = executionLink.getTraces().get(0);
                    assertPrintListener(trace, "listenerA", ListenerEvent.start);

                    trace = executionLink.getTraces().get(1);
                    assertStart(trace);

                    trace = executionLink.getTraces().get(8);
                    assertPrintListener(trace, "listenerC", ListenerEvent.start);

                    trace = executionLink.getTraces().get(9);
                    assertJoinGateway(trace);

                    trace = executionLink.getTraces().get(10);
                    assertPrintListener(trace, "listenerD", ListenerEvent.end);

                    trace = executionLink.getTraces().get(11);
                    assertExclusiveGateway(trace);
                }
            }
        });
    }

    @Test
    public void testParallelHardAndJoinAndSoftAndJoinAndOrJoinWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        printAction(content=\"actionA\")&,\n" +
                "        printAction(content=\"actionB\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")],\n" +
                "    join & {\n" +
                "        printAction(content=\"actionC\")&,\n" +
                "        printAction(content=\"actionD\")&,\n" +
                "        printAction(content=\"actionE\")&\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")],\n" +
                "    join | {\n" +
                "        printAction(content=\"actionF\"){\n" +
                "            printAction(content=\"actionG\"){\n" +
                "                printAction(content=\"actionH\"){\n" +
                "                    printAction(content=\"actionI\")&\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerG\"), printListener(event=\"end\", content=\"listenerH\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0);

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerG", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[AB]");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerH", ListenerEvent.end);

            executionLink = findLink(executionInstance, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerG", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[CDE]");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(6);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerH", ListenerEvent.end);

            executionLink = findLink(executionInstance, 10);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerG", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionF");

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionG");

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionH");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionI");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerH", ListenerEvent.end);
        });
    }

    @Test
    public void testSelectCascadedWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\"){\n" +
                "                printAction(content=\"actionB\"){\n" +
                "                    printAction(content=\"actionC\")\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(8);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSelectParallelWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                "            printAction(content=\"actionA\"),\n" +
                "            printAction(content=\"actionB\"),\n" +
                "            printAction(content=\"actionC\")\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0);

            for (int i = 0; i < executionInstance.getLinks().size(); i++) {
                executionLink = executionInstance.getLinks().get(i);
                assertExecutionLink(executionLink, 8);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(5);
                assertPrintCondition(trace, "conditionA", true);

                trace = executionLink.getTraces().get(6);
                assertPrintAction(trace, "action[ABC]");

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerB", ListenerEvent.end);
            }
        });
    }

    @Test
    public void testSelectFirstTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);
        });
    }

    @Test
    public void testSelectMiddleTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSelectLastTrueWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionC", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(5);
                assertPrintCondition(trace, "condition[AB]", false);
            }
        });
    }

    @Test
    public void testSelectAllFalseWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 0, 3);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);
                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(5);
                assertPrintCondition(trace, "condition[ABC]", false);
            }
        });
    }

    @Test
    public void testSelectLinkExceptionWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
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
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]");

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
            assertExecutionInstance(executionInstance, 1, 2);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            for (int i = 0; i < executionInstance.getUnreachableLinks().size(); i++) {
                executionLink = executionInstance.getUnreachableLinks().get(i);

                assertExecutionLink(executionLink, 6);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerA", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listenerC", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertExclusiveGateway(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintListener(trace, "listenerD", ListenerEvent.end);

                trace = executionLink.getTraces().get(5);
                assertThrowLinkTerminateCondition(trace);
            }
        });
    }

    @Test
    public void testParallelSelectWithGatewayAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            printAction(content=\"actionA\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")],\n" +
                "    select{\n" +
                "        if(printCondition(content=\"conditionC\", output=false)){\n" +
                "            printAction(content=\"actionC\")\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionD\", output=true)){\n" +
                "            printAction(content=\"actionD\"){\n" +
                "                printAction(content=\"actionE\")\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 2);

            for (ExecutionLink unreachableLink : executionInstance.getUnreachableLinks()) {
                assertExecutionLink(unreachableLink, 6);

                trace = unreachableLink.getTraces().get(0);
                assertPrintListener(trace, "listenerE", ListenerEvent.start);

                trace = unreachableLink.getTraces().get(1);
                assertStart(trace);

                trace = unreachableLink.getTraces().get(2);
                assertPrintListener(trace, "listener[AC]", ListenerEvent.start);

                trace = unreachableLink.getTraces().get(3);
                assertExclusiveGateway(trace);

                trace = unreachableLink.getTraces().get(4);
                assertPrintListener(trace, "listener[BD]", ListenerEvent.end);

                trace = unreachableLink.getTraces().get(5);
                assertPrintCondition(trace, "condition[AC]", false);
            }

            executionLink = findLink(executionInstance, 8);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);

            executionLink = findLink(executionInstance, 9);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionD", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionD");

            trace = executionLink.getTraces().get(7);
            assertPrintAction(trace, "actionE");

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);
        });
    }

    @Test
    public void testSubSingleWithSingleActionWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertRule(trace);

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubSingleWithCascadeActionWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\"){\n" +
                "            printAction(content=\"actionB\"){\n" +
                "                printAction(content=\"actionC\")\n" +
                "            }\n" +
                "        }\n" +
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionC");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubSingleWithParallelActionWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\"),\n" +
                "        printAction(content=\"actionB\"),\n" +
                "        printAction(content=\"actionC\")\n" +
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "action[ABC]");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubSingleWithIfWithSubAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertExecutionLink(executionLink, 11);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            for (int i = 4; i < 8; i++) {
                trace = executionLink.getTraces().get(i);

                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "action[AB]");
                } else {
                    assertPrintCondition(trace, "condition[AB]", null);
                }
            }

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(9);
            assertRule(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubSingleWithJoinWithSubAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            for (int i = 4; i < 11; i++) {
                trace = executionLink.getTraces().get(i);

                if (ElementType.ACTION.equals(trace.getType())) {
                    assertPrintAction(trace, "action[ABCDE]");
                } else {
                    assertJoinGateway(trace);
                }
            }

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(12);
            assertRule(trace);

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubSingleWithSelectWithSubAndGlobalListener() {
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
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 6);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubSingleWithSubWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        sub{\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")] \n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

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
            assertExecutionLink(executionLink, 12);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(10);
            assertRule(trace);

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);
        });
    }

    @Test
    public void testParallelSubWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")],\n" +
                "    sub{\n" +
                "        printAction(content=\"actionB\")\n" +
                "    }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

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
                assertExecutionLink(executionLink, 8);

                trace = executionLink.getTraces().get(0);
                assertPrintListener(trace, "listenerE", ListenerEvent.start);

                trace = executionLink.getTraces().get(1);
                assertStart(trace);

                trace = executionLink.getTraces().get(2);
                assertPrintListener(trace, "listener[AC]", ListenerEvent.start);

                trace = executionLink.getTraces().get(3);
                assertStart(trace);

                trace = executionLink.getTraces().get(4);
                assertPrintAction(trace, "action[AB]");

                trace = executionLink.getTraces().get(5);
                assertPrintListener(trace, "listener[BD]", ListenerEvent.end);

                trace = executionLink.getTraces().get(6);
                assertRule(trace);

                trace = executionLink.getTraces().get(7);
                assertPrintListener(trace, "listenerF", ListenerEvent.end);
            }
        });
    }

    @Test
    public void testSubNestedInActionWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    printAction(content=\"actionA\"){\n" +
                "        sub{\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertStart(trace);

            trace = executionLink.getTraces().get(5);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(6);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(7);
            assertRule(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubNestedInIfThenTrueWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertStart(trace);

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
    public void testSubNestedInIfThenFalseWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubNestedInIfThenElseTrueWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "    }else{\n" +
                "        throwExceptionAction()\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", true);

            trace = executionLink.getTraces().get(3);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(4);
            assertStart(trace);

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
    public void testSubNestedInIfThenElseFalseWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "        sub {\n" +
                "            throwExceptionAction()\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "    }else{\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(3);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubNestedInSelectWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                "            throwExceptionAction()\n" +
                "        },\n" +
                "        if(printCondition(content=\"conditionB\", output=true)){\n" +
                "            sub {\n" +
                "                printAction(content=\"actionA\")\n" +
                "            }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")]\n" +
                "        }\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionB", true);

            trace = executionLink.getTraces().get(4);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(5);
            assertStart(trace);

            trace = executionLink.getTraces().get(6);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(7);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(8);
            assertRule(trace);

            trace = executionLink.getTraces().get(9);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            executionLink = executionInstance.getUnreachableLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertExclusiveGateway(trace);

            trace = executionLink.getTraces().get(3);
            assertPrintCondition(trace, "conditionA", false);
        });
    }

    @Test
    public void testSubNestedInJoinWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] &\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]");

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
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertRule(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);
        });
    }

    @Test
    public void testSubNestedInJoinThenWithSubAndGlobalListener() {
        Rule rule = compile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionA\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerA\"), printListener(event=\"end\", content=\"listenerB\")] &\n" +
                "    } then {\n" +
                "        sub {\n" +
                "            printAction(content=\"actionB\")\n" +
                "        }[printListener(event=\"start\", content=\"listenerC\"), printListener(event=\"end\", content=\"listenerD\")]\n" +
                "    }\n" +
                "}[printListener(event=\"start\", content=\"listenerE\"), printListener(event=\"end\", content=\"listenerF\")]");

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
            assertExecutionLink(executionLink, 14);

            trace = executionLink.getTraces().get(0);
            assertPrintListener(trace, "listenerE", ListenerEvent.start);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertPrintListener(trace, "listenerA", ListenerEvent.start);

            trace = executionLink.getTraces().get(3);
            assertStart(trace);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(5);
            assertPrintListener(trace, "listenerB", ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertRule(trace);

            trace = executionLink.getTraces().get(7);
            assertJoinGateway(trace);

            trace = executionLink.getTraces().get(8);
            assertPrintListener(trace, "listenerC", ListenerEvent.start);

            trace = executionLink.getTraces().get(9);
            assertStart(trace);

            trace = executionLink.getTraces().get(10);
            assertPrintAction(trace, "actionB");

            trace = executionLink.getTraces().get(11);
            assertPrintListener(trace, "listenerD", ListenerEvent.end);

            trace = executionLink.getTraces().get(12);
            assertRule(trace);

            trace = executionLink.getTraces().get(13);
            assertPrintListener(trace, "listenerF", ListenerEvent.end);
        });
    }
}
