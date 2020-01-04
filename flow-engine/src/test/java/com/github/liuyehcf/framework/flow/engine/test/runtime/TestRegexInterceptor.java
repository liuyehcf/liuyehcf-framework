package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.test.runtime.interceptor.ActionRegexInterceptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestRegexInterceptor extends TestRuntimeBase {

    @Test
    public void singleCondition() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(printCondition(content=\"conditionA\", output=true))\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(0, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void singleConditionWithListener() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\")])\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(0, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void singleAction() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    printAction(content=\"actionA\")\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(1, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void singleActionWithListener() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    printAction(content=\"actionA\")[printListener(event=\"before\", content=\"listenerA\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(1, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void multiAction() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    printAction(content=\"actionA\"){\n" +
                        "        printAction(content=\"actionB\"){\n" +
                        "            printAction(content=\"actionC\")\n" +
                        "        }\n" +
                        "    },\n" +
                        "    printAction(content=\"actionD\"){\n" +
                        "        printAction(content=\"actionE\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(5, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void multiActionWithListener() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    printAction(content=\"actionA\")[printListener(event=\"before\", content=\"listenerA\")]{\n" +
                        "        printAction(content=\"actionB\")[printListener(event=\"before\", content=\"listenerB\")]{\n" +
                        "            printAction(content=\"actionC\")[printListener(event=\"before\", content=\"listenerC\")]\n" +
                        "        }\n" +
                        "    },\n" +
                        "    printAction(content=\"actionD\")[printListener(event=\"before\", content=\"listenerD\")]{\n" +
                        "        printAction(content=\"actionE\")[printListener(event=\"before\", content=\"listenerE\")]\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(5, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void mix() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    sub{\n" +
                        "        if(printCondition(content=\"conditionA\", output=true)){\n" +
                        "            if(printCondition(content=\"conditionB\", output=false)){\n" +
                        "                throwExceptionAction()\n" +
                        "            }else{\n" +
                        "                join {\n" +
                        "                    select{\n" +
                        "                        if(printCondition(content=\"conditionC\", output=true))&\n" +
                        "                    },\n" +
                        "                    if(printCondition(content=\"conditionD\", output=false)){\n" +
                        "                        throwExceptionAction()&\n" +
                        "                    }\n" +
                        "                } then {\n" +
                        "                    printAction(content=\"actionA\")\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    } then {\n" +
                        "        printAction(content=\"actionB\")\n" +
                        "    } else{\n" +
                        "        throwExceptionAction()\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(2, ActionRegexInterceptor.COUNTER.get());
    }

    @Test
    public void mixWithListener() {
        ActionRegexInterceptor.COUNTER.set(0);

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    sub{\n" +
                        "        if(printCondition(content=\"conditionA\", output=true)[printListener(event=\"before\", content=\"listenerA\")]){\n" +
                        "            if(printCondition(content=\"conditionB\", output=false)[printListener(event=\"before\", content=\"listenerA\")]){\n" +
                        "                throwExceptionAction()[printListener(event=\"before\", content=\"listenerA\")]\n" +
                        "            }else{\n" +
                        "                join {\n" +
                        "                    select{\n" +
                        "                        if(printCondition(content=\"conditionC\", output=true)[printListener(event=\"before\", content=\"listenerA\")])&\n" +
                        "                    },\n" +
                        "                    if(printCondition(content=\"conditionD\", output=false)[printListener(event=\"before\", content=\"listenerA\")]){\n" +
                        "                        throwExceptionAction()[printListener(event=\"before\", content=\"listenerA\")]&\n" +
                        "                    }\n" +
                        "                } then {\n" +
                        "                    printAction(content=\"actionA\")[printListener(event=\"before\", content=\"listenerA\")]\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    } then {\n" +
                        "        printAction(content=\"actionB\")[printListener(event=\"before\", content=\"listenerA\")]\n" +
                        "    } else{\n" +
                        "        throwExceptionAction()[printListener(event=\"before\", content=\"listenerA\")]\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals(2, ActionRegexInterceptor.COUNTER.get());
    }
}
