package com.github.liuyehcf.framework.rule.engine.test.dsl.topology;

import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestExclusiveGatewayTopology extends TestBaseTopology {

    @Test
    public void testSelectIfThenSingleAction() {
        Rule rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionA()\n" +
                "        },\n" +
                "        if(conditionB()){\n" +
                "            actionB()\n" +
                "        }\n" +
                "    } [listenerA(event=\"before\")]\n" +
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
        assertListener(listenerA, exclusiveGateway, "listenerA", ListenerEvent.before, 1);

        assertRule(rule, LinkType.NORMAL, 0, 2, 1, 0, 7, null);
    }

    @Test
    public void testSelectIfThenSingleActionWithParallelAction() {
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
    public void testSelectComplexIfThenElse() {
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
    public void testCascadeActionAndSelect() {
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
                "            }[listenerA(event=\"before\")],\n" +
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
    public void testSelectNestedInIfThen() {
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
                "        }[listenerA(event=\"success\")]\n" +
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
        assertListener(listenerA, exclusiveGateway2, "listenerA", ListenerEvent.success, 1);

        assertRule(rule, LinkType.NORMAL, 0, 3, 1, 0, 11, null);
    }

    @Test
    public void testSelectNestedInJoin() {
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
                "        }[listenerA(event=\"before\")],\n" +
                "        actionF()&,\n" +
                "        actionG()\n" +
                "    }[listenerB(event=\"before\")],\n" +
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
        assertListener(listenerA, exclusiveGateway, "listenerA", ListenerEvent.before, 1);

        Action actionF = (Action) start.getSuccessors().get(3);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, start);

        Action actionG = (Action) start.getSuccessors().get(4);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 5, 0, 1, actionB, actionC, actionD, actionE, actionF);

        Listener listenerB = joinGateway.getListeners().get(0);
        assertListener(listenerB, joinGateway, "listenerB", ListenerEvent.before, 1);

        Action actionH = (Action) start.getSuccessors().get(5);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 4, 2, 0, 16, null);
    }

    @Test
    public void testSelectNestedInJoinWithListener() {
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
                "        }[listenerA(event=\"before\")],\n" +
                "        actionF()&,\n" +
                "        actionG()\n" +
                "    }[listenerB(event=\"before\")] then{\n" +
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
                "        }[listenerC(event=\"before\")]\n" +
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
        assertListener(listenerA, exclusiveGateway1, "listenerA", ListenerEvent.before, 1);

        Action actionF = (Action) start.getSuccessors().get(3);
        assertActivity(actionF, LinkType.NORMAL, "actionF", 0, 1, 0, 0, start);

        Action actionG = (Action) start.getSuccessors().get(4);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionB.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 5, 2, 1, actionB, actionC, actionD, actionE, actionF);

        Listener listenerB = joinGateway.getListeners().get(0);
        assertListener(listenerB, joinGateway, "listenerB", ListenerEvent.before, 1);

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
        assertListener(listenerC, exclusiveGateway2, "listenerC", ListenerEvent.before, 1);

        Action actionL = (Action) start.getSuccessors().get(5);
        assertActivity(actionL, LinkType.NORMAL, "actionL", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 7, 3, 0, 25, null);
    }

    @Test
    public void testSelectContainJoin() {
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
                "    }[listenerA(event=\"before\")],\n" +
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
        assertListener(listenerA, exclusiveGateway, "listenerA", ListenerEvent.before, 1);

        Action actionG = (Action) start.getSuccessors().get(2);
        assertActivity(actionG, LinkType.NORMAL, "actionG", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 6, 1, 0, 15, null);
    }

    @Test
    public void testSelectContainJoinWithListener() {
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
                "            } [listenerA(event=\"before\")] then{\n" +
                "                actionE()\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionC()){\n" +
                "            actionF()\n" +
                "        },\n" +
                "        if(conditionD()){\n" +
                "            actionG()\n" +
                "        }\n" +
                "    }[listenerB(event=\"before\")],\n" +
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
        assertListener(listenerA, joinGateway, "listenerA", ListenerEvent.before, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(1);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 0, 0, exclusiveGateway);

        Action actionF = (Action) conditionC.getSuccessors().get(0);
        assertActivity(actionF, LinkType.TRUE, "actionF", 0, 0, 0, 0, conditionC);

        Condition conditionD = (Condition) exclusiveGateway.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.NORMAL, "conditionD", 0, 1, 0, 0, exclusiveGateway);

        Action actionG = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionG, LinkType.TRUE, "actionG", 0, 0, 0, 0, conditionD);

        Listener listenerB = exclusiveGateway.getListeners().get(0);
        assertListener(listenerB, exclusiveGateway, "listenerB", ListenerEvent.before, 1);

        Action actionH = (Action) start.getSuccessors().get(2);
        assertActivity(actionH, LinkType.NORMAL, "actionH", 0, 0, 0, 0, start);

        assertRule(rule, LinkType.NORMAL, 0, 6, 2, 0, 17, null);
    }
}
