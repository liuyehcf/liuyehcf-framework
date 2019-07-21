package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class TestConcurrent extends TestRuntimeBase {

    @Test
    public void testAction() {
        Rule rule = compile(
                "{\n" +
                        "    printAction(content=\"test1\"),\n" +
                        "    printAction(content=\"test2\"),\n" +
                        "    printAction(content=\"test3\"),\n" +
                        "    printAction(content=\"test4\"),\n" +
                        "    printAction(content=\"test5\"),\n" +
                        "    printAction(content=\"test6\"),\n" +
                        "    printAction(content=\"test7\"),\n" +
                        "    printAction(content=\"test8\"),\n" +
                        "    printAction(content=\"test9\"),\n" +
                        "    printAction(content=\"test10\")\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Promise<ExecutionInstance> promise = startRule(rule, null);
            promise.sync();
            assertPromise(promise, false, true, true, false);
        }
    }

    @Test
    public void testIfThen() {
        Rule rule = compile(
                "{\n" +
                        "    if(printCondition(content=\"conditionA\", output=true)){\n" +
                        "        printAction(content=\"test1\"),\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\"),\n" +
                        "        printAction(content=\"test4\"),\n" +
                        "        printAction(content=\"test5\"),\n" +
                        "        printAction(content=\"test6\"),\n" +
                        "        printAction(content=\"test7\"),\n" +
                        "        printAction(content=\"test8\"),\n" +
                        "        printAction(content=\"test9\"),\n" +
                        "        printAction(content=\"test10\")\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Promise<ExecutionInstance> promise = startRule(rule, null);
            promise.sync();
            assertPromise(promise, false, true, true, false);
        }
    }

    @Test
    public void testIfThenElse() {
        Rule rule = compile(
                "{\n" +
                        "    if(setPropertyCondition(name=\"a.b.c\", value=${output}, output=${output})){\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=true)\n" +
                        "    }else{\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "        getPropertyAction(name=\"a.b.c\", expectedValue=false)\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("output", RANDOM.nextBoolean())
                    .put("a.b", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);
            promise.sync();
            assertPromise(promise, false, true, true, false);
        }
    }

    @Test
    public void testSelect1() {
        Rule rule = compile(
                "{\n" +
                        "    join {\n" +
                        "        sleepAction(timeout=${timeout1})&,\n" +
                        "        sleepAction(timeout=${timeout2})&,\n" +
                        "        sleepAction(timeout=${timeout3})&,\n" +
                        "        if(sleepCondition(timeout=${timeout4}, output=${output1})){\n" +
                        "            sleepAction(timeout=${timeout5})&,\n" +
                        "            sleepAction(timeout=${timeout6}){\n" +
                        "                sleepAction(timeout=${timeout7})&\n" +
                        "            }\n" +
                        "        },\n" +
                        "        if(sleepCondition(timeout=${timeout8}, output=${output2})){\n" +
                        "            sleepAction(timeout=${timeout9}){\n" +
                        "                sleepAction(timeout=${timeout10})&\n" +
                        "            },\n" +
                        "            sleepAction(timeout=${timeout11})&\n" +
                        "        },\n" +
                        "        if(sleepCondition(timeout=${timeout12}, output=${output3})){\n" +
                        "            sleepAction(timeout=${timeout13})&,\n" +
                        "            sleepAction(timeout=${timeout14}){\n" +
                        "                sleepAction(timeout=${timeout15})&,\n" +
                        "                sleepAction(timeout=${timeout16})&,\n" +
                        "                sleepAction(timeout=${timeout17})&\n" +
                        "            }\n" +
                        "        } else {\n" +
                        "            sleepAction(timeout=${timeout18}){\n" +
                        "                sleepAction(timeout=${timeout19})&\n" +
                        "            },\n" +
                        "            sleepAction(timeout=${timeout20})&\n" +
                        "        },\n" +
                        "        if(sleepCondition(timeout=${timeout21}, output=${output4})){\n" +
                        "            sleepAction(timeout=${timeout22}){\n" +
                        "                sleepAction(timeout=${timeout23})&\n" +
                        "            },\n" +
                        "            sleepAction(timeout=${timeout24})&\n" +
                        "        } else {\n" +
                        "            sleepAction(timeout=${timeout25})&,\n" +
                        "            sleepAction(timeout=${timeout26}){\n" +
                        "                sleepAction(timeout=${timeout27})&,\n" +
                        "                sleepAction(timeout=${timeout28})&,\n" +
                        "                sleepAction(timeout=${timeout29})&\n" +
                        "            }\n" +
                        "        },\n" +
                        "        sleepAction(timeout=${timeout30})&,\n" +
                        "        sleepAction(timeout=${timeout31})&,\n" +
                        "        sleepAction(timeout=${timeout32})&\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            EnvBuilder builder = EnvBuilder.builder();
            for (int j = 1; j <= 32; j++) {
                builder.put("timeout" + j, (long) RANDOM.nextInt(200));
                if (j < 5) {
                    builder.put("output" + j, RANDOM.nextBoolean());
                }
            }

            Map<String, Object> env = builder.build();

            int baseTraceNum = 8;
            if ((boolean) env.get("output1")) {
                baseTraceNum += 4;
            }
            if ((boolean) env.get("output2")) {
                baseTraceNum += 4;
            }
            if ((boolean) env.get("output3")) {
                baseTraceNum += 6;
            } else {
                baseTraceNum += 4;
            }
            if ((boolean) env.get("output4")) {
                baseTraceNum += 4;
            } else {
                baseTraceNum += 6;
            }

            Promise<ExecutionInstance> promise = startRule(rule, env);
            promise.sync();

            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance = promise.get();
            Assert.assertEquals(1, executionInstance.getLinks().size());
            ExecutionLink executionLink = executionInstance.getLinks().get(0);
            Assert.assertEquals(baseTraceNum, executionLink.getTraces().size());
        }
    }

    @Test
    public void testJoin1() {
        Rule rule = compile(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        if(setPropertyCondition(name=\"a.b.c\", value=true, output=true)){\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)\n" +
                        "        }else{\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)\n" +
                        "        },\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\")&,\n" +
                        "        printAction(content=\"test4\"),\n" +
                        "        printAction(content=\"test5\")&,\n" +
                        "        printAction(content=\"test6\"),\n" +
                        "        printAction(content=\"test7\")&,\n" +
                        "        printAction(content=\"test8\"){\n" +
                        "            printAction(content=\"test9\")&,\n" +
                        "            printAction(content=\"test10\"),\n" +
                        "            printAction(content=\"test11\"){\n" +
                        "                printAction(content=\"test12\")&,\n" +
                        "                printAction(content=\"test13\"){\n" +
                        "                    printAction(content=\"test15\")&\n" +
                        "                },\n" +
                        "                printAction(content=\"test14\")\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("output1", RANDOM.nextBoolean())
                    .put("a.b", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);
            promise.sync();

            assertPromise(promise, false, true, true, false);
        }
    }

    @Test
    public void testJoin2() {
        Rule rule = compile(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Promise<ExecutionInstance> promise = startRule(rule, null);
            promise.sync();

            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance = promise.get();
            Assert.assertEquals(1, executionInstance.getLinks().size());
        }
    }

    @Test
    public void testJoinThen1() {
        Rule rule = compile(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        if(setPropertyCondition(name=\"a.b.c\", value=true, output=true)){\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)\n" +
                        "        }else{\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)\n" +
                        "        },\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\")&,\n" +
                        "        printAction(content=\"test4\"),\n" +
                        "        printAction(content=\"test5\")&,\n" +
                        "        printAction(content=\"test6\"),\n" +
                        "        printAction(content=\"test7\")&,\n" +
                        "        printAction(content=\"test8\"){\n" +
                        "            printAction(content=\"test9\")&,\n" +
                        "            printAction(content=\"test10\"),\n" +
                        "            printAction(content=\"test11\"){\n" +
                        "                printAction(content=\"test12\")&,\n" +
                        "                printAction(content=\"test13\"){\n" +
                        "                    printAction(content=\"test15\")&\n" +
                        "                },\n" +
                        "                printAction(content=\"test14\")\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }then{\n" +
                        "        if(setPropertyCondition(name=\"a.b.c\", value=${output1}, output=${output1})){\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)\n" +
                        "        }else{\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("output1", RANDOM.nextBoolean())
                    .put("a.b", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);
            promise.sync();

            assertPromise(promise, false, true, true, false);
        }
    }

    @Test
    public void testJoinThen2() {
        Rule rule = compile(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        if(setPropertyCondition(name=\"a.b.c\", value=false, output=false)){\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)\n" +
                        "        }else{\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)&,\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)&\n" +
                        "        },\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\")&,\n" +
                        "        printAction(content=\"test4\"),\n" +
                        "        printAction(content=\"test5\")&,\n" +
                        "        printAction(content=\"test6\"),\n" +
                        "        printAction(content=\"test7\")&,\n" +
                        "        printAction(content=\"test8\"){\n" +
                        "            printAction(content=\"test9\")&,\n" +
                        "            printAction(content=\"test10\"),\n" +
                        "            printAction(content=\"test11\"){\n" +
                        "                printAction(content=\"test12\")&,\n" +
                        "                printAction(content=\"test13\"){\n" +
                        "                    printAction(content=\"test15\")&\n" +
                        "                },\n" +
                        "                printAction(content=\"test14\")\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }then{\n" +
                        "        printAction(content=\"test1\"),\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\"),\n" +
                        "        printAction(content=\"test4\"),\n" +
                        "        printAction(content=\"test5\"),\n" +
                        "        printAction(content=\"test6\"),\n" +
                        "        printAction(content=\"test7\"),\n" +
                        "        printAction(content=\"test8\"),\n" +
                        "        if(setPropertyCondition(name=\"a.b.c\", value=${output2}, output=${output2})){\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=true)\n" +
                        "        }else{\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false),\n" +
                        "            getPropertyAction(name=\"a.b.c\", expectedValue=false)\n" +
                        "        },\n" +
                        "        printAction(content=\"test1\"),\n" +
                        "        printAction(content=\"test2\"),\n" +
                        "        printAction(content=\"test3\"),\n" +
                        "        printAction(content=\"test4\"),\n" +
                        "        printAction(content=\"test5\"),\n" +
                        "        printAction(content=\"test6\"),\n" +
                        "        printAction(content=\"test7\"),\n" +
                        "        printAction(content=\"test8\")\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Map<String, Object> env = EnvBuilder.builder()
                    .put("output2", RANDOM.nextBoolean())
                    .put("a.b", Maps.newHashMap())
                    .build();

            Promise<ExecutionInstance> promise = startRule(rule, env);
            promise.sync();

            assertPromise(promise, false, true, true, false);
        }
    }

    @Test
    public void testJoinThen3() {
        Rule rule = compile(
                "{\n" +
                        "    join & {\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&,\n" +
                        "        printAction(content=\"test1\")&\n" +
                        "    } then {\n" +
                        "        printAction(content=\"test2\")\n" +
                        "    }\n" +
                        "}"
        );

        for (int i = 0; i < EXECUTE_TIMES; i++) {
            Promise<ExecutionInstance> promise = startRule(rule, null);
            promise.sync();

            assertPromise(promise, false, true, true, false);

            ExecutionInstance executionInstance = promise.get();
            Assert.assertEquals(1, executionInstance.getLinks().size());
            Assert.assertEquals(0, executionInstance.getUnreachableLinks().size());
            Assert.assertEquals(1, executionInstance.getLinks().size());
        }
    }
}
