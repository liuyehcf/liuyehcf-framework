package com.github.liuyehcf.framework.flow.engine.test.dsl.topology;

import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestConditionTopology extends TestBaseTopology {

    @Test
    public void testSingleIf() {
        Flow flow = compile("{\n" +
                "    if(conditionA())\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 0, 0, 0, start);

        assertFlow(flow, LinkType.NORMAL, 0, 1, 0, 0, 2, null);
    }

    @Test
    public void testParallelIf() {
        Flow flow = compile("{\n" +
                "    if(conditionA()),\n" +
                "    if(conditionB())\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 2, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 0, 0, 0, start);

        Condition conditionB = (Condition) start.getSuccessors().get(1);
        assertActivity(conditionB, LinkType.NORMAL, "conditionB", 0, 0, 0, 0, start);

        assertFlow(flow, LinkType.NORMAL, 0, 2, 0, 0, 3, null);
    }

    @Test
    public void testIfThenSingleAction() {
        Flow flow = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        assertFlow(flow, LinkType.NORMAL, 0, 1, 0, 0, 3, null);
    }

    @Test
    public void testIfThenCascadeAction() {
        Flow flow = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(){\n" +
                "            actionB(){\n" +
                "                actionC()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 1, 0, 0, conditionA);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, actionA);

        Action actionC = (Action) actionB.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, actionB);

        assertFlow(flow, LinkType.NORMAL, 0, 1, 0, 0, 5, null);
    }

    @Test
    public void testIfThenParallelAction() {
        Flow flow = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 2, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Action actionB = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 0, 0, 0, conditionA);

        assertFlow(flow, LinkType.NORMAL, 0, 2, 0, 0, 4, null);
    }

    @Test
    public void testIfThenElseSingleAction() {
        Flow flow = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA()\n" +
                "    }else{\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 2, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 0, 0, conditionA);

        Action actionB = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionB, LinkType.FALSE, "actionB", 0, 0, 0, 0, conditionA);

        assertFlow(flow, LinkType.NORMAL, 0, 2, 0, 0, 4, null);
    }

    @Test
    public void testIfThenElseCascadeAction() {
        Flow flow = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(){\n" +
                "            actionB()\n" +
                "        }\n" +
                "    }else{\n" +
                "        actionC(){\n" +
                "            actionD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Condition conditionA = (Condition) start.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 2, 0, 0, start);

        Action actionA = (Action) conditionA.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 1, 0, 0, conditionA);

        Action actionB = (Action) actionA.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, actionA);

        Action actionC = (Action) conditionA.getSuccessors().get(1);
        assertActivity(actionC, LinkType.FALSE, "actionC", 0, 1, 0, 0, conditionA);

        Action actionD = (Action) actionC.getSuccessors().get(0);
        assertActivity(actionD, LinkType.NORMAL, "actionD", 0, 0, 0, 0, actionC);

        assertFlow(flow, LinkType.NORMAL, 0, 2, 0, 0, 6, null);
    }

    @Test
    public void testIfThenElseParallelAction() {
        Flow flow = compile("{\n" +
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

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 6, 0, 0, 8, null);
    }

    @Test
    public void testIfThenCascadeIfThen() {
        Flow flow = compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionB(),\n" +
                "        if(conditionC()){\n" +
                "            actionD(),\n" +
                "            actionE()\n" +
                "        },\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 4, 0, 0, 7, null);
    }

    @Test
    public void testIfThenCascadeIfThenElse() {
        Flow flow = compile("{\n" +
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

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 6, 0, 0, 9, null);
    }

    @Test
    public void testIfThenElseCascadeIfThenAndIfThenElse() {
        Flow flow = compile("{\n" +
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

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 9, 0, 0, 15, null);
    }
}
