package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.*;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/18
 */
@SuppressWarnings("all")
public class TestAttrTrace extends TestTraceBase {
    @Test
    public void testAttr1() {
        Flow flow = compile("{\n" +
                "    setTraceAttrAction(name=\"a\",value=2)\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 2);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetAttrAction(trace, "a", 2);
        });
    }

    @Test
    public void testAttr2() {
        Flow flow = compile("{\n" +
                "    setTraceAttrAction(name=\"a\",value=2)[setTraceAttrListener(event=\"success\",name=\"b\",value=\"test\")]\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetAttrAction(trace, "a", 2);

            trace = executionLink.getTraces().get(2);
            assertSetAttrListener(trace, "b", "test", ListenerEvent.success);
        });
    }

    @Test
    public void testAttr3() {
        Flow flow = compile("{\n" +
                "    if(setTraceAttrCondition(name=\"a\",value=2,output=true)){\n" +
                "        printAction(content=\"hehe\"){\n" +
                "            if(setTraceAttrCondition(name=\"b\",value=3L,output=false)){\n" +
                "                printAction(content=\"unreached\")\n" +
                "            }else{\n" +
                "                printAction(content=\"xixi\")\n" +
                "            }\n" +
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
            assertExecutionInstance(executionInstance, 1, 0, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 5);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetAttrCondition(trace, "a", 2, true);

            trace = executionLink.getTraces().get(2);
            assertPrintAction(trace, "hehe");

            trace = executionLink.getTraces().get(3);
            assertSetAttrCondition(trace, "b", 3L, false);

            trace = executionLink.getTraces().get(4);
            assertPrintAction(trace, "xixi");
        });
    }

    @Test
    public void testAttr4() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"test\")" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            Promise<ExecutionInstance> promise = startFlow(flow, env, null);

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
            assertPrintAction(trace, "test");

            Assert.assertTrue(executionInstance.getAttributes().isEmpty());
        });
    }

    @Test
    public void testAttr5() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"test\")" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            List<Attribute> attributes = Lists.newArrayList();
            Promise<ExecutionInstance> promise = startFlow(flow, env, attributes);

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
            assertPrintAction(trace, "test");

            Assert.assertTrue(executionInstance.getAttributes().isEmpty());
        });
    }

    public void testAttr6() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"test\")" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            List<Attribute> attributes = Lists.newArrayList();
            attributes.add(null);
            attributes.add(new DefaultAttribute("a", "b"));
            Promise<ExecutionInstance> promise = startFlow(flow, env, attributes);

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
            assertPrintAction(trace, "test");

            Assert.assertEquals(1, executionInstance.getAttributes().size());
            Attribute attribute = executionInstance.getAttributes().get("a");
            Assert.assertEquals("a", attribute.getName());
            Assert.assertEquals("b", attribute.getValue());
        });
    }

    public void testAttr7() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"test\")" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            List<Attribute> attributes = Lists.newArrayList();
            attributes.add(null);
            attributes.add(new DefaultAttribute("a", "b"));
            attributes.add(new DefaultAttribute("a", "b"));
            Promise<ExecutionInstance> promise = startFlow(flow, env, attributes);

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
            assertPrintAction(trace, "test");

            Assert.assertEquals(1, executionInstance.getAttributes().size());
            Attribute attribute = executionInstance.getAttributes().get("a");
            Assert.assertEquals("a", attribute.getName());
            Assert.assertEquals("b", attribute.getValue());
        });
    }

    public void testAttr8() {
        Flow flow = compile("{\n" +
                "    printAction(content=\"test\")" +
                "}");

        executeTimes(() -> {
            Map<String, Object> env = EnvBuilder.builder()
                    .build();

            List<Attribute> attributes = Lists.newArrayList();
            attributes.add(null);
            attributes.add(new DefaultAttribute("a", "b"));
            attributes.add(new DefaultAttribute("c", "d"));
            attributes.add(null);
            attributes.add(new DefaultAttribute("a", "b"));
            Promise<ExecutionInstance> promise = startFlow(flow, env, attributes);

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
            assertPrintAction(trace, "test");

            Assert.assertEquals(2, executionInstance.getAttributes().size());
            Attribute attribute = executionInstance.getAttributes().get("a");
            Assert.assertEquals("a", attribute.getName());
            Assert.assertEquals("b", attribute.getValue());
            attribute = executionInstance.getAttributes().get("c");
            Assert.assertEquals("c", attribute.getName());
            Assert.assertEquals("d", attribute.getValue());
        });
    }
}
