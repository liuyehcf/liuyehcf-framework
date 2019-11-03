package com.github.liuyehcf.framework.rule.engine.test.dsl.topology;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.rule.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.rule.engine.model.*;
import com.github.liuyehcf.framework.rule.engine.model.activity.Activity;
import com.github.liuyehcf.framework.rule.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import org.junit.Assert;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public class TestBaseTopology {

    Rule compile(String input) {
        System.out.println(input);
        CompileResult<Rule> result = DslCompiler.getInstance().compile(input);

        if (result.getError() != null) {
            result.getError().printStackTrace();
        }
        Assert.assertTrue(result.isSuccess());

        return result.getResult();
    }

    void assertStart(Start start, int successorSize, int listenerSize) {
        Assert.assertEquals(0, start.getPredecessors().size());
        Assert.assertEquals(successorSize, start.getSuccessors().size());
        Assert.assertEquals(listenerSize, start.getListeners().size());
    }

    void assertRule(Rule rule, LinkType linkType, int successorSize, int endNum, int listenerSize, int eventSize, int elementSize, Node predecessor) {
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
                    Assert.assertTrue(listener.getScope().isGlobal());
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

    void assertActivity(Activity activity, LinkType linkType, String name, int argumentLength, int successorSize, int listenerSize, int eventSize, Node predecessor) {
        Assert.assertEquals(linkType, activity.getLinkType());
        Assert.assertEquals(name, activity.getName());
        Assert.assertEquals(argumentLength, activity.getArgumentNames().length);
        Assert.assertEquals(argumentLength, activity.getArgumentValues().length);
        Assert.assertEquals(1, activity.getPredecessors().size());
        Assert.assertEquals(successorSize, activity.getSuccessors().size());
        Assert.assertEquals(listenerSize, activity.getListeners().size());
        Assert.assertEquals(activity.getPredecessors().get(0), predecessor);
    }

    void assertListener(Listener listener, Node attachedNode, String name, ListenerEvent event, int argumentLength) {
        Assert.assertEquals(attachedNode.getId(), listener.getAttachedId());
        Assert.assertEquals(name, listener.getName());
        Assert.assertEquals(event, listener.getEvent());
        Assert.assertEquals(argumentLength, listener.getArgumentNames().length);
        Assert.assertEquals(argumentLength, listener.getArgumentValues().length);
    }

    void assertJoinGateway(JoinGateway gateway, int predecessorSize, int successorSize, int listenerSize, Node... predecessors) {
        Assert.assertEquals(predecessorSize, gateway.getPredecessors().size());
        Assert.assertEquals(successorSize, gateway.getSuccessors().size());
        Assert.assertEquals(listenerSize, gateway.getListeners().size());
        Assert.assertEquals(predecessorSize, predecessors.length);

        for (int i = 0; i < predecessors.length; i++) {
            Assert.assertEquals(gateway.getPredecessors().get(i), predecessors[i]);
            Assert.assertEquals(predecessors[i].getSuccessors().get(0), gateway);
        }
    }

    void assertExclusiveGateway(ExclusiveGateway gateway, LinkType linkType, int successorSize, int listenerSize, Node predecessor) {
        Assert.assertEquals(linkType, gateway.getLinkType());
        Assert.assertEquals(1, gateway.getPredecessors().size());
        Assert.assertEquals(successorSize, gateway.getSuccessors().size());
        Assert.assertEquals(listenerSize, gateway.getListeners().size());
        Assert.assertEquals(gateway.getPredecessors().get(0), predecessor);
    }
}
