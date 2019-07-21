package com.github.liuyehcf.framework.rule.engine.test.runtime.trace;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
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
public class TestAttr extends TestTraceBase {
    @Test
    public void testAttr1() {
        Rule rule = compile("{\n" +
                "    setTraceAttrAction(name=\"a\",value=2)\n" +
                "}");

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
            assertExecutionInstance(executionInstance, 1, 0);

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
        Rule rule = compile("{\n" +
                "    setTraceAttrAction(name=\"a\",value=2)[setTraceAttrListener(event=\"end\",name=\"b\",value=\"test\")]\n" +
                "}");

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
            assertExecutionInstance(executionInstance, 1, 0);

            executionLink = executionInstance.getLinks().get(0);
            assertExecutionLink(executionLink, 3);

            trace = executionLink.getTraces().get(0);
            assertStart(trace);

            trace = executionLink.getTraces().get(1);
            assertSetAttrAction(trace, "a", 2);

            trace = executionLink.getTraces().get(2);
            assertSetAttrListener(trace, "b", "test", ListenerEvent.end);
        });
    }

    @Test
    public void testAttr3() {
        Rule rule = compile("{\n" +
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
}
