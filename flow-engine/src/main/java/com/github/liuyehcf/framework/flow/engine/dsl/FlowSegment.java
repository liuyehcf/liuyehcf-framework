package com.github.liuyehcf.framework.flow.engine.dsl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.DefaultFlow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.google.common.collect.Lists;

import java.util.LinkedList;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
class FlowSegment extends DefaultFlow {

    private static final long serialVersionUID = 672756188946202754L;

    private final LinkType linkType;
    private final LinkedList<Node> nodeStack = Lists.newLinkedList();
    private final LinkedList<LinkType> linkTypeStack = Lists.newLinkedList();
    private final LinkedList<JoinScope> joinScopeStack = Lists.newLinkedList();

    FlowSegment(String name, String id, Start start, LinkType linkType) {
        super(name, id, start);
        Assert.assertNotNull(linkType, "linkType");
        this.linkType = linkType;
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

    LinkType getLinkType() {
        return linkType;
    }
}
