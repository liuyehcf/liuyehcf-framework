package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class TestMissingArgument extends TestRuntimeBase {

    @Test
    public void testAction() {
        Promise<ExecutionInstance> promise = startRule("{\n" +
                "    missingArgumentAction(notMissing1=1,notMissing2=2)\n" +
                "}", null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition() {
        Promise<ExecutionInstance> promise = startRule("{\n" +
                "    if(missingArgumentCondition(notMissing1=1,notMissing2=2)){\n" +
                "        printAction(content=\"actionA\")\n" +
                "    }\n" +
                "}", null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener() {
        Promise<ExecutionInstance> promise = startRule("{\n" +
                "    printAction(content=\"actionA\")[missingArgumentListener(event=\"before\", notMissing1=1,notMissing2=2)]\n" +
                "}", null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }
}
