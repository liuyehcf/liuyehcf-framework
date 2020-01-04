package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/5/28
 */
public class TestRedundantArgument extends TestRuntimeBase {

    @Test
    public void testAction() {
        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    printAction(redundantArg1=1, content=\"actionA\",redundantArg2=2)\n" +
                "}", null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition() {
        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    if(printCondition(redundantArg1=1, content=\"actionA\",redundantArg2=2, output=true)){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}", null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener() {
        Promise<ExecutionInstance> promise = startFlow("{\n" +
                "    printAction(content=\"actionA\")[printListener(event=\"before\", redundantArg1=1, content=\"actionA\",redundantArg2=2)]\n" +
                "}", null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }
}
