package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class TestRuntime extends TestRuntimeBase {

    @Test
    public void testAction1() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    printAction(content=\"test\")\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testAction2() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    printAction(content=\"test1\"),\n" +
                        "    printAction(content=\"test2\")\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testAction3() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    unknownAction()\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("unregistered action 'unknownAction'", promise.cause().getMessage());
    }

    @Test
    public void testCondition1() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                        "        printAction(content=\"test\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition2() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                        "        printAction(content=\"test1\"),\n" +
                        "        printAction(content=\"test2\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition3() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(printCondition(content=\"conditionA\", output=false)){\n" +
                        "        printAction(content=\"test1\")\n" +
                        "    }else{\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testCondition4() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(unknownCondition(content=false)){\n" +
                        "        printAction(content=\"test1\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("unregistered condition 'unknownCondition'", promise.cause().getMessage());
    }

    @Test
    public void testListener1() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    printAction(content=\"test\")[printListener(event=\"before\", content=\"listener1\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener2() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    if(printCondition(content=\"condition1\", output=true)[printListener(event=\"before\", content=\"listener1\")]){\n" +
                        "        printAction(content=\"action1\")[printListener(event=\"before\", content=\"listener2\")]\n" +
                        "    },\n" +
                        "    if(printCondition(content=\"condition2\", output=false)[printListener(event=\"success\", content=\"listener3\")]){\n" +
                        "        printAction(content=\"action2\")[printListener(event=\"before\", content=\"listener4\")]\n" +
                        "    }else{\n" +
                        "        printAction(content=\"action3\")[printListener(event=\"success\", content=\"listener5\")]\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testListener3() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    printAction(content=\"action1\")[unknownListener(event=\"before\", content=\"listener2\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("unregistered listener 'unknownListener'", promise.cause().getMessage());
    }

    @Test
    public void testSelect1() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    select {\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            printAction(content=\"test\")\n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            throwExceptionAction()\n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            throwExceptionAction()\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testSelect2() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    select {\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "            throwExceptionAction()\n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            printAction(content=\"test\")\n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            throwExceptionAction()\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testSelect3() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    select {\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "            throwExceptionAction()\n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "            throwExceptionAction()  \n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            printAction(content=\"test\")\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testSelect4() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("a", 1)
                .build();

        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    printAction(content=\"test1\"),\n" +
                        "    select {\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "            throwExceptionAction()\n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "            throwExceptionAction()  \n" +
                        "        },\n" +
                        "        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "            if(printCondition(content=\"conditionA\", output=false)){\n" +
                        "                select{\n" +
                        "                    if(printCondition(content=\"conditionB\", output=true)){\n" +
                        "                        throwExceptionAction()\n" +
                        "                    },\n" +
                        "                    if(printCondition(content=\"conditionC\", output=true)){\n" +
                        "                        throwExceptionAction()\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }else{\n" +
                        "                printAction(content=\"test2\"),\n" +
                        "                if(printCondition(content=\"conditionD\", output=true)){\n" +
                        "                    printAction(content=\"test3\"),\n" +
                        "                    select{\n" +
                        "                        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "                            throwExceptionAction()\n" +
                        "                        },\n" +
                        "                        if(getPropertyCondition(name=\"a\",expectedValue=1, output=false)){\n" +
                        "                            throwExceptionAction()  \n" +
                        "                        },\n" +
                        "                        if(getPropertyCondition(name=\"a\",expectedValue=1, output=true)){\n" +
                        "                            printAction(content=\"success\")\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }[getPropertyListener(event=\"before\", name=\"a\",expectedValue=1),getPropertyListener(event=\"success\", name=\"a\",expectedValue=1)],\n" +
                        "    printAction(content=\"test\")\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoin1() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\")\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\"), printListener(event=\"before\", content=\"listener2\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoin2() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\")&\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\"), printListener(event=\"before\", content=\"listener2\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoin3() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                        "            printAction(content=\"test1\")&\n" +
                        "        },\n" +
                        "        printAction(content=\"test2\")\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoin4() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                        "            printAction(content=\"test1\")&\n" +
                        "        },\n" +
                        "        printAction(content=\"test2\")&\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\")]\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoinThen1() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\")\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\"), printListener(event=\"before\", content=\"listener2\")] then {\n" +
                        "        printAction(content=\"test3\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoinThen2() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\")&\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\"), printListener(event=\"before\", content=\"listener2\")] then  {\n" +
                        "        printAction(content=\"test3\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoinThen3() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                        "            printAction(content=\"test1\")&\n" +
                        "        },\n" +
                        "        printAction(content=\"test2\")\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\")] then {\n" +
                        "        printAction(content=\"test3\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testJoinThen4() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    join & {\n" +
                        "        if(printCondition(content=\"conditionA\", output=false)){\n" +
                        "            printAction(content=\"test1\")&\n" +
                        "        },\n" +
                        "        printAction(content=\"test2\")&\n" +
                        "    } [printListener(event=\"success\", content=\"listener1\")] then  {\n" +
                        "        printAction(content=\"test3\")\n" +
                        "    }\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);
    }

    @Test
    public void testException1() {
        Promise<ExecutionInstance> promise = startRule(
                "{\n" +
                        "    printAction(content=\"test1\"){\n" +
                        "        throwExceptionAction(){\n" +
                        "            printAction(content=\"test2\")\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                null);

        promise.addListener(p -> {
            System.out.println("promise listener");
            Assert.assertFalse(p.isCancelled());
            Assert.assertTrue(p.isDone());
            Assert.assertFalse(p.isSuccess());
            Assert.assertTrue(p.isFailure());
        }).sync();

        assertPromise(promise, false, true, false, true);
        Assert.assertEquals("throw exception action", promise.cause().getMessage());
    }
}
