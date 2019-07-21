package com.github.liuyehcf.framework.rule.engine.test.dsl;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.rule.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.rule.engine.model.*;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.model.activity.Activity;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
@SuppressWarnings("all")
public class TestCompileResult {

    private Rule compile(String input) {
        System.out.println(input);
        CompileResult<Rule> result = DslCompiler.getInstance().compile(input);

        if (result.getError() != null) {
            result.getError().printStackTrace();
        }
        Assert.assertTrue(result.isSuccess());

        return result.getResult();
    }

    @Test
    public void testCascade1() {
        Rule rule = compile("{\n" +
                "    actionA(arg1=1.0, arg2=\"test\", arg3=${a.b.c}){\n" +
                "        actionB(arg1=1.0, arg2=\"test\", arg3=${a.b})\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 3, 1, 0, 0, start);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 3, 0, 0, 0, actionA);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 3, null);
    }

    @Test
    public void testCascade2() {
        Rule rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(),\n" +
                "                actionE()\n" +
                "            },\n" +
                "            actionF(){\n" +
                "                actionG()\n" +
                "            }\n" +
                "        },\n" +
                "        actionH(){\n" +
                "            actionI(),\n" +
                "            actionJ(){\n" +
                "                actionK()\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    actionL(),\n" +
                "    actionM(){\n" +
                "        actionN()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 3, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, start);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 2, 0, 0, actionA);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 2, 0, 0, actionB);

        Action actionD = (Action) actionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, actionC);

        Action actionE = (Action) actionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, actionC);

        Action actionF = (Action) actionB.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, actionB);

        Action actionG = (Action) actionF.getSuccessors().get(0);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, actionF);

        Action actionH = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 2, 0, 0, actionA);

        Action actionI = (Action) actionH.getSuccessors().get(0);
        assertActivity(actionI, LinkType.NORMAL, "actionI", 0, 0, 0, 0, actionH);

        Action actionJ = (Action) actionH.getSuccessors().get(1);
        assertActivity(actionJ, LinkType.NORMAL, "actionJ", 0, 1, 0, 0, actionH);

        Action actionK = (Action) actionJ.getSuccessors().get(0);
        assertActivity(actionK, LinkType.NORMAL, "actionK", 0, 0, 0, 0, actionJ);

        Action actionL = (Action) start.getSuccessors().get(1);
        assertActivity(actionL, LinkType.NORMAL, "actionL", 0, 0, 0, 0, start);

        Action actionM = (Action) start.getSuccessors().get(2);
        assertActivity(actionM, LinkType.NORMAL, "actionM", 0, 1, 0, 0, start);

        Action actionN = (Action) actionM.getSuccessors().get(0);
        assertActivity(actionN, LinkType.NORMAL, "actionN", 0, 0, 0, 0, actionM);

        assertRule(rule, LinkType.NORMAL, 0, 7, 0, 0, 15, null);
    }

    @Test
    public void testParallel1() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    actionB()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 3, null);
    }

    @Test
    public void testCascadeParallel1() {
        Rule rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(),\n" +
                "        actionC()\n" +
                "    },\n" +
                "    actionD(){\n" +
                "        actionE(),\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, start);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, actionA);

        Action actionC = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, actionA);

        Action actionD = (Action) start.getSuccessors().get(1);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 2, 0, 0, start);

        Action actionE = (Action) actionD.getSuccessors().get(0);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, actionD);

        Action actionF = (Action) actionD.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 0, 0, 0, actionD);

        assertRule(rule, LinkType.NORMAL, 0, 4, 0, 0, 7, null);
    }

    @Test
    public void testCondition1() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 3, null);
    }

    @Test
    public void testCondition2() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 2, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Action actionB = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 4, null);
    }

    @Test
    public void testCondition3() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA()\n" +
                "    }else{\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 2, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Action actionB = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionB, LinkType.FALSE, "actionB", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 4, null);
    }

    @Test
    public void testCondition4() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        actionB(),\n" +
                "        actionC()\n" +
                "    }else{\n" +
                "        actionD(),\n" +
                "        actionE(),\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 6, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Action actionB = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        Action actionC = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionA);

        Action actionD = (Action) conditionA.getSuccessors().get(3);
        assertActivity(actionD, LinkType.FALSE, "actionD", 0, 0, 0, 0, conditionA);

        Action actionE = (Action) conditionA.getSuccessors().get(4);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 0, 0, 0, conditionA);

        Action actionF = (Action) conditionA.getSuccessors().get(5);
        assertActivity(actionF, LinkType.FALSE, "actionF", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 6, 0, 0, 8, null);
    }

    @Test
    public void testCascadeCondition1() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionB(),\n" +
                "        if(conditionC()){\n" +
                "            actionD(),\n" +
                "            actionE()\n" +
                "        },\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, start);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        Condition conditionC = (Condition) conditionA.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.TRUE, "conditionC", 0, 2, 0, 0, conditionA);

        Action actionD = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionC);

        Action actionE = (Action) conditionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionC);

        Action actionF = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionF, LinkType.TRUE, "actionF", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 4, 0, 0, 7, null);
    }

    @Test
    public void testCascadeCondition2() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionB(),\n" +
                "        if(conditionC()){\n" +
                "            actionD(),\n" +
                "            actionE()\n" +
                "        }else{\n" +
                "            actionF(),\n" +
                "            actionG()\n" +
                "        },\n" +
                "        actionH()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, start);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        Condition conditionC = (Condition) conditionA.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.TRUE, "conditionC", 0, 4, 0, 0, conditionA);

        Action actionD = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionC);

        Action actionE = (Action) conditionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionC);

        Action actionF = (Action) conditionC.getSuccessors().get(2);
        assertActivity(actionF, LinkType.FALSE, "actionF", 0, 0, 0, 0, conditionC);

        Action actionG = (Action) conditionC.getSuccessors().get(3);
        assertActivity(actionG, LinkType.FALSE, "actionG", 0, 0, 0, 0, conditionC);

        Action actionH = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionH, LinkType.TRUE, "actionH", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 6, 0, 0, 9, null);
    }

    @Test
    public void testCascadeCondition3() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionB(),\n" +
                "        if(conditionC()){\n" +
                "            actionD()\n" +
                "        }else{\n" +
                "            actionE(),\n" +
                "            if(conditionF()){\n" +
                "                actionG()\n" +
                "            }\n" +
                "        },\n" +
                "        actionH()\n" +
                "    }else{\n" +
                "        if(conditionI()){\n" +
                "            if(conditionJ()){\n" +
                "                actionK()\n" +
                "            }else{\n" +
                "                actionL()\n" +
                "            },\n" +
                "            actionM()\n" +
                "        }\n" +
                "    },\n" +
                "    actionN()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 4, 0, 0, start);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        Condition conditionC = (Condition) conditionA.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.TRUE, "conditionC", 0, 3, 0, 0, conditionA);

        Action actionD = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionC);

        Action actionE = (Action) conditionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 0, 0, 0, conditionC);

        Condition conditionF = (Condition) conditionC.getSuccessors().get(2);
        assertActivity(conditionF, LinkType.FALSE, "conditionF", 0, 1, 0, 0, conditionC);

        Action actionG = (Action) conditionF.getSuccessors().get(0);
        assertActivity(actionG, LinkType.TRUE, "actionG", 0, 0, 0, 0, conditionF);

        Action actionH = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionH, LinkType.TRUE, "actionH", 0, 0, 0, 0, conditionA);

        Condition conditionI = (Condition) conditionA.getSuccessors().get(3);
        assertActivity(conditionI, LinkType.FALSE, "conditionI", 0, 2, 0, 0, conditionA);

        Condition conditionJ = (Condition) conditionI.getSuccessors().get(0);
        assertActivity(conditionJ, LinkType.TRUE, "conditionJ", 0, 2, 0, 0, conditionI);

        Action actionK = (Action) conditionJ.getSuccessors().get(0);
        assertActivity(actionK, LinkType.TRUE, "actionK", 0, 0, 0, 0, conditionJ);

        Action actionL = (Action) conditionJ.getSuccessors().get(1);
        assertActivity(actionL, LinkType.FALSE, "actionL", 0, 0, 0, 0, conditionJ);

        Action actionM = (Action) conditionI.getSuccessors().get(1);
        assertActivity(actionM, LinkType.TRUE, "actionM", 0, 0, 0, 0, conditionI);

        Action actionN = (Action) start.getSuccessors().get(1);
        assertActivity(actionN, LinkType.NORMAL, "actionN", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 9, 0, 0, 15, null);
    }

    @Test
    public void testJoinThen1() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        actionA()&\n" +
                "    }then{\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 1, 1, 0, actionA);

        Action actionB = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, joinGateway);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 4, null);
    }

    @Test
    public void testJoinThen2() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()\n" +
                "    }then{\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 1, 1, 0, actionA);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 5, null);
    }

    @Test
    public void testJoinThen3() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    }then{\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 0, actionA, actionB);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 5, null);
    }

    @Test
    public void testJoinThen4() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        if(conditionA()){\n" +
                "            actionB()&,\n" +
                "            actionC()\n" +
                "        }else{\n" +
                "            actionD(),\n" +
                "            actionE()&\n" +
                "        }\n" +
                "    }then{\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 4, 0, 0, start);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 0, 0, conditionA);

        Action actionC = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionA);

        Action actionD = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionD, LinkType.FALSE, "actionD", 0, 0, 0, 0, conditionA);

        Action actionE = (Action) conditionA.getSuccessors().get(3);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 1, 0, 0, conditionA);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 0, actionB, actionE);

        Action actionF = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 0, 0, 0, joinGateway);

        assertRule(rule, LinkType.NORMAL, 0, 3, 0, 0, 8, null);
    }

    @Test
    public void testJoinThen5() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()\n" +
                "    } [listenerC(event=\"start\")] then {\n" +
                "        actionD(),\n" +
                "        if(conditionE()){\n" +
                "            actionF()\n" +
                "        }else{\n" +
                "            actionG()\n" +
                "        },\n" +
                "        actionH()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 1, 3, 1, actionA);

        Action actionD = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, joinGateway);

        Condition conditionE = (Condition) joinGateway.getSuccessors().get(1);
        assertActivity(conditionE, LinkType.NORMAL, "conditionE", 0, 2, 0, 0, joinGateway);

        Action actionF = (Action) conditionE.getSuccessors().get(0);
        assertActivity(actionF, LinkType.TRUE, "actionF", 0, 0, 0, 0, conditionE);

        Action actionG = (Action) conditionE.getSuccessors().get(1);
        assertActivity(actionG, LinkType.FALSE, "actionG", 0, 0, 0, 0, conditionE);

        Action actionH = (Action) joinGateway.getSuccessors().get(2);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, joinGateway);

        assertRule(rule, LinkType.NORMAL, 0, 5, 1, 0, 10, null);
    }

    @Test
    public void testJoinThen6() {
        Rule rule = compile("{\n" +
                "    join & {\n" +
                "        actionA(){\n" +
                "            actionB()&\n" +
                "        },\n" +
                "        join & {\n" +
                "            actionC(){\n" +
                "                actionD(),\n" +
                "                actionE()&\n" +
                "            },\n" +
                "            actionF(){\n" +
                "                actionG()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            actionH()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionI()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 3, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, actionA);

        Action actionC = (Action) start.getSuccessors().get(1);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 2, 0, 0, start);

        Action actionD = (Action) actionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, actionC);

        Action actionE = (Action) actionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 1, 0, 0, actionC);

        Action actionF = (Action) start.getSuccessors().get(2);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, start);

        Action actionG = (Action) actionF.getSuccessors().get(0);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 1, 0, 0, actionF);

        JoinGateway gateway1 = (JoinGateway) actionE.getSuccessors().get(0);
        assertJoinGateway(gateway1, 2, 1, 0, actionE, actionG);

        Action actionH = (Action) gateway1.getSuccessors().get(0);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 1, 0, 0, gateway1);

        JoinGateway gateway2 = (JoinGateway) actionH.getSuccessors().get(0);
        assertJoinGateway(gateway2, 2, 1, 0, actionB, actionH);

        Action actionI = (Action) gateway2.getSuccessors().get(0);
        assertActivity(actionI, LinkType.NORMAL, "actionI", 0, 0, 0, 0, gateway2);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 12, null);
    }

    @Test
    public void testSelect1() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionA()\n" +
                "        },\n" +
                "        if(conditionB()){\n" +
                "            actionB()\n" +
                "        }\n" +
                "    } [listenerA(event=\"start\")]\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) start.getSuccessors().get(0);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 2, 1, start);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, exclusiveGateway);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Condition conditionB = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway);

        Action actionB = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionB);

        Listener listenerA = exclusiveGateway.getListeners().get(0);
        assertListener(listenerA, exclusiveGateway, "listenerA", ListenerEvent.start, 1);

        assertRule(rule, LinkType.NORMAL, 0, 2, 1, 0, 7, null);
    }

    @Test
    public void testSelect2() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB()\n" +
                "        },\n" +
                "        if(conditionB()){\n" +
                "            actionC()\n" +
                "        }\n" +
                "    },\n" +
                "    actionD()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 3, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) start.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 2, 0, start);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, exclusiveGateway);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        Condition conditionB = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway);

        Action actionC = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionB);

        Action actionD = (Action) start.getSuccessors().get(2);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 4, 0, 0, 8, null);
    }

    @Test
    public void testSelect3() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB(),\n" +
                "            if(conditionB()){\n" +
                "                actionC()\n" +
                "            }else{\n" +
                "                actionD()\n" +
                "            },\n" +
                "            if(conditionC()){\n" +
                "                if(conditionD()){\n" +
                "                    actionE()\n" +
                "                }else{\n" +
                "                    actionF()\n" +
                "                },\n" +
                "                actionG()\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionE()){\n" +
                "            if(conditionF()){\n" +
                "                actionI()\n" +
                "            }else{\n" +
                "                if(conditionG()){\n" +
                "                    actionJ()\n" +
                "                }else{\n" +
                "                    if(conditionH()){\n" +
                "                        actionK(),\n" +
                "                        if(conditionI()){\n" +
                "                            actionL()\n" +
                "                        }else{\n" +
                "                            actionM()\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                actionN()\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    actionO()\n" +
                "}\n");

        Start start = rule.getStart();
        assertStart(start, 3, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) start.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 2, 0, start);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, exclusiveGateway);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 2, 0, 0, conditionA);

        Action actionC = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionB);

        Action actionD = (Action) conditionB.getSuccessors().get(1);
        assertActivity(actionD, LinkType.FALSE, "actionD", 0, 0, 0, 0, conditionB);

        Condition conditionC = (Condition) conditionA.getSuccessors().get(2);
        assertActivity(conditionC, LinkType.TRUE, "conditionC", 0, 2, 0, 0, conditionA);

        Condition conditionD = (Condition) conditionC.getSuccessors().get(0);
        assertActivity(conditionD, LinkType.TRUE, "conditionD", 0, 2, 0, 0, conditionC);

        Action actionE = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionD);

        Action actionF = (Action) conditionD.getSuccessors().get(1);
        assertActivity(actionF, LinkType.FALSE, "actionF", 0, 0, 0, 0, conditionD);

        Action actionG = (Action) conditionC.getSuccessors().get(1);
        assertActivity(actionG, LinkType.TRUE, "actionG", 0, 0, 0, 0, conditionC);

        Condition conditionE = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionE, LinkType.NORMAL, "conditionE", 0, 1, 0, 0, exclusiveGateway);

        Condition conditionF = (Condition) conditionE.getSuccessors().get(0);
        assertActivity(conditionF, LinkType.TRUE, "conditionF", 0, 3, 0, 0, conditionE);

        Action actionI = (Action) conditionF.getSuccessors().get(0);
        assertActivity(actionI, LinkType.TRUE, "actionI", 0, 0, 0, 0, conditionF);

        Condition conditionG = (Condition) conditionF.getSuccessors().get(1);
        assertActivity(conditionG, LinkType.FALSE, "conditionG", 0, 2, 0, 0, conditionF);

        Action actionJ = (Action) conditionG.getSuccessors().get(0);
        assertActivity(actionJ, LinkType.TRUE, "actionJ", 0, 0, 0, 0, conditionG);

        Condition conditionH = (Condition) conditionG.getSuccessors().get(1);
        assertActivity(conditionH, LinkType.FALSE, "conditionH", 0, 2, 0, 0, conditionG);

        Action actionK = (Action) conditionH.getSuccessors().get(0);
        assertActivity(actionK, LinkType.TRUE, "actionK", 0, 0, 0, 0, conditionH);

        Condition conditionI = (Condition) conditionH.getSuccessors().get(1);
        assertActivity(conditionI, LinkType.TRUE, "conditionI", 0, 2, 0, 0, conditionH);

        Action actionL = (Action) conditionI.getSuccessors().get(0);
        assertActivity(actionL, LinkType.TRUE, "actionL", 0, 0, 0, 0, conditionI);

        Action actionM = (Action) conditionI.getSuccessors().get(1);
        assertActivity(actionM, LinkType.FALSE, "actionM", 0, 0, 0, 0, conditionI);

        Action actionN = (Action) conditionF.getSuccessors().get(2);
        assertActivity(actionN, LinkType.FALSE, "actionN", 0, 0, 0, 0, conditionF);

        Action actionO = (Action) start.getSuccessors().get(2);
        assertActivity(actionO, LinkType.NORMAL, "actionO", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 14, 0, 0, 25, null);
    }

    @Test
    public void testSelect4() {
        Rule rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(),\n" +
                "            select{\n" +
                "                if(conditionA()){\n" +
                "                    actionD()\n" +
                "                },\n" +
                "                if(conditionB()){\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }[listenerA(event=\"start\")],\n" +
                "            actionF()\n" +
                "        },\n" +
                "        actionG()\n" +
                "    },\n" +
                "    actionH()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, start);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 3, 0, 0, actionA);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, actionB);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) actionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 2, 1, actionB);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, exclusiveGateway);

        Action actionD = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionA);

        Condition conditionB = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway);

        Action actionE = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionB);

        Action actionF = (Action) actionB.getSuccessors().get(2);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 0, 0, 0, actionB);

        Action actionG = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, actionA);

        Action actionH = (Action) start.getSuccessors().get(1);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 6, 1, 0, 13, null);
    }

    @Test
    public void testSelect5() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        select {\n" +
                "            if(conditionB()){\n" +
                "                actionA()\n" +
                "            },\n" +
                "            if(conditionC()){\n" +
                "                actionB()\n" +
                "            }\n" +
                "        }\n" +
                "    }else{\n" +
                "        select {\n" +
                "            if(conditionD()){\n" +
                "                actionC()\n" +
                "            }\n" +
                "        }[listenerA(event=\"end\")]\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 2, 0, 0, start);

        ExclusiveGateway exclusiveGateway1 = (ExclusiveGateway) conditionA.getSuccessors().get(0);
        assertExclusiveGateway(exclusiveGateway1, LinkType.TRUE, 2, 0, conditionA);

        Condition conditionB = (Condition) exclusiveGateway1.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionB);

        Condition conditionC = (Condition) exclusiveGateway1.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway1);

        Action actionB = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionC);

        ExclusiveGateway exclusiveGateway2 = (ExclusiveGateway) conditionA.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway2, LinkType.FALSE, 1, 1, conditionA);

        Condition conditionD = (Condition) exclusiveGateway2.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway1);

        Action actionC = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionD);

        Listener listenerA = exclusiveGateway2.getListeners().get(0);
        assertListener(listenerA, exclusiveGateway2, "listenerA", ListenerEvent.end, 1);

        assertRule(rule, LinkType.NORMAL, 0, 3, 1, 0, 11, null);
    }

    @Test
    public void testSelect6() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    join & {\n" +
                "        actionB()&,\n" +
                "        select{\n" +
                "            if(conditionA()){\n" +
                "                actionC()&\n" +
                "            },\n" +
                "            if(conditionB()){\n" +
                "                actionD()&\n" +
                "            },\n" +
                "            if(conditionC()){\n" +
                "                actionE()&\n" +
                "            }\n" +
                "        }[listenerA(event=\"start\")],\n" +
                "        actionF()&,\n" +
                "        actionG()\n" +
                "    }[listenerB(event=\"start\")],\n" +
                "    actionH()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 6, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, start);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) start.getSuccessors().get(2);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 3, 1, start);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, exclusiveGateway);

        Action actionC = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 1, 0, 0, conditionA);

        Condition conditionB = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway);

        Action actionD = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 1, 0, 0, conditionB);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(2);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Action actionE = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 1, 0, 0, conditionC);

        Listener listenerA = exclusiveGateway.getListeners().get(0);
        assertListener(listenerA, exclusiveGateway, "listenerA", ListenerEvent.start, 1);

        Action actionF = (Action) start.getSuccessors().get(3);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, start);

        Action actionG = (Action) start.getSuccessors().get(4);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 5, 0, 1, actionB, actionC, actionD, actionE, actionF);

        Listener listenerB = joinGateway.getListeners().get(0);
        assertListener(listenerB, joinGateway, "listenerB", ListenerEvent.start, 1);

        Action actionH = (Action) start.getSuccessors().get(5);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 4, 2, 0, 16, null);
    }

    @Test
    public void testSelect7() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    select{\n" +
                "        if(conditionA()){\n" +
                "            join & {\n" +
                "                actionB()&,\n" +
                "                if(conditionB()){\n" +
                "                    actionC()&\n" +
                "                },\n" +
                "                actionD()\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionC()){\n" +
                "            actionE()\n" +
                "        },\n" +
                "        if(conditionD()){\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }[listenerA(event=\"start\")],\n" +
                "    actionG()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 3, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) start.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 3, 1, start);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, exclusiveGateway);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 0, 0, conditionA);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 1, 0, 0, conditionA);

        Action actionC = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 1, 0, 0, conditionB);

        Action actionD = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionA);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 0, 0, actionB, actionC);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Action actionE = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionC);

        Condition conditionD = (Condition) exclusiveGateway.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.NORMAL, "conditionD", 0, 1, 0, 0, exclusiveGateway);

        Action actionF = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionF, LinkType.TRUE, "actionF", 0, 0, 0, 0, conditionD);

        Listener listenerA = exclusiveGateway.getListeners().get(0);
        assertListener(listenerA, exclusiveGateway, "listenerA", ListenerEvent.start, 1);

        Action actionG = (Action) start.getSuccessors().get(2);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 6, 1, 0, 15, null);
    }

    @Test
    public void testSelect8() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    join & {\n" +
                "        actionB()&,\n" +
                "        select{\n" +
                "            if(conditionA()){\n" +
                "                actionC()&\n" +
                "            },\n" +
                "            if(conditionB()){\n" +
                "                actionD()&\n" +
                "            },\n" +
                "            if(conditionC()){\n" +
                "                actionE()&\n" +
                "            }\n" +
                "        }[listenerA(event=\"start\")],\n" +
                "        actionF()&,\n" +
                "        actionG()\n" +
                "    }[listenerB(event=\"start\")] then{\n" +
                "        actionH(),\n" +
                "        select{\n" +
                "            if(conditionD()){\n" +
                "                actionI()\n" +
                "            },\n" +
                "            if(conditionE()){\n" +
                "                actionJ()\n" +
                "            },\n" +
                "            if(conditionF()){\n" +
                "                actionK()\n" +
                "            }\n" +
                "        }[listenerC(event=\"start\")]\n" +
                "    },\n" +
                "    actionL()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 6, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, start);

        ExclusiveGateway exclusiveGateway1 = (ExclusiveGateway) start.getSuccessors().get(2);
        assertExclusiveGateway(exclusiveGateway1, LinkType.NORMAL, 3, 1, start);

        Condition conditionA = (Condition) exclusiveGateway1.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, exclusiveGateway1);

        Action actionC = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 1, 0, 0, conditionA);

        Condition conditionB = (Condition) exclusiveGateway1.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 1, 0, 0, exclusiveGateway1);

        Action actionD = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 1, 0, 0, conditionB);

        Condition conditionC = (Condition) exclusiveGateway1.getSuccessors().get(2);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway1);

        Action actionE = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 1, 0, 0, conditionC);

        Listener listenerA = exclusiveGateway1.getListeners().get(0);
        assertListener(listenerA, exclusiveGateway1, "listenerA", ListenerEvent.start, 1);

        Action actionF = (Action) start.getSuccessors().get(3);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, start);

        Action actionG = (Action) start.getSuccessors().get(4);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 5, 2, 1, actionB, actionC, actionD, actionE, actionF);

        Listener listenerB = joinGateway.getListeners().get(0);
        assertListener(listenerB, joinGateway, "listenerB", ListenerEvent.start, 1);

        Action actionH = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, joinGateway);

        ExclusiveGateway exclusiveGateway2 = (ExclusiveGateway) joinGateway.getSuccessors().get(1);

        Condition conditionD = (Condition) exclusiveGateway2.getSuccessors().get(0);
        assertActivity(conditionD, LinkType.NORMAL, "conditionD", 0, 1, 0, 0, exclusiveGateway2);

        Action actionI = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionI, LinkType.TRUE, "actionI", 0, 0, 0, 0, conditionD);

        Condition conditionE = (Condition) exclusiveGateway2.getSuccessors().get(1);
        assertActivity(conditionE, LinkType.NORMAL, "conditionE", 0, 1, 0, 0, exclusiveGateway2);

        Action actionJ = (Action) conditionE.getSuccessors().get(0);
        assertActivity(actionJ, LinkType.TRUE, "actionJ", 0, 0, 0, 0, conditionE);

        Condition conditionF = (Condition) exclusiveGateway2.getSuccessors().get(2);
        assertActivity(conditionF, LinkType.NORMAL, "conditionF", 0, 1, 0, 0, exclusiveGateway2);

        Action actionK = (Action) conditionF.getSuccessors().get(0);
        assertActivity(actionK, LinkType.TRUE, "actionK", 0, 0, 0, 0, conditionF);

        Listener listenerC = exclusiveGateway2.getListeners().get(0);
        assertListener(listenerC, exclusiveGateway2, "listenerC", ListenerEvent.start, 1);

        Action actionL = (Action) start.getSuccessors().get(5);
        assertActivity(actionL, LinkType.NORMAL, "actionL", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 7, 3, 0, 25, null);
    }

    @Test
    public void testSelect9() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    select{\n" +
                "        if(conditionA()){\n" +
                "            join & {\n" +
                "                actionB()&,\n" +
                "                if(conditionB()){\n" +
                "                    actionC()&\n" +
                "                },\n" +
                "                actionD()\n" +
                "            } [listenerA(event=\"start\")] then{\n" +
                "                actionE()\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionC()){\n" +
                "            actionF()\n" +
                "        },\n" +
                "        if(conditionD()){\n" +
                "            actionG()\n" +
                "        }\n" +
                "    }[listenerB(event=\"start\")],\n" +
                "    actionH()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 3, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) start.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.NORMAL, 3, 1, start);

        Condition conditionA = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, exclusiveGateway);

        Action actionB = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 0, 0, conditionA);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 1, 0, 0, conditionA);

        Action actionC = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 1, 0, 0, conditionB);

        Action actionD = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionA);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 1, actionB, actionC);

        Action actionE = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, joinGateway);

        Listener listenerA = joinGateway.getListeners().get(0);
        assertListener(listenerA, joinGateway, "listenerA", ListenerEvent.start, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Action actionF = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionF, LinkType.TRUE, "actionF", 0, 0, 0, 0, conditionC);

        Condition conditionD = (Condition) exclusiveGateway.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.NORMAL, "conditionD", 0, 1, 0, 0, exclusiveGateway);

        Action actionG = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionG, LinkType.TRUE, "actionG", 0, 0, 0, 0, conditionD);

        Listener listenerB = exclusiveGateway.getListeners().get(0);
        assertListener(listenerB, exclusiveGateway, "listenerB", ListenerEvent.start, 1);

        Action actionH = (Action) start.getSuccessors().get(2);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 6, 2, 0, 17, null);
    }

    @Test
    public void testSub1() {
        Rule rule = compile("{\n" +
                "    actionA(){\n" +
                "        sub{\n" +
                "            actionB(){\n" +
                "                actionC()\n" +
                "            },\n" +
                "            actionD()\n" +
                "        },\n" +
                "        actionE()\n" +
                "    },\n" +
                "    actionF()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, start);

        Rule subRule1 = (Rule) actionA.getSuccessors().get(0);
        assertRule(subRule1, LinkType.NORMAL, 0, 2, 0, 0, 4, actionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 2, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, subStart1);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, actionB);

        Action actionD = (Action) subStart1.getSuccessors().get(1);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, subStart1);

        Action actionE = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, actionA);

        Action actionF = (Action) start.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 3, 0, 0, 5, null);
    }

    @Test
    public void testSub2() {
        Rule rule = compile("{\n" +
                "    actionZ(){\n" +
                "        sub {\n" +
                "            actionA(){\n" +
                "                actionB(){\n" +
                "                    actionC(){\n" +
                "                        actionD(),\n" +
                "                        actionE()\n" +
                "                    },\n" +
                "                    actionF(){\n" +
                "                        actionG()\n" +
                "                    }\n" +
                "                },\n" +
                "                actionH(){\n" +
                "                    actionI(),\n" +
                "                    actionJ(){\n" +
                "                        actionK()\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            actionL(),\n" +
                "            actionM(){\n" +
                "                actionN()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionZ = (Action) start.getSuccessors().get(0);
        assertActivity(actionZ, LinkType.NORMAL, "actionZ", 0, 1, 0, 0, start);

        Rule subRule1 = (Rule) actionZ.getSuccessors().get(0);
        assertRule(subRule1, LinkType.NORMAL, 0, 7, 0, 0, 15, actionZ);

        Start subStart1 = subRule1.getStart();

        Action actionA = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, subStart1);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 2, 0, 0, actionA);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 2, 0, 0, actionB);

        Action actionD = (Action) actionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, actionC);

        Action actionE = (Action) actionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, actionC);

        Action actionF = (Action) actionB.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, actionB);

        Action actionG = (Action) actionF.getSuccessors().get(0);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, actionF);

        Action actionH = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 2, 0, 0, actionA);

        Action actionI = (Action) actionH.getSuccessors().get(0);
        assertActivity(actionI, LinkType.NORMAL, "actionI", 0, 0, 0, 0, actionH);

        Action actionJ = (Action) actionH.getSuccessors().get(1);
        assertActivity(actionJ, LinkType.NORMAL, "actionJ", 0, 1, 0, 0, actionH);

        Action actionK = (Action) actionJ.getSuccessors().get(0);
        assertActivity(actionK, LinkType.NORMAL, "actionK", 0, 0, 0, 0, actionJ);

        Action actionL = (Action) subStart1.getSuccessors().get(1);
        assertActivity(actionL, LinkType.NORMAL, "actionL", 0, 0, 0, 0, subStart1);

        Action actionM = (Action) subStart1.getSuccessors().get(2);
        assertActivity(actionM, LinkType.NORMAL, "actionM", 0, 1, 0, 0, subStart1);

        Action actionN = (Action) actionM.getSuccessors().get(0);
        assertActivity(actionN, LinkType.NORMAL, "actionN", 0, 0, 0, 0, actionM);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 3, null);
    }

    @Test
    public void testSub3() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()){\n" +
                "            if(conditionB()){\n" +
                "                actionA()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC())&\n" +
                "                    },\n" +
                "                    if(conditionD()){\n" +
                "                        actionB()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 0, 2, 0, 0, 10, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, subStart);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 0, 0, conditionA);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionB);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 0, conditionB);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 0, 0, conditionB);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 0, 0, conditionD);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 0, conditionC, actionB);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 2, null);
    }

    @Test
    public void testSub4() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()[listenerA(event=\"start\"), listenerB(event=\"end\")]){\n" +
                "            if(conditionB()[listenerC(event=\"start\"), listenerD(event=\"end\")]){\n" +
                "                actionA()[listenerE(event=\"start\"), listenerF(event=\"end\")]\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC()[listenerG(event=\"start\"), listenerH(event=\"end\")])&\n" +
                "                    }[listenerI(event=\"start\"), listenerJ(event=\"end\")],\n" +
                "                    if(conditionD()[listenerK(event=\"start\"), listenerL(event=\"end\")]){\n" +
                "                        actionB()[listenerM(event=\"start\"), listenerN(event=\"end\")]&\n" +
                "                    }\n" +
                "                }[listenerO(event=\"start\"), listenerP(event=\"end\")] then {\n" +
                "                    actionC()[listenerQ(event=\"start\"), listenerR(event=\"end\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[listenerS(event=\"start\"), listenerT(event=\"end\")]\n" +
                "}[listenerU(event=\"start\"), listenerV(event=\"end\")]");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 0, 2, 20, 0, 30, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 2, 0, subStart);

        Listener listenerA = conditionA.getListeners().get(0);
        assertListener(listenerA, conditionA, "listenerA", ListenerEvent.start, 1);

        Listener listenerB = conditionA.getListeners().get(1);
        assertListener(listenerB, conditionA, "listenerB", ListenerEvent.end, 1);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 2, 0, conditionA);

        Listener listenerC = conditionB.getListeners().get(0);
        assertListener(listenerC, conditionB, "listenerC", ListenerEvent.start, 1);

        Listener listenerD = conditionB.getListeners().get(1);
        assertListener(listenerD, conditionB, "listenerD", ListenerEvent.end, 1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 2, 0, conditionB);

        Listener listenerE = actionA.getListeners().get(0);
        assertListener(listenerE, actionA, "listenerE", ListenerEvent.start, 1);

        Listener listenerF = actionA.getListeners().get(1);
        assertListener(listenerF, actionA, "listenerF", ListenerEvent.end, 1);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 2, conditionB);

        Listener listenerI = exclusiveGateway.getListeners().get(0);
        assertListener(listenerI, exclusiveGateway, "listenerI", ListenerEvent.start, 1);

        Listener listenerJ = exclusiveGateway.getListeners().get(1);
        assertListener(listenerJ, exclusiveGateway, "listenerJ", ListenerEvent.end, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 2, 0, exclusiveGateway);

        Listener listenerG = conditionC.getListeners().get(0);
        assertListener(listenerG, conditionC, "listenerG", ListenerEvent.start, 1);

        Listener listenerH = conditionC.getListeners().get(1);
        assertListener(listenerH, conditionC, "listenerH", ListenerEvent.end, 1);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 2, 0, conditionB);

        Listener listenerK = conditionD.getListeners().get(0);
        assertListener(listenerK, conditionD, "listenerK", ListenerEvent.start, 1);

        Listener listenerL = conditionD.getListeners().get(1);
        assertListener(listenerL, conditionD, "listenerL", ListenerEvent.end, 1);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 2, 0, conditionD);

        Listener listenerM = actionB.getListeners().get(0);
        assertListener(listenerM, actionB, "listenerM", ListenerEvent.start, 1);

        Listener listenerN = actionB.getListeners().get(1);
        assertListener(listenerN, actionB, "listenerN", ListenerEvent.end, 1);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 2, conditionC, actionB);

        Listener listenerO = joinGateway.getListeners().get(0);
        assertListener(listenerO, joinGateway, "listenerO", ListenerEvent.start, 1);

        Listener listenerP = joinGateway.getListeners().get(1);
        assertListener(listenerP, joinGateway, "listenerP", ListenerEvent.end, 1);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 2, 0, joinGateway);

        Listener listenerQ = actionC.getListeners().get(0);
        assertListener(listenerQ, actionC, "listenerQ", ListenerEvent.start, 1);

        Listener listenerR = actionC.getListeners().get(1);
        assertListener(listenerR, actionC, "listenerR", ListenerEvent.end, 1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 2, 0, 4, null);
    }

    @Test
    public void testSub5() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        sub {\n" +
                "            actionB()\n" +
                "        },\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Rule subRule1 = (Rule) conditionA.getSuccessors().get(1);
        assertRule(subRule1, LinkType.TRUE, 0, 1, 0, 0, 2, conditionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 1, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, subStart1);

        Action actionC = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 3, 0, 0, 5, null);
    }

    @Test
    public void testSub6() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        sub {\n" +
                "            actionB()\n" +
                "        },\n" +
                "        actionC()\n" +
                "    }else{\n" +
                "        actionD(),\n" +
                "        sub {\n" +
                "            actionE()\n" +
                "        },\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 6, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Rule subRule1 = (Rule) conditionA.getSuccessors().get(1);
        assertRule(subRule1, LinkType.TRUE, 0, 1, 0, 0, 2, conditionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 1, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, subStart1);

        Action actionC = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, conditionA);

        Action actionD = (Action) conditionA.getSuccessors().get(3);
        assertActivity(actionD, LinkType.FALSE, "actionD", 0, 0, 0, 0, conditionA);

        Rule subRule2 = (Rule) conditionA.getSuccessors().get(4);
        assertRule(subRule2, LinkType.FALSE, 0, 1, 0, 0, 2, conditionA);

        Start subStart2 = subRule2.getStart();
        assertStart(subStart2, 1, 0);

        Action actionE = (Action) subStart2.getSuccessors().get(0);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, subStart2);

        Action actionF = (Action) conditionA.getSuccessors().get(5);
        assertActivity(actionF, LinkType.FALSE, "actionF", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 6, 0, 0, 8, null);
    }

    @Test
    public void testSubThen1() {
        Rule rule = compile("{\n" +
                "    actionA(){\n" +
                "        sub{\n" +
                "            actionB(){\n" +
                "                actionC()\n" +
                "            },\n" +
                "            actionD()\n" +
                "        } then {\n" +
                "            actionE()\n" +
                "        },\n" +
                "        actionF()\n" +
                "    },\n" +
                "    actionG()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, start);

        Rule subRule1 = (Rule) actionA.getSuccessors().get(0);
        assertRule(subRule1, LinkType.NORMAL, 1, 2, 0, 0, 4, actionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 2, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, subStart1);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, actionB);

        Action actionD = (Action) subStart1.getSuccessors().get(1);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, subStart1);

        Action actionE = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, subRule1);

        Action actionF = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 0, 0, 0, actionA);

        Action actionG = (Action) start.getSuccessors().get(1);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 3, 0, 0, 6, null);
    }

    @Test
    public void testSubThen2() {
        Rule rule = compile("{\n" +
                "    actionZ(){\n" +
                "        sub {\n" +
                "            actionA(){\n" +
                "                actionB(){\n" +
                "                    actionC(){\n" +
                "                        actionD(),\n" +
                "                        actionE()\n" +
                "                    },\n" +
                "                    actionF(){\n" +
                "                        actionG()\n" +
                "                    }\n" +
                "                },\n" +
                "                actionH(){\n" +
                "                    actionI(),\n" +
                "                    actionJ(){\n" +
                "                        actionK()\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            actionL(),\n" +
                "            actionM(){\n" +
                "                actionN()\n" +
                "            }\n" +
                "        } then {\n" +
                "            actionO()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionZ = (Action) start.getSuccessors().get(0);
        assertActivity(actionZ, LinkType.NORMAL, "actionZ", 0, 1, 0, 0, start);

        Rule subRule1 = (Rule) actionZ.getSuccessors().get(0);
        assertRule(subRule1, LinkType.NORMAL, 1, 7, 0, 0, 15, actionZ);

        Start subStart1 = subRule1.getStart();

        Action actionA = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, subStart1);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 2, 0, 0, actionA);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 2, 0, 0, actionB);

        Action actionD = (Action) actionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, actionC);

        Action actionE = (Action) actionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, actionC);

        Action actionF = (Action) actionB.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, actionB);

        Action actionG = (Action) actionF.getSuccessors().get(0);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, actionF);

        Action actionH = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 2, 0, 0, actionA);

        Action actionI = (Action) actionH.getSuccessors().get(0);
        assertActivity(actionI, LinkType.NORMAL, "actionI", 0, 0, 0, 0, actionH);

        Action actionJ = (Action) actionH.getSuccessors().get(1);
        assertActivity(actionJ, LinkType.NORMAL, "actionJ", 0, 1, 0, 0, actionH);

        Action actionK = (Action) actionJ.getSuccessors().get(0);
        assertActivity(actionK, LinkType.NORMAL, "actionK", 0, 0, 0, 0, actionJ);

        Action actionL = (Action) subStart1.getSuccessors().get(1);
        assertActivity(actionL, LinkType.NORMAL, "actionL", 0, 0, 0, 0, subStart1);

        Action actionM = (Action) subStart1.getSuccessors().get(2);
        assertActivity(actionM, LinkType.NORMAL, "actionM", 0, 1, 0, 0, subStart1);

        Action actionN = (Action) actionM.getSuccessors().get(0);
        assertActivity(actionN, LinkType.NORMAL, "actionN", 0, 0, 0, 0, actionM);

        Action actionO = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionO, LinkType.TRUE, "actionO", 0, 0, 0, 0, subRule1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 4, null);
    }

    @Test
    public void testSubThen3() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()){\n" +
                "            if(conditionB()){\n" +
                "                actionA()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC())&\n" +
                "                    },\n" +
                "                    if(conditionD()){\n" +
                "                        actionB()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 1, 2, 0, 0, 10, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, subStart);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 0, 0, conditionA);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionB);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 0, conditionB);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 0, 0, conditionB);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 0, 0, conditionD);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 0, conditionC, actionB);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        Action actionD = (Action) subRule.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, subRule);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 3, null);
    }

    @Test
    public void testSubThen4() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()[listenerA(event=\"start\"), listenerB(event=\"end\")]){\n" +
                "            if(conditionB()[listenerC(event=\"start\"), listenerD(event=\"end\")]){\n" +
                "                actionA()[listenerE(event=\"start\"), listenerF(event=\"end\")]\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC()[listenerG(event=\"start\"), listenerH(event=\"end\")])&\n" +
                "                    }[listenerI(event=\"start\"), listenerJ(event=\"end\")],\n" +
                "                    if(conditionD()[listenerK(event=\"start\"), listenerL(event=\"end\")]){\n" +
                "                        actionB()[listenerM(event=\"start\"), listenerN(event=\"end\")]&\n" +
                "                    }\n" +
                "                }[listenerO(event=\"start\"), listenerP(event=\"end\")] then {\n" +
                "                    actionC()[listenerQ(event=\"start\"), listenerR(event=\"end\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[listenerS(event=\"start\"), listenerT(event=\"end\")] then {\n" +
                "        actionD()[listenerU(event=\"start\"), listenerV(event=\"end\")]\n" +
                "    }\n" +
                "}[listenerW(event=\"start\"), listenerX(event=\"end\")]");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 1, 2, 20, 0, 30, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 2, 0, subStart);

        Listener listenerA = conditionA.getListeners().get(0);
        assertListener(listenerA, conditionA, "listenerA", ListenerEvent.start, 1);

        Listener listenerB = conditionA.getListeners().get(1);
        assertListener(listenerB, conditionA, "listenerB", ListenerEvent.end, 1);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 2, 0, conditionA);

        Listener listenerC = conditionB.getListeners().get(0);
        assertListener(listenerC, conditionB, "listenerC", ListenerEvent.start, 1);

        Listener listenerD = conditionB.getListeners().get(1);
        assertListener(listenerD, conditionB, "listenerD", ListenerEvent.end, 1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 2, 0, conditionB);

        Listener listenerE = actionA.getListeners().get(0);
        assertListener(listenerE, actionA, "listenerE", ListenerEvent.start, 1);

        Listener listenerF = actionA.getListeners().get(1);
        assertListener(listenerF, actionA, "listenerF", ListenerEvent.end, 1);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 2, conditionB);

        Listener listenerI = exclusiveGateway.getListeners().get(0);
        assertListener(listenerI, exclusiveGateway, "listenerI", ListenerEvent.start, 1);

        Listener listenerJ = exclusiveGateway.getListeners().get(1);
        assertListener(listenerJ, exclusiveGateway, "listenerJ", ListenerEvent.end, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 2, 0, exclusiveGateway);

        Listener listenerG = conditionC.getListeners().get(0);
        assertListener(listenerG, conditionC, "listenerG", ListenerEvent.start, 1);

        Listener listenerH = conditionC.getListeners().get(1);
        assertListener(listenerH, conditionC, "listenerH", ListenerEvent.end, 1);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 2, 0, conditionB);

        Listener listenerK = conditionD.getListeners().get(0);
        assertListener(listenerK, conditionD, "listenerK", ListenerEvent.start, 1);

        Listener listenerL = conditionD.getListeners().get(1);
        assertListener(listenerL, conditionD, "listenerL", ListenerEvent.end, 1);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 2, 0, conditionD);

        Listener listenerM = actionB.getListeners().get(0);
        assertListener(listenerM, actionB, "listenerM", ListenerEvent.start, 1);

        Listener listenerN = actionB.getListeners().get(1);
        assertListener(listenerN, actionB, "listenerN", ListenerEvent.end, 1);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 2, conditionC, actionB);

        Listener listenerO = joinGateway.getListeners().get(0);
        assertListener(listenerO, joinGateway, "listenerO", ListenerEvent.start, 1);

        Listener listenerP = joinGateway.getListeners().get(1);
        assertListener(listenerP, joinGateway, "listenerP", ListenerEvent.end, 1);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 2, 0, joinGateway);

        Listener listenerQ = actionC.getListeners().get(0);
        assertListener(listenerQ, actionC, "listenerQ", ListenerEvent.start, 1);

        Listener listenerR = actionC.getListeners().get(1);
        assertListener(listenerR, actionC, "listenerR", ListenerEvent.end, 1);

        Action actionD = (Action) subRule.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 2, 0, subRule);

        Listener listenerU = actionD.getListeners().get(0);
        assertListener(listenerU, actionD, "listenerU", ListenerEvent.start, 1);

        Listener listenerV = actionD.getListeners().get(1);
        assertListener(listenerV, actionD, "listenerV", ListenerEvent.end, 1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 4, 0, 7, null);
    }

    @Test
    public void testSubThen5() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        sub {\n" +
                "            actionB()\n" +
                "        } then {\n" +
                "            actionC()\n" +
                "        },\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Rule subRule1 = (Rule) conditionA.getSuccessors().get(1);
        assertRule(subRule1, LinkType.TRUE, 1, 1, 0, 0, 2, conditionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 1, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, subStart1);

        Action actionC = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, subRule1);

        Action actionD = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 3, 0, 0, 6, null);
    }

    @Test
    public void testSubThen6() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        sub {\n" +
                "            actionB()\n" +
                "        } then {\n" +
                "            actionC()\n" +
                "        },\n" +
                "        actionD()\n" +
                "    }else{\n" +
                "        actionE(),\n" +
                "        sub {\n" +
                "            actionF()\n" +
                "        } then {\n" +
                "            actionG()\n" +
                "        },\n" +
                "        actionH()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 6, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Rule subRule1 = (Rule) conditionA.getSuccessors().get(1);
        assertRule(subRule1, LinkType.TRUE, 1, 1, 0, 0, 2, conditionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 1, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, subStart1);

        Action actionC = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, subRule1);

        Action actionD = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, conditionA);

        Action actionE = (Action) conditionA.getSuccessors().get(3);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 0, 0, 0, conditionA);

        Rule subRule2 = (Rule) conditionA.getSuccessors().get(4);
        assertRule(subRule2, LinkType.FALSE, 1, 1, 0, 0, 2, conditionA);

        Start subStart2 = subRule2.getStart();
        assertStart(subStart2, 1, 0);

        Action actionF = (Action) subStart2.getSuccessors().get(0);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 0, 0, 0, subStart2);

        Action actionG = (Action) subRule2.getSuccessors().get(0);
        assertActivity(actionG, LinkType.TRUE, "actionG", 0, 0, 0, 0, subRule2);

        Action actionH = (Action) conditionA.getSuccessors().get(5);
        assertActivity(actionH, LinkType.FALSE, "actionH", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 6, 0, 0, 10, null);
    }

    @Test
    public void testSubElseThen1() {
        Rule rule = compile("{\n" +
                "    actionA(){\n" +
                "        sub{\n" +
                "            actionB(){\n" +
                "                actionC()\n" +
                "            },\n" +
                "            actionD()\n" +
                "        } then {\n" +
                "            actionE()\n" +
                "        } else {\n" +
                "            actionF()\n" +
                "        },\n" +
                "        actionG()\n" +
                "    },\n" +
                "    actionH()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, start);

        Rule subRule1 = (Rule) actionA.getSuccessors().get(0);
        assertRule(subRule1, LinkType.NORMAL, 2, 2, 0, 0, 4, actionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 2, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, subStart1);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, actionB);

        Action actionD = (Action) subStart1.getSuccessors().get(1);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, subStart1);

        Action actionE = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, subRule1);

        Action actionF = (Action) subRule1.getSuccessors().get(1);
        assertActivity(actionF, LinkType.FALSE, "actionF", 0, 0, 0, 0, subRule1);

        Action actionG = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, actionA);

        Action actionH = (Action) start.getSuccessors().get(1);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 4, 0, 0, 7, null);
    }

    @Test
    public void testSubElseThen2() {
        Rule rule = compile("{\n" +
                "    actionZ(){\n" +
                "        sub {\n" +
                "            actionA(){\n" +
                "                actionB(){\n" +
                "                    actionC(){\n" +
                "                        actionD(),\n" +
                "                        actionE()\n" +
                "                    },\n" +
                "                    actionF(){\n" +
                "                        actionG()\n" +
                "                    }\n" +
                "                },\n" +
                "                actionH(){\n" +
                "                    actionI(),\n" +
                "                    actionJ(){\n" +
                "                        actionK()\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            actionL(),\n" +
                "            actionM(){\n" +
                "                actionN()\n" +
                "            }\n" +
                "        } then {\n" +
                "            actionO()\n" +
                "        } else {\n" +
                "            actionP()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionZ = (Action) start.getSuccessors().get(0);
        assertActivity(actionZ, LinkType.NORMAL, "actionZ", 0, 1, 0, 0, start);

        Rule subRule1 = (Rule) actionZ.getSuccessors().get(0);
        assertRule(subRule1, LinkType.NORMAL, 2, 7, 0, 0, 15, actionZ);

        Start subStart1 = subRule1.getStart();

        Action actionA = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 2, 0, 0, subStart1);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 2, 0, 0, actionA);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 2, 0, 0, actionB);

        Action actionD = (Action) actionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, actionC);

        Action actionE = (Action) actionC.getSuccessors().get(1);
        assertActivity(actionE, LinkType.NORMAL, "actionE", 0, 0, 0, 0, actionC);

        Action actionF = (Action) actionB.getSuccessors().get(1);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, actionB);

        Action actionG = (Action) actionF.getSuccessors().get(0);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, actionF);

        Action actionH = (Action) actionA.getSuccessors().get(1);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 2, 0, 0, actionA);

        Action actionI = (Action) actionH.getSuccessors().get(0);
        assertActivity(actionI, LinkType.NORMAL, "actionI", 0, 0, 0, 0, actionH);

        Action actionJ = (Action) actionH.getSuccessors().get(1);
        assertActivity(actionJ, LinkType.NORMAL, "actionJ", 0, 1, 0, 0, actionH);

        Action actionK = (Action) actionJ.getSuccessors().get(0);
        assertActivity(actionK, LinkType.NORMAL, "actionK", 0, 0, 0, 0, actionJ);

        Action actionL = (Action) subStart1.getSuccessors().get(1);
        assertActivity(actionL, LinkType.NORMAL, "actionL", 0, 0, 0, 0, subStart1);

        Action actionM = (Action) subStart1.getSuccessors().get(2);
        assertActivity(actionM, LinkType.NORMAL, "actionM", 0, 1, 0, 0, subStart1);

        Action actionN = (Action) actionM.getSuccessors().get(0);
        assertActivity(actionN, LinkType.NORMAL, "actionN", 0, 0, 0, 0, actionM);

        Action actionO = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionO, LinkType.TRUE, "actionO", 0, 0, 0, 0, subRule1);

        Action actionP = (Action) subRule1.getSuccessors().get(1);
        assertActivity(actionP, LinkType.FALSE, "actionP", 0, 0, 0, 0, subRule1);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 5, null);
    }

    @Test
    public void testSubElseThen3() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()){\n" +
                "            if(conditionB()){\n" +
                "                actionA()\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC())&\n" +
                "                    },\n" +
                "                    if(conditionD()){\n" +
                "                        actionB()&\n" +
                "                    }\n" +
                "                } then {\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    } else {\n" +
                "        actionE()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 2, 2, 0, 0, 10, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, subStart);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 0, 0, conditionA);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionB);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 0, conditionB);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 0, 0, conditionB);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 0, 0, conditionD);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 0, conditionC, actionB);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        Action actionD = (Action) subRule.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 0, 0, subRule);

        Action actionE = (Action) subRule.getSuccessors().get(1);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 0, 0, 0, subRule);

        assertRule(rule, LinkType.NORMAL, 0, 2, 0, 0, 4, null);
    }

    @Test
    public void testSubElseThen4() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()[listenerA(event=\"start\"), listenerB(event=\"end\")]){\n" +
                "            if(conditionB()[listenerC(event=\"start\"), listenerD(event=\"end\")]){\n" +
                "                actionA()[listenerE(event=\"start\"), listenerF(event=\"end\")]\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC()[listenerG(event=\"start\"), listenerH(event=\"end\")])&\n" +
                "                    }[listenerI(event=\"start\"), listenerJ(event=\"end\")],\n" +
                "                    if(conditionD()[listenerK(event=\"start\"), listenerL(event=\"end\")]){\n" +
                "                        actionB()[listenerM(event=\"start\"), listenerN(event=\"end\")]&\n" +
                "                    }\n" +
                "                }[listenerO(event=\"start\"), listenerP(event=\"end\")] then {\n" +
                "                    actionC()[listenerQ(event=\"start\"), listenerR(event=\"end\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[listenerS(event=\"start\"), listenerT(event=\"end\")] then {\n" +
                "        actionD()[listenerU(event=\"start\"), listenerV(event=\"end\")]\n" +
                "    } else{\n" +
                "        actionE()[listenerW(event=\"start\"), listenerX(event=\"end\")]\n" +
                "    }\n" +
                "}[listenerY(event=\"start\"), listenerZ(event=\"end\")]\n");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 2, 2, 20, 0, 30, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 2, 0, subStart);

        Listener listenerA = conditionA.getListeners().get(0);
        assertListener(listenerA, conditionA, "listenerA", ListenerEvent.start, 1);

        Listener listenerB = conditionA.getListeners().get(1);
        assertListener(listenerB, conditionA, "listenerB", ListenerEvent.end, 1);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 2, 0, conditionA);

        Listener listenerC = conditionB.getListeners().get(0);
        assertListener(listenerC, conditionB, "listenerC", ListenerEvent.start, 1);

        Listener listenerD = conditionB.getListeners().get(1);
        assertListener(listenerD, conditionB, "listenerD", ListenerEvent.end, 1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 2, 0, conditionB);

        Listener listenerE = actionA.getListeners().get(0);
        assertListener(listenerE, actionA, "listenerE", ListenerEvent.start, 1);

        Listener listenerF = actionA.getListeners().get(1);
        assertListener(listenerF, actionA, "listenerF", ListenerEvent.end, 1);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 2, conditionB);

        Listener listenerI = exclusiveGateway.getListeners().get(0);
        assertListener(listenerI, exclusiveGateway, "listenerI", ListenerEvent.start, 1);

        Listener listenerJ = exclusiveGateway.getListeners().get(1);
        assertListener(listenerJ, exclusiveGateway, "listenerJ", ListenerEvent.end, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 2, 0, exclusiveGateway);

        Listener listenerG = conditionC.getListeners().get(0);
        assertListener(listenerG, conditionC, "listenerG", ListenerEvent.start, 1);

        Listener listenerH = conditionC.getListeners().get(1);
        assertListener(listenerH, conditionC, "listenerH", ListenerEvent.end, 1);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 2, 0, conditionB);

        Listener listenerK = conditionD.getListeners().get(0);
        assertListener(listenerK, conditionD, "listenerK", ListenerEvent.start, 1);

        Listener listenerL = conditionD.getListeners().get(1);
        assertListener(listenerL, conditionD, "listenerL", ListenerEvent.end, 1);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 2, 0, conditionD);

        Listener listenerM = actionB.getListeners().get(0);
        assertListener(listenerM, actionB, "listenerM", ListenerEvent.start, 1);

        Listener listenerN = actionB.getListeners().get(1);
        assertListener(listenerN, actionB, "listenerN", ListenerEvent.end, 1);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 2, conditionC, actionB);

        Listener listenerO = joinGateway.getListeners().get(0);
        assertListener(listenerO, joinGateway, "listenerO", ListenerEvent.start, 1);

        Listener listenerP = joinGateway.getListeners().get(1);
        assertListener(listenerP, joinGateway, "listenerP", ListenerEvent.end, 1);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 2, 0, joinGateway);

        Listener listenerQ = actionC.getListeners().get(0);
        assertListener(listenerQ, actionC, "listenerQ", ListenerEvent.start, 1);

        Listener listenerR = actionC.getListeners().get(1);
        assertListener(listenerR, actionC, "listenerR", ListenerEvent.end, 1);

        Action actionD = (Action) subRule.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 2, 0, subRule);

        Listener listenerU = actionD.getListeners().get(0);
        assertListener(listenerU, actionD, "listenerU", ListenerEvent.start, 1);

        Listener listenerV = actionD.getListeners().get(1);
        assertListener(listenerV, actionD, "listenerV", ListenerEvent.end, 1);

        Action actionE = (Action) subRule.getSuccessors().get(1);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 0, 2, 0, subRule);

        Listener listenerW = actionE.getListeners().get(0);
        assertListener(listenerW, actionE, "listenerW", ListenerEvent.start, 1);

        Listener listenerX = actionE.getListeners().get(1);
        assertListener(listenerX, actionE, "listenerX", ListenerEvent.end, 1);

        assertRule(rule, LinkType.NORMAL, 0, 2, 6, 0, 10, null);
    }

    @Test
    public void testSubThenElse5() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        sub {\n" +
                "            actionB()\n" +
                "        } then {\n" +
                "            actionC()\n" +
                "        } else{\n" +
                "            actionD()\n" +
                "        },\n" +
                "        actionE()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 3, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Rule subRule1 = (Rule) conditionA.getSuccessors().get(1);
        assertRule(subRule1, LinkType.TRUE, 2, 1, 0, 0, 2, conditionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 1, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, subStart1);

        Action actionC = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, subRule1);

        Action actionD = (Action) subRule1.getSuccessors().get(1);
        assertActivity(actionD, LinkType.FALSE, "actionD", 0, 0, 0, 0, subRule1);

        Action actionE = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 4, 0, 0, 7, null);
    }

    @Test
    public void testSubThenElse6() {
        Rule rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        sub {\n" +
                "            actionB()\n" +
                "        } then {\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()\n" +
                "        },\n" +
                "        actionE()\n" +
                "    }else{\n" +
                "        actionF(),\n" +
                "        sub {\n" +
                "            actionG()\n" +
                "        } then {\n" +
                "            actionH()\n" +
                "        } else {\n" +
                "            actionI()\n" +
                "        },\n" +
                "        actionJ()\n" +
                "    }\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 6, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Rule subRule1 = (Rule) conditionA.getSuccessors().get(1);
        assertRule(subRule1, LinkType.TRUE, 2, 1, 0, 0, 2, conditionA);

        Start subStart1 = subRule1.getStart();
        assertStart(subStart1, 1, 0);

        Action actionB = (Action) subStart1.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, subStart1);

        Action actionC = (Action) subRule1.getSuccessors().get(0);
        assertActivity(actionC, LinkType.TRUE, "actionC", 0, 0, 0, 0, subRule1);

        Action actionD = (Action) subRule1.getSuccessors().get(1);
        assertActivity(actionD, LinkType.FALSE, "actionD", 0, 0, 0, 0, subRule1);

        Action actionE = (Action) conditionA.getSuccessors().get(2);
        assertActivity(actionE, LinkType.TRUE, "actionE", 0, 0, 0, 0, conditionA);

        Action actionF = (Action) conditionA.getSuccessors().get(3);
        assertActivity(actionF, LinkType.FALSE, "actionF", 0, 0, 0, 0, conditionA);

        Rule subRule2 = (Rule) conditionA.getSuccessors().get(4);
        assertRule(subRule2, LinkType.FALSE, 2, 1, 0, 0, 2, conditionA);

        Start subStart2 = subRule2.getStart();
        assertStart(subStart2, 1, 0);

        Action actionG = (Action) subStart2.getSuccessors().get(0);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, subStart2);

        Action actionH = (Action) subRule2.getSuccessors().get(0);
        assertActivity(actionH, LinkType.TRUE, "actionH", 0, 0, 0, 0, subRule2);

        Action actionI = (Action) subRule2.getSuccessors().get(1);
        assertActivity(actionI, LinkType.FALSE, "actionI", 0, 0, 0, 0, subRule2);

        Action actionJ = (Action) conditionA.getSuccessors().get(5);
        assertActivity(actionJ, LinkType.FALSE, "actionJ", 0, 0, 0, 0, conditionA);

        assertRule(rule, LinkType.NORMAL, 0, 8, 0, 0, 12, null);
    }

    @Test
    public void testListeners() {
        Rule rule = compile("{\n" +
                "    actionA() [listenerA(event=\"start\")]\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 1, 0, start);

        Listener listenerA = actionA.getListeners().get(0);
        assertListener(listenerA, actionA, "listenerA", ListenerEvent.start, 1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 1, 0, 3, null);
    }

    private void assertStart(Start start, int successorSize, int listenerSize) {
        Assert.assertEquals(0, start.getPredecessors().size());
        Assert.assertEquals(successorSize, start.getSuccessors().size());
        Assert.assertEquals(listenerSize, start.getListeners().size());
    }

    private void assertRule(Rule rule, LinkType linkType, int successorSize, int endNum, int listenerSize, int eventSize, int elementSize, Node predecessor) {
        Assert.assertEquals(linkType, rule.getLinkType());
        if (predecessor != null) {
            Assert.assertEquals(1, rule.getPredecessors().size());
        } else {
            Assert.assertEquals(0, rule.getPredecessors().size());
        }
        Assert.assertEquals(successorSize, rule.getSuccessors().size());
        Assert.assertEquals(endNum, rule.getEnds().size());
        Assert.assertEquals(listenerSize, rule.getListeners().size());
        Assert.assertEquals(eventSize, rule.getEvents().size());
        Assert.assertEquals(elementSize, rule.getElements().size());
        if (predecessor != null) {
            Assert.assertEquals(rule.getPredecessors().get(0), predecessor);
        }
        if (!rule.getListeners().isEmpty()) {
            for (Listener listener : rule.getListeners()) {
                String attachedId = listener.getAttachedId();
                if (attachedId == null) {
                    Assert.assertEquals(ListenerScope.GLOBAL, listener.getScope());
                    continue;
                }
                Node attachedNode = (Node) rule.getElement(attachedId);
                Assert.assertTrue(attachedNode.getListeners().contains(listener));
            }
        }

        for (Element element : rule.getElements()) {
            Assert.assertNotNull(element.getRule());
        }
    }

    private void assertActivity(Activity activity, LinkType linkType, String name, int argumentLength, int successorSize, int listenerSize, int eventSize, Node predecessor) {
        Assert.assertEquals(linkType, activity.getLinkType());
        Assert.assertEquals(name, activity.getName());
        Assert.assertEquals(argumentLength, activity.getArgumentNames().length);
        Assert.assertEquals(argumentLength, activity.getArgumentValues().length);
        Assert.assertEquals(1, activity.getPredecessors().size());
        Assert.assertEquals(successorSize, activity.getSuccessors().size());
        Assert.assertEquals(listenerSize, activity.getListeners().size());
        Assert.assertEquals(activity.getPredecessors().get(0), predecessor);
    }

    private void assertListener(Listener listener, Node attachedNode, String name, ListenerEvent event, int argumentLength) {
        Assert.assertEquals(attachedNode.getId(), listener.getAttachedId());
        Assert.assertEquals(name, listener.getName());
        Assert.assertEquals(event, listener.getEvent());
        Assert.assertEquals(argumentLength, listener.getArgumentNames().length);
        Assert.assertEquals(argumentLength, listener.getArgumentValues().length);
    }

    private void assertJoinGateway(JoinGateway gateway, int predecessorSize, int successorSize, int listenerSize, Node... predecessors) {
        Assert.assertEquals(predecessorSize, gateway.getPredecessors().size());
        Assert.assertEquals(successorSize, gateway.getSuccessors().size());
        Assert.assertEquals(listenerSize, gateway.getListeners().size());
        Assert.assertEquals(predecessorSize, predecessors.length);

        for (int i = 0; i < predecessors.length; i++) {
            Assert.assertEquals(gateway.getPredecessors().get(i), predecessors[i]);
            Assert.assertEquals(predecessors[i].getSuccessors().get(0), gateway);
        }
    }

    private void assertExclusiveGateway(ExclusiveGateway gateway, LinkType linkType, int successorSize, int listenerSize, Node predecessor) {
        Assert.assertEquals(linkType, gateway.getLinkType());
        Assert.assertEquals(1, gateway.getPredecessors().size());
        Assert.assertEquals(successorSize, gateway.getSuccessors().size());
        Assert.assertEquals(listenerSize, gateway.getListeners().size());
        Assert.assertEquals(gateway.getPredecessors().get(0), predecessor);
    }
}
