package com.github.liuyehcf.framework.flow.engine.dsl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.model.*;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.activity.DefaultAction;
import com.github.liuyehcf.framework.flow.engine.model.activity.DefaultCondition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.*;
import com.github.liuyehcf.framework.flow.engine.model.listener.DefaultListener;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerScope;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class CompilerContext extends Context {

    private final LinkedList<FlowSegment> flowStack;
    private final IDGenerator idGenerator;


    public CompilerContext(Context context, LinkedList<FlowSegment> flowStack, IDGenerator idGenerator) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.flowStack = flowStack;
        this.idGenerator = idGenerator;
    }

    /**
     * 设置属性
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @param attrName    属性名
     * @param value       属性值
     */
    public void setAttr(int stackOffset, AttrName attrName, Object value) {
        Assert.assertNotNull(attrName, "attrName");
        getStack().get(stackOffset).put(attrName.name(), value);
    }

    /**
     * 为产生之左部的语法树节点设置属性
     *
     * @param attrName 属性名
     * @param value    属性值
     */
    public void setAttrToLeftNode(AttrName attrName, Object value) {
        Assert.assertNotNull(attrName, "attrName");
        getLeftNode().put(attrName.name(), value);
    }

    /**
     * 获取属性值
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @param attrName    属性名
     * @param <T>         属性值类型
     * @return 属性值
     */
    public <T> T getAttr(int stackOffset, AttrName attrName) {
        Assert.assertNotNull(attrName, "attrName");
        return getStack().get(stackOffset).get(attrName.name());
    }

    /**
     * 获取语法树节点的词法值，该值由词法分析器提供
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @return 词法值
     */
    public String getValue(int stackOffset) {
        return getStack().get(stackOffset).getValue();
    }

    public Action addAction(String actionName, List<String> argumentNameList, List<Object> argumentValueList) {
        FlowSegment flow = peekFlow();
        Node preNode = flow.peekNode();

        Action action;
        if (argumentNameList == null && argumentValueList == null) {
            action = new DefaultAction(
                    generateId(),
                    actionName,
                    new String[0],
                    new Object[0]
            );
        } else if (argumentNameList == null || argumentValueList == null) {
            throw new NullPointerException("argumentNameList or argumentValueList is null");
        } else {
            action = new DefaultAction(
                    generateId(),
                    actionName,
                    argumentNameList.toArray(new String[0]),
                    argumentValueList.toArray(new Object[0])
            );
        }

        preNode.addSuccessor(action, flow.peekLinkType());
        action.addPredecessor(preNode);

        bindFlow(action, flow);
        pushNode(action);
        pushLinkType(LinkType.NORMAL);

        return action;
    }

    public Condition addCondition(String conditionName, List<String> argumentNameList, List<Object> argumentValueList) {
        FlowSegment flow = peekFlow();

        Condition condition;
        if (argumentNameList == null && argumentValueList == null) {
            condition = new DefaultCondition(
                    generateId(),
                    conditionName,
                    new String[0],
                    new Object[0]
            );
        } else if (argumentNameList == null || argumentValueList == null) {
            throw new NullPointerException("argumentNameList or argumentValueList is null");
        } else {
            condition = new DefaultCondition(
                    generateId(),
                    conditionName,
                    argumentNameList.toArray(new String[0]),
                    argumentValueList.toArray(new Object[0])
            );
        }

        Node preNode = flow.peekNode();

        preNode.addSuccessor(condition, flow.peekLinkType());
        condition.addPredecessor(preNode);

        bindFlow(condition, flow);
        pushNode(condition);

        return condition;
    }

    public Listener addListener(String listenerName, List<String> argumentNameList, List<Object> argumentValueList) {
        Assert.assertNotNull(argumentNameList, "listener must contains argument 'event'");
        Assert.assertNotNull(argumentValueList, "listener must contains argument 'event'");

        int index = argumentNameList.indexOf(Listener.ARGUMENT_NAME_EVENT);
        Assert.assertFalse(index == -1, "listener must contains argument 'event'");
        String event = (String) argumentValueList.get(index);

        ListenerEvent listenerEvent = ListenerEvent.valueOf(event);

        FlowSegment flow = peekFlow();

        Listener listener = new DefaultListener(
                generateId(),
                flow.peekNode().getId(),
                listenerName,
                ListenerScope.node,
                listenerEvent,
                argumentNameList.toArray(new String[0]),
                argumentValueList.toArray(new Object[0])
        );

        bindFlow(listener, flow);

        return listener;
    }

    public ExclusiveGateway addExclusiveGateway() {
        FlowSegment flow = peekFlow();

        ExclusiveGateway exclusiveGateway = new DefaultExclusiveGateway(generateId());

        Node preNode = flow.peekNode();

        preNode.addSuccessor(exclusiveGateway, flow.peekLinkType());
        exclusiveGateway.addPredecessor(preNode);

        bindFlow(exclusiveGateway, flow);
        pushNode(exclusiveGateway);
        pushLinkType(LinkType.NORMAL);

        return exclusiveGateway;
    }

    public JoinGateway addJoinGateway(JoinMode joinMode) {
        FlowSegment flow = peekFlow();
        JoinScope joinScope = flow.popJoinScope();

        JoinGateway joinGateway = new DefaultJoinGateway(idGenerator.generateNumberId(), joinMode);

        Assert.assertFalse(joinScope.getJoinNodes().isEmpty(), "unreachable join gateway");

        for (Pair<Node, LinkType> joinNode : joinScope.getJoinNodes()) {
            joinNode.getKey().addSuccessor(joinGateway, joinNode.getValue());
            joinGateway.addPredecessor(joinNode.getKey());
        }

        bindFlow(joinGateway, flow);
        pushNode(joinGateway);
        pushLinkType(LinkType.NORMAL);

        return joinGateway;
    }

    public void pushFlow(boolean isSubFlow, String flowName, String flowId, LinkType joinMarkLinkType) {
        FlowSegment flow;
        if (isSubFlow) {
            LinkType linkType = peekFlow().peekLinkType();
            FlowSegment peek = peekFlow();
            String segmentFlowName = peek.getName();
            String segmentFlowId = generateId();
            flow = new FlowSegment(segmentFlowName, segmentFlowId, new Start(generateId()), linkType);
        } else {
            String segmentFlowName = flowName;
            String segmentFlowId = flowId;
            if (segmentFlowName == null) {
                segmentFlowName = generateUUID();
            }
            if (segmentFlowId == null) {
                segmentFlowId = generateUUID();
            }
            flow = new FlowSegment(segmentFlowName, segmentFlowId, new Start(generateId()), LinkType.NORMAL);
        }

        if (isSubFlow) {
            FlowSegment peekFlow = peekFlow();
            Node peekNode = peekFlow.peekNode();
            peekNode.addSuccessor(flow, flow.getLinkType());
            flow.addPredecessor(peekNode);
            pushNode(flow);
            if (joinMarkLinkType != null) {
                addJoinNode(flow, joinMarkLinkType);
            }
        }
        flowStack.push(flow);
    }

    public void bindSubFlow() {
        FlowSegment subFlow = popFlow();
        FlowSegment flow = peekFlow();
        bindFlow(subFlow, flow);
        flowStack.push(subFlow);
    }

    public FlowSegment popFlow() {
        return flowStack.pop();
    }

    private FlowSegment peekFlow() {
        FlowSegment flow = flowStack.peek();

        if (flow == null) {
            throw new NullPointerException();
        }

        return flow;
    }

    public void pushJoinScope() {
        FlowSegment flow = peekFlow();
        flow.pushJoinScope(new JoinScope());
    }

    private JoinScope peekJoinScope() {
        FlowSegment flow = peekFlow();
        JoinScope joinScope = flow.peekJoinScope();

        if (joinScope == null) {
            throw new NullPointerException();
        }

        return joinScope;
    }

    public void addJoinNode(Node node, LinkType linkType) {
        Assert.assertNotNull(node, "node");
        Assert.assertNotNull(linkType, "linkType");
        peekJoinScope().addJoinNode(node, linkType);
    }

    public void pushLinkType(LinkType linkType) {
        FlowSegment flow = peekFlow();
        flow.pushLinkType(linkType);
    }

    public void popLinkType() {
        peekFlow().popLinkType();
    }

    private void pushNode(Node node) {
        FlowSegment flow = peekFlow();
        flow.pushNode(node);
    }

    public void popNode() {
        peekFlow().popNode();
    }

    private String generateId() {
        return idGenerator.generateNumberId();
    }

    private String generateUUID() {
        return IDGenerator.generateUuid();
    }

    private void bindFlow(Element element, Flow flow) {
        element.bindFlow(flow);
    }
}
