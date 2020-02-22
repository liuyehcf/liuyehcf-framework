package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.PropertyUpdateType;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestPropertyTrace extends TestTraceBase {

    @Test
    public void testCreatePropertyCascade() {
        Flow flow = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=1){\n" +
                "        setPropertyAction(name=\"b\", value=2){\n" +
                "            setPropertyAction(name=\"c\", value=3)\n" +
                "        }\n" +
                "    }\n" +
                "}[\n" +
                "    getPropertyListener(event=\"success\", name=\"a\", expectedValue=1),\n" +
                "    getPropertyListener(event=\"success\", name=\"b\", expectedValue=2),\n" +
                "    getPropertyListener(event=\"success\", name=\"c\", expectedValue=3)\n" +
                "]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 3);

            trace = executionInstance.getTraces().get(0);
            assertGetPropertyListener(trace, "a", 1, ListenerEvent.success);

            trace = executionInstance.getTraces().get(1);
            assertGetPropertyListener(trace, "b", 2, ListenerEvent.success);

            trace = executionInstance.getTraces().get(2);
            assertGetPropertyListener(trace, "c", 3, ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "b", 2, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertSetPropertyAction(trace, "c", 3, PropertyUpdateType.CREATE, null);

            Assert.assertEquals(3, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
            Assert.assertEquals(2, executionInstance.getEnv().get("b"));
            Assert.assertEquals(3, executionInstance.getEnv().get("c"));
        });
    }

    @Test
    public void testCreatePropertyParallel() {
        Flow flow = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=1),\n" +
                "    setPropertyAction(name=\"b\", value=2),\n" +
                "    setPropertyAction(name=\"c\", value=3)\n" +
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

            Assert.assertEquals(3, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
            Assert.assertEquals(2, executionInstance.getEnv().get("b"));
            Assert.assertEquals(3, executionInstance.getEnv().get("c"));
        });
    }

    @Test
    public void testUpdatePropertyCascade() {
        Flow flow = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=1){\n" +
                "        setPropertyAction(name=\"a\", value=2){\n" +
                "            setPropertyAction(name=\"a\", value=3)\n" +
                "        }\n" +
                "    }\n" +
                "}[\n" +
                "    getPropertyListener(event=\"success\", name=\"a\", expectedValue=3)\n" +
                "]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertGetPropertyListener(trace, "a", 3, ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetPropertyAction(trace, "a", 1, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "a", 2, PropertyUpdateType.UPDATE, 1);

            trace = executionLink.getTraces().get(3);
            assertSetPropertyAction(trace, "a", 3, PropertyUpdateType.UPDATE, 2);

            Assert.assertEquals(1, executionInstance.getEnv().size());
            Assert.assertEquals(3, executionInstance.getEnv().get("a"));
        });
    }

    @Test
    public void testSingleGlobalBeforeListenerSetPropertyCreate() {
        Flow flow = compile("{\n" +
                "    getPropertyAction(name=\"a\", expectedValue=1)\n" +
                "}[setPropertyListener(event=\"before\", name=\"a\", value=1)]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertSetPropertyListener(trace, "a", 1, ListenerEvent.before, PropertyUpdateType.CREATE, null);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "a", 1);

            Assert.assertEquals(1, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
        });
    }

    @Test
    public void testMultiGlobalBeforeListenerSetPropertyCreate() {
        Flow flow = compile("{\n" +
                "    getPropertyAction(name=\"a\", expectedValue=1){\n" +
                "        getPropertyAction(name=\"b\", expectedValue=2)\n" +
                "    },\n" +
                "    getPropertyAction(name=\"b\", expectedValue=2)\n" +
                "}[setPropertyListener(event=\"before\", name=\"a\", value=1), setPropertyListener(event=\"before\", name=\"b\", value=2)]\n");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertSetPropertyListener(trace, "a", 1, ListenerEvent.before, PropertyUpdateType.CREATE, null);

            trace = executionInstance.getTraces().get(1);
            assertSetPropertyListener(trace, "b", 2, ListenerEvent.before, PropertyUpdateType.CREATE, null);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "b", 2);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "a", 1);

            trace = executionLink.getTraces().get(2);
            assertGetPropertyAction(trace, "b", 2);

            Assert.assertEquals(2, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
            Assert.assertEquals(2, executionInstance.getEnv().get("b"));
        });
    }

    @Test
    public void testSingleGlobalBeforeListenerSetPropertyUpdate() {
        Flow flow = compile("{\n" +
                "    getPropertyAction(name=\"a\", expectedValue=11)\n" +
                "}[setPropertyListener(event=\"before\", name=\"a\", value=11)]");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a", 1)
                    .put("b", 2)
                    .put("c", 3)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 1);

            trace = executionInstance.getTraces().get(0);
            assertSetPropertyListener(trace, "a", 11, ListenerEvent.before, PropertyUpdateType.UPDATE, 1);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "a", 11);

            Assert.assertEquals(3, executionInstance.getEnv().size());
            Assert.assertEquals(11, executionInstance.getEnv().get("a"));
            Assert.assertEquals(2, executionInstance.getEnv().get("b"));
            Assert.assertEquals(3, executionInstance.getEnv().get("c"));
        });
    }

    @Test
    public void testMultiGlobalBeforeListenerSetPropertyUpdate() {
        Flow flow = compile("{\n" +
                "    getPropertyAction(name=\"a\", expectedValue=11){\n" +
                "        getPropertyAction(name=\"b\", expectedValue=22)\n" +
                "    },\n" +
                "    getPropertyAction(name=\"b\", expectedValue=22)\n" +
                "}[setPropertyListener(event=\"before\", name=\"a\", value=11), setPropertyListener(event=\"before\", name=\"b\", value=22)]\n");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a", 1)
                    .put("b", 2)
                    .put("c", 3)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 2, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertSetPropertyListener(trace, "a", 11, ListenerEvent.before, PropertyUpdateType.UPDATE, 1);

            trace = executionInstance.getTraces().get(1);
            assertSetPropertyListener(trace, "b", 22, ListenerEvent.before, PropertyUpdateType.UPDATE, 2);

            executionLink = findLink(executionInstance, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "b", 22);

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "a", 11);

            trace = executionLink.getTraces().get(2);
            assertGetPropertyAction(trace, "b", 22);

            Assert.assertEquals(3, executionInstance.getEnv().size());
            Assert.assertEquals(11, executionInstance.getEnv().get("a"));
            Assert.assertEquals(22, executionInstance.getEnv().get("b"));
            Assert.assertEquals(3, executionInstance.getEnv().get("c"));
        });
    }

    @Test
    public void testNodeBeforeListenerGetPropertySetByPreviousListener() {
        Flow flow = compile("{\n" +
                "    getPropertyAction(name=\"a\", expectedValue=1)[\n" +
                "        setPropertyListener(event=\"before\", name=\"a\", value=1),\n" +
                "        getPropertyListener(event=\"before\", name=\"a\", expectedValue=1)\n" +
                "    ]\n" +
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
            assertSetPropertyListener(trace, "a", 1, ListenerEvent.before, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(2);
            assertGetPropertyListener(trace, "a", 1, ListenerEvent.before);

            trace = executionLink.getTraces().get(3);
            assertGetPropertyAction(trace, "a", 1);

            Assert.assertEquals(1, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
        });
    }

    @Test
    public void testNodeSuccessListenerGetPropertySetByPreviousListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")[\n" +
                "        setPropertyListener(event=\"success\", name=\"a\", value=1),\n" +
                "        getPropertyListener(event=\"success\", name=\"a\", expectedValue=1)\n" +
                "    ]\n" +
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
            assertPrintAction(trace, "actionA");

            trace = executionLink.getTraces().get(2);
            assertSetPropertyListener(trace, "a", 1, ListenerEvent.success, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(3);
            assertGetPropertyListener(trace, "a", 1, ListenerEvent.success);


            Assert.assertEquals(1, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
        });
    }

    @Test
    public void testGlobalBeforeListenerGetPropertySetByPreviousListener() {
        Flow flow = compile("{\n" +
                "    getPropertyAction(name=\"a\", expectedValue=1)\n" +
                "}[\n" +
                "    setPropertyListener(event=\"before\", name=\"a\", value=1),\n" +
                "    getPropertyListener(event=\"before\", name=\"a\", expectedValue=1)\n" +
                "]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertSetPropertyListener(trace, "a", 1, ListenerEvent.before, PropertyUpdateType.CREATE, null);

            trace = executionInstance.getTraces().get(1);
            assertGetPropertyListener(trace, "a", 1, ListenerEvent.before);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertGetPropertyAction(trace, "a", 1);

            Assert.assertEquals(1, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
        });
    }

    @Test
    public void testGlobalSuccessListenerGetPropertySetByPreviousListener() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"actionA\")\n" +
                "}[\n" +
                "    setPropertyListener(event=\"success\", name=\"a\", value=1),\n" +
                "    getPropertyListener(event=\"success\", name=\"a\", expectedValue=1)\n" +
                "]");

        executeTimes(() -> {
            Promise<ExecutionInstance> promise = startFlow(flow, null);

            promise.sync();
            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance;
            ExecutionLink executionLink;
            Trace trace;

            executionInstance = promise.get();
            assertExecutionInstance(executionInstance, 1, 0, 2);

            trace = executionInstance.getTraces().get(0);
            assertSetPropertyListener(trace, "a", 1, ListenerEvent.success, PropertyUpdateType.CREATE, null);

            trace = executionInstance.getTraces().get(1);
            assertGetPropertyListener(trace, "a", 1, ListenerEvent.success);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertPrintAction(trace, "actionA");

            Assert.assertEquals(1, executionInstance.getEnv().size());
            Assert.assertEquals(1, executionInstance.getEnv().get("a"));
        });
    }

    @Test
    public void testLinkEnvNotAffectedByMergeOperation() {
        Flow flow = compile("{\n" +
                "    setPropertyAction(name=\"a\", value=5),\n" +
                "    setPropertyAction(name=\"a\", value=6){\n" +
                "        setPropertyAction(name=\"b\", value=7)\n" +
                "    },\n" +
                "    setPropertyAction(name=\"a\", value=8){\n" +
                "        setPropertyAction(name=\"b\", value=9){\n" +
                "            setPropertyAction(name=\"c\", value=10)\n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a", 1)
                    .put("b", 2)
                    .put("c", 3)
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env);

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
            assertSetPropertyAction(trace, "a", 5, PropertyUpdateType.UPDATE, 1);

            Assert.assertEquals(3, executionLink.getEnv().size());
            Assert.assertEquals(5, executionLink.getEnv().get("a"));
            Assert.assertEquals(2, executionLink.getEnv().get("b"));
            Assert.assertEquals(3, executionLink.getEnv().get("c"));

            executionLink = findLink(executionInstance, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetPropertyAction(trace, "a", 6, PropertyUpdateType.UPDATE, 1);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "b", 7, PropertyUpdateType.UPDATE, 2);

            Assert.assertEquals(3, executionLink.getEnv().size());
            Assert.assertEquals(6, executionLink.getEnv().get("a"));
            Assert.assertEquals(7, executionLink.getEnv().get("b"));
            Assert.assertEquals(3, executionLink.getEnv().get("c"));

            executionLink = findLink(executionInstance, 4);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetPropertyAction(trace, "a", 8, PropertyUpdateType.UPDATE, 1);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "b", 9, PropertyUpdateType.UPDATE, 2);

            trace = executionLink.getTraces().get(3);
            assertSetPropertyAction(trace, "c", 10, PropertyUpdateType.UPDATE, 3);

            Assert.assertEquals(3, executionLink.getEnv().size());
            Assert.assertEquals(8, executionLink.getEnv().get("a"));
            Assert.assertEquals(9, executionLink.getEnv().get("b"));
            Assert.assertEquals(10, executionLink.getEnv().get("c"));

            Assert.assertEquals(3, executionInstance.getEnv().size());
            Assert.assertTrue(Objects.equals(5, executionLink.getEnv().get("a"))
                    || Objects.equals(6, executionLink.getEnv().get("a"))
                    || Objects.equals(8, executionLink.getEnv().get("a")));
            Assert.assertTrue(Objects.equals(7, executionLink.getEnv().get("b"))
                    || Objects.equals(9, executionLink.getEnv().get("b")));
            Assert.assertEquals(10, executionLink.getEnv().get("c"));
        });
    }

    @Test
    public void testGetPropertyFromUnreachableSubFlow() {
        Flow flow = compile("{\n" +
                "    sub {\n" +
                "        setPropertyAction(name=\"a\", value=11){\n" +
                "            setPropertyAction(name=\"b\", value=22){\n" +
                "                setPropertyAction(name=\"c\", value=3){\n" +
                "                    if(printCondition(content=\"conditionA\", output=false)){\n" +
                "                        throwExceptionAction()\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        throwExceptionAction()\n" +
                "    } else{\n" +
                "        getPropertyAction(name=\"a\", expectedValue=11){\n" +
                "            getPropertyAction(name=\"b\", expectedValue=22){\n" +
                "                getPropertyAction(name=\"c\", expectedValue=3)\n" +
                "            } \n" +
                "        }\n" +
                "    }\n" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("a", 1)
                    .put("b", 2)
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
            assertExecutionLink(executionLink, 10);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertStart(trace);

            trace = executionLink.getTraces().get(2);
            assertSetPropertyAction(trace, "a", 11, PropertyUpdateType.UPDATE, 1);

            trace = executionLink.getTraces().get(3);
            assertSetPropertyAction(trace, "b", 22, PropertyUpdateType.UPDATE, 2);

            trace = executionLink.getTraces().get(4);
            assertSetPropertyAction(trace, "c", 3, PropertyUpdateType.CREATE, null);

            trace = executionLink.getTraces().get(5);
            assertPrintCondition(trace, "conditionA", false);

            trace = executionLink.getTraces().get(6);
            assertFlow(trace);

            trace = executionLink.getTraces().get(7);
            assertGetPropertyAction(trace, "a", 11);

            trace = executionLink.getTraces().get(8);
            assertGetPropertyAction(trace, "b", 22);

            trace = executionLink.getTraces().get(9);
            assertGetPropertyAction(trace, "c", 3);

            Assert.assertEquals(3, executionLink.getEnv().size());
            Assert.assertEquals(11, executionLink.getEnv().get("a"));
            Assert.assertEquals(22, executionLink.getEnv().get("b"));
            Assert.assertEquals(3, executionLink.getEnv().get("c"));

            Assert.assertEquals(3, executionInstance.getEnv().size());
            Assert.assertEquals(11, executionInstance.getEnv().get("a"));
            Assert.assertEquals(22, executionInstance.getEnv().get("b"));
            Assert.assertEquals(3, executionInstance.getEnv().get("c"));
        });
    }
}
