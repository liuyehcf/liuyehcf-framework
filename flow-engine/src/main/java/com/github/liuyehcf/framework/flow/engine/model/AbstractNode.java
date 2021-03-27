package com.github.liuyehcf.framework.flow.engine.model;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public abstract class AbstractNode extends AbstractElement implements Node {

    private final List<Listener> listeners = Lists.newCopyOnWriteArrayList();
    private final List<Node> predecessors = Lists.newCopyOnWriteArrayList();
    private final List<Node> successors = Lists.newCopyOnWriteArrayList();
    private final Map<LinkType, List<Node>> successorMap = Maps.newConcurrentMap();
    private final Map<Node, LinkType> successorLinkTypeMap = Maps.newHashMap();

    protected AbstractNode(String id) {
        super(id);
        for (LinkType linkType : LinkType.values()) {
            successorMap.put(linkType, Lists.newCopyOnWriteArrayList());
        }
    }

    @Override
    public final void addListener(Listener listener) {
        Assert.assertNotNull(listener, "listener");
        listeners.add(listener);
    }

    @Override
    public final void removeListener(Listener listener) {
        Assert.assertNotNull(listener, "listener");
        listeners.remove(listener);
    }

    @Override
    public final void addPredecessor(Node predecessor) {
        Assert.assertNotNull(predecessor, "node");
        predecessors.add(predecessor);
    }

    @Override
    public final void removePredecessor(Node predecessor) {
        Assert.assertNotNull(predecessor, "node");
        predecessors.remove(predecessor);
    }

    @Override
    public final void addSuccessor(Node successor, LinkType linkType) {
        Assert.assertNotNull(successor, "node");
        Assert.assertNotNull(linkType, "linkType");
        successors.add(successor);
        if (!successorMap.containsKey(linkType)) {
            successorMap.putIfAbsent(linkType, Lists.newCopyOnWriteArrayList());
        }
        successorMap.get(linkType).add(successor);
        successorLinkTypeMap.put(successor, linkType);
    }

    @Override
    public final void removeSuccessor(Node successor) {
        Assert.assertNotNull(successor, "node");
        successors.remove(successor);
        for (List<Node> nodes : successorMap.values()) {
            nodes.remove(successor);
        }
        successorLinkTypeMap.remove(successor);
    }

    @Override
    public final List<Listener> getListeners() {
        return listeners;
    }

    @Override
    public final List<Node> getPredecessors() {
        return predecessors;
    }

    @Override
    public final List<Node> getSuccessors() {
        return successors;
    }

    @Override
    public final List<Node> getSuccessorsOf(LinkType linkType) {
        return successorMap.get(linkType);
    }

    @Override
    public final LinkType getLinkTypeOf(Node successor) {
        return successorLinkTypeMap.get(successor);
    }
}
