package com.github.liuyehcf.framework.flow.engine.test.dsl.topology;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.flow.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.flow.engine.model.*;
import com.github.liuyehcf.framework.flow.engine.model.activity.Activity;
import com.github.liuyehcf.framework.flow.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import org.junit.Assert;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public class TestBaseTopology {

    Flow compile(String input) {
        System.out.println(input);
        CompileResult<Flow> result = DslCompiler.getInstance().compile(input);

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

    void assertFlow(Flow flow, LinkType linkType, int successorSize, int endNum, int listenerSize, int eventSize, int elementSize, Node predecessor) {
        Assert.assertEquals(linkType, flow.getLinkType());
        if (predecessor != null) {
            Assert.assertEquals(1, flow.getPredecessors().size());
        } else {
            Assert.assertEquals(0, flow.getPredecessors().size());
        }
        Assert.assertEquals(successorSize, flow.getSuccessors().size());
        Assert.assertEquals(endNum, flow.getEnds().size());
        Assert.assertEquals(listenerSize, flow.getListeners().size());
        Assert.assertEquals(eventSize, flow.getEvents().size());
        Assert.assertEquals(elementSize, flow.getElements().size());
        if (predecessor != null) {
            Assert.assertEquals(flow.getPredecessors().get(0), predecessor);
        }
        if (!flow.getListeners().isEmpty()) {
            for (Listener listener : flow.getListeners()) {
                String attachedId = listener.getAttachedId();
                if (attachedId == null) {
                    Assert.assertTrue(listener.getScope().isGlobal());
                    continue;
                }
                Node attachedNode = (Node) flow.getElement(attachedId);
                Assert.assertTrue(attachedNode.getListeners().contains(listener));
            }
        }

        for (Element element : flow.getElements()) {
            Assert.assertNotNull(element.getFlow());
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
