package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestProperty extends TestTraceBase {

    @Test
    public void testCreatePropertyCascade() {
        Rule rule = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=1){\n" +
                "        setPropertyAction(name=\"b\", value=2){\n" +
                "            setPropertyAction(name=\"c\", value=3)\n" +
                "        }\n" +
                "    }\n" +
                "}[\n" +
                "    getPropertyListener(event=\"end\", name=\"a\", expectedValue=1),\n" +
                "    getPropertyListener(event=\"end\", name=\"b\", expectedValue=2),\n" +
                "    getPropertyListener(event=\"end\", name=\"c\", expectedValue=3)\n" +
                "]");

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
            assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "b", 2, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertSetPropertyAction(trace, "c", 3, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(4);
            assertGetPropertyListener(trace, "a", 1, ListenerEvent.end);

            trace = executionLink.getTraces().get(5);
            assertGetPropertyListener(trace, "b", 2, ListenerEvent.end);

            trace = executionLink.getTraces().get(6);
            assertGetPropertyListener(trace, "c", 3, ListenerEvent.end);
        });
    }

    @Test
    public void testCreatePropertyParallel() {
        Rule rule = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=1),\n" +
                "    setPropertyAction(name=\"b\", value=2),\n" +
                "    setPropertyAction(name=\"c\", value=3)\n" +
                "}");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startRule(rule, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 3, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            if (trace.getArguments().get(0).getValue().equals("a")) {
                assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);
            } else if (trace.getArguments().get(0).getValue().equals("b")) {
                assertSetPropertyAction(trace, "b", 2, PropertyUpdateType.CREATE, null);
            } else {
                assertSetPropertyAction(trace, "c", 3, PropertyUpdateType.CREATE, null);
            }
        });
    }

    @Test
    public void testUpdatePropertyCascade() {
        Rule rule = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=1){\n" +
                "        setPropertyAction(name=\"a\", value=2){\n" +
                "            setPropertyAction(name=\"a\", value=3)\n" +
                "        }\n" +
                "    }\n" +
                "}[\n" +
                "    getPropertyListener(event=\"end\", name=\"a\", expectedValue=3)\n" +
                "]");

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
            assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "a", 2, PropertyUpdateType.UPDATE, 1);

            trace = executionLink.getTraces().get(3);
            assertSetPropertyAction(trace, "a", 3, PropertyUpdateType.UPDATE, 2);

            trace = executionLink.getTraces().get(4);
            assertGetPropertyListener(trace, "a", 3, ListenerEvent.end);
        });
    }
}
