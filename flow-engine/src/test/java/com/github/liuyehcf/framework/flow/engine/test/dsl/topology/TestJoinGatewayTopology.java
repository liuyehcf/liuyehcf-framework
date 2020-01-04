package com.github.liuyehcf.framework.flow.engine.test.dsl.topology;

import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestJoinGatewayTopology extends TestBaseTopology {

    @Test
    public void testHardJoinThenWithSingleAction() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        actionA()&\n" +
                "    }then{\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 1, 1, 0, actionA);

        Action actionB = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, joinGateway);

        assertFlow(flow, LinkType.NORMAL, 0, 1, 0, 0, 4, null);
    }

    @Test
    public void testHardJoinThenWithNonSelectAction() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()\n" +
                "    }then{\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 0, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 1, 1, 0, actionA);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        assertFlow(flow, LinkType.NORMAL, 0, 2, 0, 0, 5, null);
    }

    @Test
    public void testHardJoinThenWithAllSelectedAction() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    }then{\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 2, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 1, 0, 0, start);

        Action actionB = (Action) start.getSuccessors().get(1);
        assertActivity(actionB, LinkType.NORMAL, "actionB", 0, 1, 0, 0, start);

        JoinGateway joinGateway = (JoinGateway) actionA.getSuccessors().get(0);
        assertJoinGateway(joinGateway, 2, 1, 0, actionA, actionB);

        Action actionC = (Action) joinGateway.getSuccessors().get(0);
        assertActivity(actionC, LinkType.NORMAL, "actionC", 0, 0, 0, 0, joinGateway);

        assertFlow(flow, LinkType.NORMAL, 0, 1, 0, 0, 5, null);
    }

    @Test
    public void testJoinThenWithIfThen() {
        Flow flow = compile("{\n" +
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

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 3, 0, 0, 8, null);
    }

    @Test
    public void testJoinThenWithIfThenWithListener() {
        Flow flow = compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB()\n" +
                "    } [listenerC(event=\"before\")] then {\n" +
                "        actionD(),\n" +
                "        if(conditionE()){\n" +
                "            actionF()\n" +
                "        }else{\n" +
                "            actionG()\n" +
                "        },\n" +
                "        actionH()\n" +
                "    }\n" +
                "}");

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 5, 1, 0, 10, null);
    }

    @Test
    public void testCascadeJoinThen() {
        Flow flow = compile("{\n" +
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

        Start start = flow.getStart();
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

        assertFlow(flow, LinkType.NORMAL, 0, 2, 0, 0, 12, null);
    }
}
