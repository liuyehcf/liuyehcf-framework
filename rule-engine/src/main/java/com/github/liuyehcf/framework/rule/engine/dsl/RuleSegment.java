package com.github.liuyehcf.framework.rule.engine.dsl;

import com.github.liuyehcf.framework.rule.engine.model.DefaultRule;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.google.common.collect.Lists;

import java.util.LinkedList;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
class RuleSegment extends DefaultRule {

    private static final long serialVersionUID = 672756188946202754L;

    private final LinkedList<Node> nodeStack = Lists.newLinkedList();
    private final LinkedList<LinkType> linkTypeStack = Lists.newLinkedList();
    private final LinkedList<JoinScope> joinScopeStack = Lists.newLinkedList();

    RuleSegment(String id, Start start, LinkType linkType) {
        super(null, id, linkType, start);
        nodeStack.push(start);
        linkTypeStack.push(LinkType.NORMAL);
    }

    Node peekNode() {
        return nodeStack.peek();
    }

    Node popNode() {
        return nodeStack.pop();
    }

    void pushNode(Node node) {
        nodeStack.push(node);
    }

    LinkType peekLinkType() {
        return linkTypeStack.peek();
    }

    LinkType popLinkType() {
        return linkTypeStack.pop();
    }

    void pushLinkType(LinkType linkType) {
        linkTypeStack.push(linkType);
    }

    JoinScope peekJoinScope() {
        return joinScopeStack.peek();
    }

    JoinScope popJoinScope() {
        return joinScopeStack.pop();
    }

    void pushJoinScope(JoinScope joinScope) {
        joinScopeStack.push(joinScope);
    }
}
