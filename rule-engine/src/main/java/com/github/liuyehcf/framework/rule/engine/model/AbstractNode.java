package com.github.liuyehcf.framework.rule.engine.model;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public abstract class AbstractNode extends AbstractElement implements Node {

    private final LinkType linkType;
    private final List<Listener> listeners = Lists.newCopyOnWriteArrayList();
    private final List<Node> predecessors = Lists.newCopyOnWriteArrayList();
    private final List<Node> successors = Lists.newCopyOnWriteArrayList();

    protected AbstractNode(String id, LinkType linkType) {
        super(id);
        Assert.assertNotNull(linkType, "linkType");
        this.linkType = linkType;
    }

    @Override
    public final LinkType getLinkType() {
        return linkType;
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
    public final void addPredecessor(Node node) {
        Assert.assertNotNull(node, "node");
        predecessors.add(node);
    }

    @Override
    public final void removePredecessor(Node node) {
        Assert.assertNotNull(node, "node");
        predecessors.remove(node);
    }

    @Override
    public final void addSuccessor(Node node) {
        Assert.assertNotNull(node, "node");
        successors.add(node);
    }

    @Override
    public final void removeSuccessor(Node node) {
        Assert.assertNotNull(node, "node");
        successors.remove(node);
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
}
