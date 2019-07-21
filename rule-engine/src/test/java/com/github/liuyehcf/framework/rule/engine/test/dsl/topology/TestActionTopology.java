package com.github.liuyehcf.framework.rule.engine.test.dsl.topology;

import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestActionTopology extends TestBaseTopology {

    @Test
    public void testSingleAction() {
        Rule rule = compile("{\n" +
                "    actionA()\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 1, 0, 0, 2, null);
    }

    @Test
    public void testCascadeAction() {
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
    public void testComplexCascadeAction() {
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
    public void testParallelAction() {
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
    public void testCascadeParallel() {
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
}
