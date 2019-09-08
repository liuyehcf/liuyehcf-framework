package com.github.liuyehcf.framework.rule.engine.dsl;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.model.*;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.activity.DefaultAction;
import com.github.liuyehcf.framework.rule.engine.model.activity.DefaultCondition;
import com.github.liuyehcf.framework.rule.engine.model.gateway.*;
import com.github.liuyehcf.framework.rule.engine.model.listener.DefaultListener;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class CompilerContext extends Context {

    private final LinkedList<RuleSegment> ruleStack;
    private final IDGenerator idGenerator;


    public CompilerContext(Context context, LinkedList<RuleSegment> ruleStack, IDGenerator idGenerator) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.ruleStack = ruleStack;
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
        RuleSegment rule = peekRule();
        Node preNode = rule.peekNode();

        Action action;
        if (argumentNameList == null && argumentValueList == null) {
            action = new DefaultAction(
                    generateId(),
                    rule.peekLinkType(),
                    actionName,
                    new String[0],
                    new Object[0]
            );
        } else if (argumentNameList == null || argumentValueList == null) {
            throw new NullPointerException("argumentNameList or argumentValueList is null");
        } else {
            action = new DefaultAction(
                    generateId(),
                    rule.peekLinkType(),
                    actionName,
                    argumentNameList.toArray(new String[0]),
                    argumentValueList.toArray(new Object[0])
            );
        }

        preNode.addSuccessor(action);
        action.addPredecessor(preNode);

        bindRule(action, rule);
        pushNode(action);
        pushLinkType(LinkType.NORMAL);

        return action;
    }

    public Condition addCondition(String conditionName, List<String> argumentNameList, List<Object> argumentValueList) {
        RuleSegment rule = peekRule();

        Condition condition;
        if (argumentNameList == null && argumentValueList == null) {
            condition = new DefaultCondition(
                    generateId(),
                    rule.peekLinkType(),
                    conditionName,
                    new String[0],
                    new Object[0]
            );
        } else if (argumentNameList == null || argumentValueList == null) {
            throw new NullPointerException("argumentNameList or argumentValueList is null");
        } else {
            condition = new DefaultCondition(
                    generateId(),
                    rule.peekLinkType(),
                    conditionName,
                    argumentNameList.toArray(new String[0]),
                    argumentValueList.toArray(new Object[0])
            );
        }

        Node preNode = rule.peekNode();

        preNode.addSuccessor(condition);
        condition.addPredecessor(preNode);

        bindRule(condition, rule);
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

        RuleSegment rule = peekRule();

        Listener listener = new DefaultListener(
                generateId(),
                rule.peekNode().getId(),
                listenerName,
                ListenerScope.node,
                listenerEvent,
                argumentNameList.toArray(new String[0]),
                argumentValueList.toArray(new Object[0])
        );

        bindRule(listener, rule);

        return listener;
    }

    public ExclusiveGateway addExclusiveGateway() {
        RuleSegment rule = peekRule();

        ExclusiveGateway exclusiveGateway = new DefaultExclusiveGateway(generateId(), rule.peekLinkType());

        Node preNode = rule.peekNode();

        preNode.addSuccessor(exclusiveGateway);
        exclusiveGateway.addPredecessor(preNode);

        bindRule(exclusiveGateway, rule);
        pushNode(exclusiveGateway);
        pushLinkType(LinkType.NORMAL);

        return exclusiveGateway;
    }

    public JoinGateway addJoinGateway(JoinMode joinMode) {
        RuleSegment rule = peekRule();
        JoinScope joinScope = rule.popJoinScope();

        JoinGateway joinGateway = new DefaultJoinGateway(idGenerator.generateNumberId(), joinMode);

        Assert.assertFalse(joinScope.getJoinNodes().isEmpty(), "unreachable join gateway");

        for (Node joinNode : joinScope.getJoinNodes()) {
            joinNode.addSuccessor(joinGateway);
            joinGateway.addPredecessor(joinNode);
        }

        bindRule(joinGateway, rule);
        pushNode(joinGateway);
        pushLinkType(LinkType.NORMAL);

        return joinGateway;
    }

    public Rule addSubRule() {
        RuleSegment subRule = ruleStack.pop();

        RuleSegment rule = peekRule();

        Node pointerNode = rule.peekNode();

        pointerNode.addSuccessor(subRule);
        subRule.addPredecessor(pointerNode);

        bindRule(subRule, rule);
        pushNode(subRule);

        ruleStack.push(subRule);

        return subRule;
    }

    public void pushRule(boolean isSubRule) {
        if (isSubRule) {
            LinkType linkType = peekRule().peekLinkType();
            ruleStack.push(new RuleSegment(generateUUID(), new Start(generateId()), linkType));
        } else {
            ruleStack.push(new RuleSegment(generateUUID(), new Start(generateId()), LinkType.NORMAL));
        }
    }

    public void popRule() {
        ruleStack.pop();
    }

    private RuleSegment peekRule() {
        RuleSegment rule = ruleStack.peek();

        if (rule == null) {
            throw new NullPointerException();
        }

        return rule;
    }

    public void pushJoinScope() {
        RuleSegment rule = peekRule();
        rule.pushJoinScope(new JoinScope());
    }

    private JoinScope peekJoinScope() {
        RuleSegment rule = peekRule();
        JoinScope joinScope = rule.peekJoinScope();

        if (joinScope == null) {
            throw new NullPointerException();
        }

        return joinScope;
    }

    public void addJoinNode(Node node) {
        Assert.assertNotNull(node, "node");
        peekJoinScope().addJoinNode(node);
    }

    public void pushLinkType(LinkType linkType) {
        RuleSegment rule = peekRule();
        rule.pushLinkType(linkType);
    }

    public void popLinkType() {
        peekRule().popLinkType();
    }

    private void pushNode(Node node) {
        RuleSegment rule = peekRule();
        rule.pushNode(node);
    }

    public void popNode() {
        peekRule().popNode();
    }

    private String generateId() {
        return idGenerator.generateNumberId();
    }

    private String generateUUID() {
        return IDGenerator.generateUuid();
    }

    private void bindRule(Element element, Rule rule) {
        element.bindRule(rule);
    }
}
