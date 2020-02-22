package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public class TestDotSlashName extends TestRuntimeBase {
    @Test
    public void testDotName() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Flow flow = compile(
                "{\n" +
                        "    if(dot.name.condition(output=true) [dot.name.listener(event=\"before\")]) {\n" +
                        "        dot.name.action()\n" +
                        "    }\n" +
                        "}");

        Promise<ExecutionInstance> promise = startFlow(flow, env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testSlashName() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Flow flow = compile(
                "{\n" +
                        "    if(slash/name/condition(output=false) [slash/name/listener(event=\"success\")]) {\n" +
                        "        dot.name.action()\n" +
                        "    }else{\n" +
                        "        slash/name/action(){\n" +
                        "            slash/name/action()[slash/name/listener(event=\"before\")]\n" +
                        "        }\n" +
                        "    }\n" +
                        "}");

        Promise<ExecutionInstance> promise = startFlow(flow, env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testMixed() {
        Map<String, Object> env = EnvBuilder.builder()
                .build();

        Flow flow = compile(
                "{\n" +
                        "    if(slash/name/condition(output=false) [dot.name.listener(event=\"success\")]) {\n" +
                        "        dot.name.action()\n" +
                        "    }else{\n" +
                        "        dot.name.action(){\n" +
                        "            slash/name/action()[slash/name/listener(event=\"before\")]\n" +
                        "        }\n" +
                        "    }\n" +
                        "}");

        Promise<ExecutionInstance> promise = startFlow(flow, env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }
}
