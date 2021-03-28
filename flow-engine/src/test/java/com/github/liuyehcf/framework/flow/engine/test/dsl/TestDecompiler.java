package com.github.liuyehcf.framework.flow.engine.test.dsl;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.flow.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.flow.engine.dsl.DslDecompiler;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2021/3/25
 */
public class TestDecompiler {

    private Flow compile(String input) {
        System.out.println(input);
        CompileResult<Flow> result = DslCompiler.getInstance().compile(input);

        if (result.getError() != null) {
            result.getError().printStackTrace();
        }
        Assert.assertTrue(result.isSuccess());
        return result.getResult();
    }

    private void testDecompile(String dsl) {
        Flow flow = compile(dsl);

        DslDecompiler dslDecompiler = new DslDecompiler(flow);
        String rebuildDsl = dslDecompiler.decompile();
        System.out.println(rebuildDsl);

        Assert.assertEquals(dsl, rebuildDsl);
    }

    private void testDecompile(String dsl, String expectedRebuildDsl) {
        Flow flow = compile(dsl);

        DslDecompiler dslDecompiler = new DslDecompiler(flow);
        String rebuildDsl = dslDecompiler.decompile();
        System.out.println(rebuildDsl);

        Assert.assertEquals(expectedRebuildDsl, rebuildDsl);
    }

    @Test
    public void testSimpleActionParam() {
        testDecompile("{\n" +
                "    actionA()\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(param1 = 1.1)\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(param1 = 1.1, param2 = \"test\")\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(param1 = 1.1, param2 = \"test\", param3 = ${env.a})\n" +
                "}");
    }

    @Test
    public void testActionParallel() {
        testDecompile("{\n" +
                "    actionA(),\n" +
                "    actionB()\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(),\n" +
                "    actionB(),\n" +
                "    actionC()\n" +
                "}");
    }

    @Test
    public void testActionWithListener() {
        testDecompile("{\n" +
                "    actionA()[listenerA(event = \"before\")]\n" +
                "}");

        testDecompile("{\n" +
                "    actionA()[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)]\n" +
                "}");

        testDecompile("{\n" +
                "    actionA()[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")]\n" +
                "}");
    }

    @Test
    public void testActionCascade() {
        testDecompile("{\n" +
                "    actionA() {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA() {\n" +
                "        actionB() {\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionParam() {
        testDecompile("{\n" +
                "    if(conditionA())\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA(param1 = 1.1))\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA(param1 = 1.1, param2 = \"test\"))\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA(param1 = 1.1, param2 = \"test\", param3 = ${env.a}))\n" +
                "}");
    }

    @Test
    public void testConditionParallel() {
        testDecompile("{\n" +
                "    if(conditionA()),\n" +
                "    if(conditionB())\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()),\n" +
                "    if(conditionB()),\n" +
                "    if(conditionC())\n" +
                "}");
    }

    @Test
    public void testConditionWithListener() {
        testDecompile("{\n" +
                "    if(conditionA()[listenerA(event = \"before\")])\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)])\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")])\n" +
                "}");
    }

    @Test
    public void testConditionCascade() {
        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        if(conditionB())\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        if(conditionB()) {\n" +
                "            if(conditionC())\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        if(conditionB()) {\n" +
                "            if(conditionC())\n" +
                "        }\n" +
                "    } else {\n" +
                "        if(conditionD()) {\n" +
                "            if(conditionE())\n" +
                "        } else {\n" +
                "            if(conditionF())\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testIfElseThenCondition() {
        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        actionA()\n" +
                "    } else {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testGlobalListener() {
        testDecompile("{\n" +
                "    actionA()\n" +
                "}[listenerA(event = \"before\")]");

        testDecompile("{\n" +
                "    actionA()\n" +
                "}[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)]");

        testDecompile("{\n" +
                "    actionA()\n" +
                "}[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")]");
    }

    @Test
    public void testJoin() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionC()&\n" +
                "    },\n" +
                "    actionB()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionB()&,\n" +
                "        actionC()&\n" +
                "    },\n" +
                "    actionA()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");
    }

    @Test
    public void testJoinParallel() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    }\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG()\n" +
                        "}");

        testDecompile("{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    }\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG()\n" +
                        "}");

        testDecompile("{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    }\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG()\n" +
                        "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");

        testDecompile("{\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    actionH(),\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    }\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    actionH()\n" +
                        "}");

        testDecompile("{\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    actionH(),\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    }\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    actionH()\n" +
                        "}");

        testDecompile("{\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionH()\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    actionH()\n" +
                        "}");

        testDecompile("{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    actionH(),\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    }\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    actionH()\n" +
                        "}");

        testDecompile("{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionH()\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    actionH()\n" +
                        "}");

        testDecompile("{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionH()\n" +
                        "}",
                "{\n" +
                        "    join {\n" +
                        "        actionA()&,\n" +
                        "        actionB()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionC()&,\n" +
                        "        actionD()&\n" +
                        "    },\n" +
                        "    join {\n" +
                        "        actionE()&,\n" +
                        "        actionF()&\n" +
                        "    },\n" +
                        "    actionG(),\n" +
                        "    actionH()\n" +
                        "}");
    }

    @Test
    public void testJoinMode() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");

        testDecompile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");

        testDecompile("{\n" +
                "    join | {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");
    }

    @Test
    public void testJoinWithListener() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }[listenerA(event = \"before\")],\n" +
                "    actionB()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)],\n" +
                "    actionB()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")],\n" +
                "    actionB()\n" +
                "}");
    }

    @Test
    public void testJoinWithOnePredecessor() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    },\n" +
                "    actionB()\n" +
                "}");
    }

    @Test
    public void testJoinAction() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()&,\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()&,\n" +
                "            actionC()&\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithSingleIf() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~&,\n" +
                "        if(conditionB())&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionB())&,\n" +
                "        if(conditionA())~&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithIfThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithIfThenElse() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithSub() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~&,\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&,\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithSubThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB(),\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB()& {\n" +
                "                actionC()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB(),\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB(),\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB()& {\n" +
                "                actionC()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB(),\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithSubThenElse() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE() {\n" +
                "                actionF()\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE()& {\n" +
                "                actionF()&\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE() {\n" +
                "                actionF()\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE()& {\n" +
                "                actionF()&\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinWithJoin() {
        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            join {\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionA()&\n" +
                "                    }&\n" +
                "                }&\n" +
                "            }&\n" +
                "        }&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA() {\n" +
                "            join {\n" +
                "                actionB()&\n" +
                "            }&,\n" +
                "            join {\n" +
                "                actionC()&\n" +
                "            }&\n" +
                "        }\n" +
                "    }\n" +
                "}", "{\n" +
                "    actionA() {\n" +
                "        join {\n" +
                "            join {\n" +
                "                actionB()&\n" +
                "            }&,\n" +
                "            join {\n" +
                "                actionC()&\n" +
                "            }&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        actionC()&\n" +
                "    }\n" +
                "}", "{\n" +
                "    join {\n" +
                "        actionC()&\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA() {\n" +
                "        join {\n" +
                "            join {\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionB()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionC()&\n" +
                "                    }&\n" +
                "                }&,\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionD()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionE()&\n" +
                "                    }&\n" +
                "                }&\n" +
                "            }&,\n" +
                "            join {\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionF()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionG()&\n" +
                "                    }&\n" +
                "                }&,\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionH()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionI()&\n" +
                "                    }&\n" +
                "                }&\n" +
                "            }&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }&,\n" +
                "        actionB()&\n" +
                "    }\n" +
                "}", "{\n" +
                "    join {\n" +
                "        actionB()&\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA() {\n" +
                "            join {\n" +
                "                actionB()&\n" +
                "            }&\n" +
                "        },\n" +
                "        actionC()&\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            join {\n" +
                "                actionA()&,\n" +
                "                actionB()&\n" +
                "            }&\n" +
                "        }&,\n" +
                "        join {\n" +
                "            join {\n" +
                "                actionC()&,\n" +
                "                actionD()&\n" +
                "            }&\n" +
                "        }&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinComplex() {
        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        join {\n" +
                "            actionA()&,\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    } else {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        actionA()\n" +
                "    } else {\n" +
                "        join {\n" +
                "            actionB()&,\n" +
                "            actionC()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA()) {\n" +
                "            actionA(),\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        actionC()&\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA()) {\n" +
                "            join {\n" +
                "                if(conditionA())&,\n" +
                "                actionC()&\n" +
                "            },\n" +
                "            actionA(),\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        actionE()&\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");
    }

    @Test
    public void testJoinOverlap() {
        testDecompile("\n" +
                "{\n" +
                "    join {\n" +
                "        if(conditionA())&,\n" +
                "        actionA() {\n" +
                "            actionB()\n" +
                "        },\n" +
                "        join {\n" +
                "            if(conditionB())&,\n" +
                "            actionB() {\n" +
                "                actionC() {\n" +
                "                    actionD()&\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        actionE() {\n" +
                "            actionF()&\n" +
                "        }\n" +
                "    }\n" +
                "}", "{\n" +
                "    join {\n" +
                "        if(conditionA())&,\n" +
                "        actionE() {\n" +
                "            actionF()&\n" +
                "        }\n" +
                "    },\n" +
                "    join {\n" +
                "        if(conditionB())&,\n" +
                "        actionB() {\n" +
                "            actionC() {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    actionA() {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinSelect() {
        testDecompile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(conditionA())&,\n" +
                "            if(conditionB())&\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinSubThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    },\n" +
                "    actionB()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        join {\n" +
                "            actionA()&,\n" +
                "            actionB()&\n" +
                "        } then {\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } else {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    if(conditionA()) {\n" +
                "        actionA()\n" +
                "    } else {\n" +
                "        join {\n" +
                "            actionB()&,\n" +
                "            actionC()&\n" +
                "        } then {\n" +
                "            actionD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionB()&,\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    },\n" +
                "    actionA()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");
    }

    @Test
    public void testJoinThenParallel() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG(),\n" +
                "    actionH()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG(),\n" +
                "    actionH()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG(),\n" +
                "    actionH()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG(),\n" +
                "    actionH()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG(),\n" +
                "    actionH()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionC()&,\n" +
                "        actionD()&\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        actionE()&,\n" +
                "        actionF()&\n" +
                "    } then {\n" +
                "        actionZ()\n" +
                "    },\n" +
                "    actionG(),\n" +
                "    actionH()\n" +
                "}");
    }

    @Test
    public void testJoinThenMode() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");

        testDecompile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");

        testDecompile("{\n" +
                "    join | {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");
    }

    @Test
    public void testJoinThenWithListener() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }[listenerA(event = \"before\")] then {\n" +
                "        actionB()\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)] then {\n" +
                "        actionB()\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")] then {\n" +
                "        actionB()\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");
    }

    @Test
    public void testJoinThenWithOnePredecessor() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");
    }

    @Test
    public void testJoinThenAction() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()&,\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()& {\n" +
                "            actionB()&,\n" +
                "            actionC()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithSingleIf() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~&,\n" +
                "        if(conditionB())&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionB())&,\n" +
                "        if(conditionA())~&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithIfThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA()& {\n" +
                "                actionB()&\n" +
                "            },\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithIfThenElse() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA())~& {\n" +
                "            actionA() {\n" +
                "                actionB()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionC(),\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithSub() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~&,\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }&,\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~&\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithSubThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB(),\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB()& {\n" +
                "                actionC()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB(),\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB(),\n" +
                "            actionC() {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB()& {\n" +
                "                actionC()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB(),\n" +
                "            actionC()& {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithSubThenElse() {
        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE() {\n" +
                "                actionF()\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE()& {\n" +
                "                actionF()&\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD() {\n" +
                "                actionE()\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE() {\n" +
                "                actionF()\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD()& {\n" +
                "                actionE()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        sub {\n" +
                "            actionA()\n" +
                "        }~& then {\n" +
                "            actionB() {\n" +
                "                actionC()\n" +
                "            }\n" +
                "        } else {\n" +
                "            actionD(),\n" +
                "            actionE()& {\n" +
                "                actionF()&\n" +
                "            },\n" +
                "            actionG()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenWithJoinThen() {
        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }&\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionC()\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            join {\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionA()&\n" +
                "                    }& then {\n" +
                "                        actionB()\n" +
                "                    }\n" +
                "                }& then {\n" +
                "                    actionD()&\n" +
                "                }\n" +
                "            }&\n" +
                "        }& then {\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA() {\n" +
                "            join {\n" +
                "                actionB()&\n" +
                "            }& then {\n" +
                "                actionD()&\n" +
                "            },\n" +
                "            join {\n" +
                "                actionC()&\n" +
                "            }&\n" +
                "        }\n" +
                "    }\n" +
                "}", "{\n" +
                "    actionA() {\n" +
                "        join {\n" +
                "            join {\n" +
                "                actionB()&\n" +
                "            }& then {\n" +
                "                actionD()&\n" +
                "            },\n" +
                "            join {\n" +
                "                actionC()&\n" +
                "            }&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}", "{\n" +
                "    join {\n" +
                "        actionC()&\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }& then {\n" +
                "            actionB()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA() {\n" +
                "        join {\n" +
                "            join {\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionB()&\n" +
                "                    }& then {\n" +
                "                        actionX()\n" +
                "                    },\n" +
                "                    join {\n" +
                "                        actionC()&\n" +
                "                    }&\n" +
                "                }&,\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionD()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionE()&\n" +
                "                    }& then {\n" +
                "                        actionY()\n" +
                "                    }\n" +
                "                }& then {\n" +
                "                    actionZ()\n" +
                "                }\n" +
                "            }&,\n" +
                "            join {\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionF()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionG()&\n" +
                "                    }&\n" +
                "                }&,\n" +
                "                join {\n" +
                "                    join {\n" +
                "                        actionH()&\n" +
                "                    }&,\n" +
                "                    join {\n" +
                "                        actionI()&\n" +
                "                    }&\n" +
                "                }&\n" +
                "            }& then {\n" +
                "                actionP()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}", "{\n" +
                "    join {\n" +
                "        actionB()&\n" +
                "        join {\n" +
                "            actionA()&\n" +
                "        }&\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA() {\n" +
                "            join {\n" +
                "                actionB()&\n" +
                "            }&\n" +
                "        },\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        join {\n" +
                "            join {\n" +
                "                actionA()&,\n" +
                "                actionB()&\n" +
                "            }& then {\n" +
                "                actionX()\n" +
                "            }\n" +
                "        }&,\n" +
                "        join {\n" +
                "            join {\n" +
                "                actionC()&,\n" +
                "                actionD()&\n" +
                "            }& then {\n" +
                "                actionY()&\n" +
                "            }\n" +
                "        }&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenComplex() {
        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA()) {\n" +
                "            actionA(),\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    },\n" +
                "    actionE()\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        if(conditionA()) {\n" +
                "            join {\n" +
                "                if(conditionA())&,\n" +
                "                actionC()&\n" +
                "            } then {\n" +
                "                actionD()\n" +
                "            },\n" +
                "            actionA(),\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        actionE()&\n" +
                "    } then {\n" +
                "        actionF()\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");
    }

    @Test
    public void testJoinThenOverlap() {
        testDecompile("\n" +
                "{\n" +
                "    join {\n" +
                "        if(conditionA())&,\n" +
                "        actionA() {\n" +
                "            actionB()\n" +
                "        },\n" +
                "        join {\n" +
                "            if(conditionB())&,\n" +
                "            actionB() {\n" +
                "                actionC() {\n" +
                "                    actionD()&\n" +
                "                }\n" +
                "            }\n" +
                "        } then {\n" +
                "            actionX()\n" +
                "        },\n" +
                "        actionE() {\n" +
                "            actionF()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    }\n" +
                "}", "{\n" +
                "    join {\n" +
                "        if(conditionA())&,\n" +
                "        actionE() {\n" +
                "            actionF()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionY()\n" +
                "    },\n" +
                "    join {\n" +
                "        if(conditionB())&,\n" +
                "        actionB() {\n" +
                "            actionC() {\n" +
                "                actionD()&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionX()\n" +
                "    },\n" +
                "    actionA() {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenSelect() {
        testDecompile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(conditionA())&,\n" +
                "            if(conditionB())&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionA()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testJoinThenCascade() {
        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        join {\n" +
                "            actionC()&,\n" +
                "            actionD()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB() {\n" +
                "            actionC()&,\n" +
                "            actionD(),\n" +
                "            actionE() {\n" +
                "                actionF(),\n" +
                "                actionG()&\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionA()) {\n" +
                "            actionH(),\n" +
                "            actionI() {\n" +
                "                actionJ() {\n" +
                "                    actionK()&\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        join {\n" +
                "            actionC()&,\n" +
                "            actionD()&\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSelect() {
        testDecompile("{\n" +
                "    select {\n" +
                "        if(conditionA()),\n" +
                "        if(conditionB())\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(),\n" +
                "    select {\n" +
                "        if(conditionA()),\n" +
                "        if(conditionB())\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    select {\n" +
                "        if(conditionA()),\n" +
                "        if(conditionB())\n" +
                "    },\n" +
                "    actionA()\n" +
                "}");

        testDecompile("{\n" +
                "    select {\n" +
                "        if(conditionA()) {\n" +
                "            actionA()\n" +
                "        },\n" +
                "        if(conditionB())\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    join {\n" +
                "        select {\n" +
                "            if(conditionA()) {\n" +
                "                actionA()&\n" +
                "            },\n" +
                "            if(conditionB())\n" +
                "        },\n" +
                "        actionB()&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSelectWithListener() {
        testDecompile("{\n" +
                "    select {\n" +
                "        if(conditionA()),\n" +
                "        if(conditionB())\n" +
                "    }[listenerA(event = \"before\")]\n" +
                "}");

        testDecompile("{\n" +
                "    select {\n" +
                "        if(conditionA()),\n" +
                "        if(conditionB())\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)]\n" +
                "}");

        testDecompile("{\n" +
                "    select {\n" +
                "        if(conditionA()),\n" +
                "        if(conditionB())\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")]\n" +
                "}");
    }

    @Test
    public void testSub() {
        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(),\n" +
                "    sub {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    },\n" +
                "    actionB()\n" +
                "}");
    }

    @Test
    public void testSubThen() {
        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(),\n" +
                "    sub {\n" +
                "        actionB()\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");
    }

    @Test
    public void testSubThenElse() {
        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    } else {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    actionA(),\n" +
                "    sub {\n" +
                "        actionB()\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    } else {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    } else {\n" +
                "        actionC()\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");
    }

    @Test
    public void testSubWithListener() {
        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\")]\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)]\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")]\n" +
                "}");
    }

    @Test
    public void testSubThenWithListener() {
        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\")] then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)] then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")] then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSubThenElseWithListener() {
        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\")] then {\n" +
                "        actionB()\n" +
                "    } else {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1)] then {\n" +
                "        actionB()\n" +
                "    } else {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        testDecompile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event = \"before\"), listenerB(event = \"success\", param = 1), listenerC(event = \"failure\", param1 = 1, param2 = \"test\")] then {\n" +
                "        actionB()\n" +
                "    } else {\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");
    }
}
