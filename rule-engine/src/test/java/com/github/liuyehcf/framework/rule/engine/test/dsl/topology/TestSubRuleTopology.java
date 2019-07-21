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
public class TestSubRuleTopology extends TestBaseTopology {

    @Test
    public void testSubParallelWithAction() {
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
    public void testSubCascadeWithAction() {
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
    public void testSubContainComplex() {
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
    public void testSubContainComplexWithListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()[listenerA(event=\"before\"), listenerB(event=\"success\")]){\n" +
                "            if(conditionB()[listenerC(event=\"before\"), listenerD(event=\"success\")]){\n" +
                "                actionA()[listenerE(event=\"before\"), listenerF(event=\"success\")]\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC()[listenerG(event=\"before\"), listenerH(event=\"success\")])&\n" +
                "                    }[listenerI(event=\"before\"), listenerJ(event=\"success\")],\n" +
                "                    if(conditionD()[listenerK(event=\"before\"), listenerL(event=\"success\")]){\n" +
                "                        actionB()[listenerM(event=\"before\"), listenerN(event=\"success\")]&\n" +
                "                    }\n" +
                "                }[listenerO(event=\"before\"), listenerP(event=\"success\")] then {\n" +
                "                    actionC()[listenerQ(event=\"before\"), listenerR(event=\"success\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[listenerS(event=\"before\"), listenerT(event=\"success\")]\n" +
                "}[listenerU(event=\"before\"), listenerV(event=\"success\")]");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 0, 2, 20, 0, 30, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 2, 0, subStart);

        Listener listenerA = conditionA.getListeners().get(0);
        assertListener(listenerA, conditionA, "listenerA", ListenerEvent.before, 1);

        Listener listenerB = conditionA.getListeners().get(1);
        assertListener(listenerB, conditionA, "listenerB", ListenerEvent.success, 1);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 2, 0, conditionA);

        Listener listenerC = conditionB.getListeners().get(0);
        assertListener(listenerC, conditionB, "listenerC", ListenerEvent.before, 1);

        Listener listenerD = conditionB.getListeners().get(1);
        assertListener(listenerD, conditionB, "listenerD", ListenerEvent.success, 1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 2, 0, conditionB);

        Listener listenerE = actionA.getListeners().get(0);
        assertListener(listenerE, actionA, "listenerE", ListenerEvent.before, 1);

        Listener listenerF = actionA.getListeners().get(1);
        assertListener(listenerF, actionA, "listenerF", ListenerEvent.success, 1);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 2, conditionB);

        Listener listenerI = exclusiveGateway.getListeners().get(0);
        assertListener(listenerI, exclusiveGateway, "listenerI", ListenerEvent.before, 1);

        Listener listenerJ = exclusiveGateway.getListeners().get(1);
        assertListener(listenerJ, exclusiveGateway, "listenerJ", ListenerEvent.success, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 2, 0, exclusiveGateway);

        Listener listenerG = conditionC.getListeners().get(0);
        assertListener(listenerG, conditionC, "listenerG", ListenerEvent.before, 1);

        Listener listenerH = conditionC.getListeners().get(1);
        assertListener(listenerH, conditionC, "listenerH", ListenerEvent.success, 1);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 2, 0, conditionB);

        Listener listenerK = conditionD.getListeners().get(0);
        assertListener(listenerK, conditionD, "listenerK", ListenerEvent.before, 1);

        Listener listenerL = conditionD.getListeners().get(1);
        assertListener(listenerL, conditionD, "listenerL", ListenerEvent.success, 1);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 2, 0, conditionD);

        Listener listenerM = actionB.getListeners().get(0);
        assertListener(listenerM, actionB, "listenerM", ListenerEvent.before, 1);

        Listener listenerN = actionB.getListeners().get(1);
        assertListener(listenerN, actionB, "listenerN", ListenerEvent.success, 1);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 2, conditionC, actionB);

        Listener listenerO = joinGateway.getListeners().get(0);
        assertListener(listenerO, joinGateway, "listenerO", ListenerEvent.before, 1);

        Listener listenerP = joinGateway.getListeners().get(1);
        assertListener(listenerP, joinGateway, "listenerP", ListenerEvent.success, 1);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 2, 0, joinGateway);

        Listener listenerQ = actionC.getListeners().get(0);
        assertListener(listenerQ, actionC, "listenerQ", ListenerEvent.before, 1);

        Listener listenerR = actionC.getListeners().get(1);
        assertListener(listenerR, actionC, "listenerR", ListenerEvent.success, 1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 2, 0, 4, null);
    }

    @Test
    public void testSubNestedInIfThen() {
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
    public void testSubNestedInIfThenElse() {
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
    public void testSubThenCascadeAndParallelWithAction() {
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
    public void testSubThenCascadeWithAction() {
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
    public void testSubThenContainJonAndSelect() {
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
    public void testSubThenContainJoinAndSelectWithListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()[listenerA(event=\"before\"), listenerB(event=\"success\")]){\n" +
                "            if(conditionB()[listenerC(event=\"before\"), listenerD(event=\"success\")]){\n" +
                "                actionA()[listenerE(event=\"before\"), listenerF(event=\"success\")]\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC()[listenerG(event=\"before\"), listenerH(event=\"success\")])&\n" +
                "                    }[listenerI(event=\"before\"), listenerJ(event=\"success\")],\n" +
                "                    if(conditionD()[listenerK(event=\"before\"), listenerL(event=\"success\")]){\n" +
                "                        actionB()[listenerM(event=\"before\"), listenerN(event=\"success\")]&\n" +
                "                    }\n" +
                "                }[listenerO(event=\"before\"), listenerP(event=\"success\")] then {\n" +
                "                    actionC()[listenerQ(event=\"before\"), listenerR(event=\"success\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[listenerS(event=\"before\"), listenerT(event=\"success\")] then {\n" +
                "        actionD()[listenerU(event=\"before\"), listenerV(event=\"success\")]\n" +
                "    }\n" +
                "}[listenerW(event=\"before\"), listenerX(event=\"success\")]");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 1, 2, 20, 0, 30, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 2, 0, subStart);

        Listener listenerA = conditionA.getListeners().get(0);
        assertListener(listenerA, conditionA, "listenerA", ListenerEvent.before, 1);

        Listener listenerB = conditionA.getListeners().get(1);
        assertListener(listenerB, conditionA, "listenerB", ListenerEvent.success, 1);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 2, 0, conditionA);

        Listener listenerC = conditionB.getListeners().get(0);
        assertListener(listenerC, conditionB, "listenerC", ListenerEvent.before, 1);

        Listener listenerD = conditionB.getListeners().get(1);
        assertListener(listenerD, conditionB, "listenerD", ListenerEvent.success, 1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 2, 0, conditionB);

        Listener listenerE = actionA.getListeners().get(0);
        assertListener(listenerE, actionA, "listenerE", ListenerEvent.before, 1);

        Listener listenerF = actionA.getListeners().get(1);
        assertListener(listenerF, actionA, "listenerF", ListenerEvent.success, 1);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 2, conditionB);

        Listener listenerI = exclusiveGateway.getListeners().get(0);
        assertListener(listenerI, exclusiveGateway, "listenerI", ListenerEvent.before, 1);

        Listener listenerJ = exclusiveGateway.getListeners().get(1);
        assertListener(listenerJ, exclusiveGateway, "listenerJ", ListenerEvent.success, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 2, 0, exclusiveGateway);

        Listener listenerG = conditionC.getListeners().get(0);
        assertListener(listenerG, conditionC, "listenerG", ListenerEvent.before, 1);

        Listener listenerH = conditionC.getListeners().get(1);
        assertListener(listenerH, conditionC, "listenerH", ListenerEvent.success, 1);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 2, 0, conditionB);

        Listener listenerK = conditionD.getListeners().get(0);
        assertListener(listenerK, conditionD, "listenerK", ListenerEvent.before, 1);

        Listener listenerL = conditionD.getListeners().get(1);
        assertListener(listenerL, conditionD, "listenerL", ListenerEvent.success, 1);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 2, 0, conditionD);

        Listener listenerM = actionB.getListeners().get(0);
        assertListener(listenerM, actionB, "listenerM", ListenerEvent.before, 1);

        Listener listenerN = actionB.getListeners().get(1);
        assertListener(listenerN, actionB, "listenerN", ListenerEvent.success, 1);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 2, conditionC, actionB);

        Listener listenerO = joinGateway.getListeners().get(0);
        assertListener(listenerO, joinGateway, "listenerO", ListenerEvent.before, 1);

        Listener listenerP = joinGateway.getListeners().get(1);
        assertListener(listenerP, joinGateway, "listenerP", ListenerEvent.success, 1);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 2, 0, joinGateway);

        Listener listenerQ = actionC.getListeners().get(0);
        assertListener(listenerQ, actionC, "listenerQ", ListenerEvent.before, 1);

        Listener listenerR = actionC.getListeners().get(1);
        assertListener(listenerR, actionC, "listenerR", ListenerEvent.success, 1);

        Action actionD = (Action) subRule.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 2, 0, subRule);

        Listener listenerU = actionD.getListeners().get(0);
        assertListener(listenerU, actionD, "listenerU", ListenerEvent.before, 1);

        Listener listenerV = actionD.getListeners().get(1);
        assertListener(listenerV, actionD, "listenerV", ListenerEvent.success, 1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 4, 0, 7, null);
    }

    @Test
    public void testSubThenNextedIfIfThen() {
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
    public void testSubThenNestedInIfThenElse() {
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
    public void testSubThenElseCascadeAndParallelWithAction() {
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
    public void testSubThenElseCascadeWithAction() {
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
    public void testSubThenElseContainJoinAndSelect() {
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
    public void testSubThenElseContainJoinAndSelectWithListener() {
        Rule rule = compile("{\n" +
                "    sub{\n" +
                "        if(conditionA()[listenerA(event=\"before\"), listenerB(event=\"success\")]){\n" +
                "            if(conditionB()[listenerC(event=\"before\"), listenerD(event=\"success\")]){\n" +
                "                actionA()[listenerE(event=\"before\"), listenerF(event=\"success\")]\n" +
                "            }else{\n" +
                "                join {\n" +
                "                    select{\n" +
                "                        if(conditionC()[listenerG(event=\"before\"), listenerH(event=\"success\")])&\n" +
                "                    }[listenerI(event=\"before\"), listenerJ(event=\"success\")],\n" +
                "                    if(conditionD()[listenerK(event=\"before\"), listenerL(event=\"success\")]){\n" +
                "                        actionB()[listenerM(event=\"before\"), listenerN(event=\"success\")]&\n" +
                "                    }\n" +
                "                }[listenerO(event=\"before\"), listenerP(event=\"success\")] then {\n" +
                "                    actionC()[listenerQ(event=\"before\"), listenerR(event=\"success\")]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }[listenerS(event=\"before\"), listenerT(event=\"success\")] then {\n" +
                "        actionD()[listenerU(event=\"before\"), listenerV(event=\"success\")]\n" +
                "    } else{\n" +
                "        actionE()[listenerW(event=\"before\"), listenerX(event=\"success\")]\n" +
                "    }\n" +
                "}[listenerY(event=\"before\"), listenerZ(event=\"success\")]\n");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Rule subRule = (Rule) start.getSuccessors().get(0);
        assertRule(subRule, LinkType.NORMAL, 2, 2, 20, 0, 30, start);

        Start subStart = subRule.getStart();
        assertStart(subStart, 1, 0);

        Condition conditionA = (Condition) subStart.getSuccessors().get(0);
        assertActivity(conditionA, LinkType.NORMAL, "conditionA", 0, 1, 2, 0, subStart);

        Listener listenerA = conditionA.getListeners().get(0);
        assertListener(listenerA, conditionA, "listenerA", ListenerEvent.before, 1);

        Listener listenerB = conditionA.getListeners().get(1);
        assertListener(listenerB, conditionA, "listenerB", ListenerEvent.success, 1);

        Condition conditionB = (Condition) conditionA.getSuccessors().get(0);
        assertActivity(conditionB, LinkType.TRUE, "conditionB", 0, 3, 2, 0, conditionA);

        Listener listenerC = conditionB.getListeners().get(0);
        assertListener(listenerC, conditionB, "listenerC", ListenerEvent.before, 1);

        Listener listenerD = conditionB.getListeners().get(1);
        assertListener(listenerD, conditionB, "listenerD", ListenerEvent.success, 1);

        Action actionA = (Action) conditionB.getSuccessors().get(0);
        assertActivity(actionA, LinkType.TRUE, "actionA", 0, 0, 2, 0, conditionB);

        Listener listenerE = actionA.getListeners().get(0);
        assertListener(listenerE, actionA, "listenerE", ListenerEvent.before, 1);

        Listener listenerF = actionA.getListeners().get(1);
        assertListener(listenerF, actionA, "listenerF", ListenerEvent.success, 1);

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) conditionB.getSuccessors().get(1);
        assertExclusiveGateway(exclusiveGateway, LinkType.FALSE, 1, 2, conditionB);

        Listener listenerI = exclusiveGateway.getListeners().get(0);
        assertListener(listenerI, exclusiveGateway, "listenerI", ListenerEvent.before, 1);

        Listener listenerJ = exclusiveGateway.getListeners().get(1);
        assertListener(listenerJ, exclusiveGateway, "listenerJ", ListenerEvent.success, 1);

        Condition conditionC = (Condition) exclusiveGateway.getSuccessors().get(0);
        assertActivity(conditionC, LinkType.NORMAL, "conditionC", 0, 1, 2, 0, exclusiveGateway);

        Listener listenerG = conditionC.getListeners().get(0);
        assertListener(listenerG, conditionC, "listenerG", ListenerEvent.before, 1);

        Listener listenerH = conditionC.getListeners().get(1);
        assertListener(listenerH, conditionC, "listenerH", ListenerEvent.success, 1);

        Condition conditionD = (Condition) conditionB.getSuccessors().get(2);
        assertActivity(conditionD, LinkType.FALSE, "conditionD", 0, 1, 2, 0, conditionB);

        Listener listenerK = conditionD.getListeners().get(0);
        assertListener(listenerK, conditionD, "listenerK", ListenerEvent.before, 1);

        Listener listenerL = conditionD.getListeners().get(1);
        assertListener(listenerL, conditionD, "listenerL", ListenerEvent.success, 1);

        Action actionB = (Action) conditionD.getSuccessors().get(0);
        assertActivity(actionB, LinkType.TRUE, "actionB", 0, 1, 2, 0, conditionD);

        Listener listenerM = actionB.getListeners().get(0);
        assertListener(listenerM, actionB, "listenerM", ListenerEvent.before, 1);

        Listener listenerN = actionB.getListeners().get(1);
        assertListener(listenerN, actionB, "listenerN", ListenerEvent.success, 1);

        JoinGateway joinGateway = (JoinGateway) conditionC.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 2, conditionC, actionB);

        Listener listenerO = joinGateway.getListeners().get(0);
        assertListener(listenerO, joinGateway, "listenerO", ListenerEvent.before, 1);

        Listener listenerP = joinGateway.getListeners().get(1);
        assertListener(listenerP, joinGateway, "listenerP", ListenerEvent.success, 1);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 2, 0, joinGateway);

        Listener listenerQ = actionC.getListeners().get(0);
        assertListener(listenerQ, actionC, "listenerQ", ListenerEvent.before, 1);

        Listener listenerR = actionC.getListeners().get(1);
        assertListener(listenerR, actionC, "listenerR", ListenerEvent.success, 1);

        Action actionD = (Action) subRule.getSuccessors().get(0);
        assertActivity(actionD, LinkType.TRUE, "actionD", 0, 0, 2, 0, subRule);

        Listener listenerU = actionD.getListeners().get(0);
        assertListener(listenerU, actionD, "listenerU", ListenerEvent.before, 1);

        Listener listenerV = actionD.getListeners().get(1);
        assertListener(listenerV, actionD, "listenerV", ListenerEvent.success, 1);

        Action actionE = (Action) subRule.getSuccessors().get(1);
        assertActivity(actionE, LinkType.FALSE, "actionE", 0, 0, 2, 0, subRule);

        Listener listenerW = actionE.getListeners().get(0);
        assertListener(listenerW, actionE, "listenerW", ListenerEvent.before, 1);

        Listener listenerX = actionE.getListeners().get(1);
        assertListener(listenerX, actionE, "listenerX", ListenerEvent.success, 1);

        assertRule(rule, LinkType.NORMAL, 0, 2, 6, 0, 10, null);
    }

    @Test
    public void testSubThenElseNestedInIfThen() {
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
    public void testSubThenElseNestedInIfThenElse() {
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
}
