package com.github.liuyehcf.framework.flow.engine.model;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.event.Event;
import com.github.liuyehcf.framework.flow.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class DefaultFlow extends AbstractNode implements Flow {

    private static final long serialVersionUID = 1370445351118297675L;

    private final String name;
    private final Start start;
    private final List<Node> ends = Lists.newArrayList();
    private final List<Event> events = Lists.newArrayList();
    private final Map<String, Element> elements = Maps.newHashMap();
    private final AtomicBoolean init = new AtomicBoolean(false);

    public DefaultFlow(String name, String id, Start start) {
        this(name, id, LinkType.NORMAL, start);
    }

    public DefaultFlow(String name, String id, LinkType linkType, Start start) {
        super(id, linkType == null ? LinkType.NORMAL : linkType);
        this.name = name;
        this.start = start;
        start.bindFlow(this);
    }

    @Override
    public final ElementType getType() {
        return ElementType.SUB_FLOW;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Start getStart() {
        return start;
    }

    @Override
    public final List<Node> getEnds() {
        return ends;
    }

    @Override
    public final void addEvent(Event event) {
        events.add(event);
    }

    @Override
    public final void removeEvent(Event event) {
        events.remove(event);
    }

    @Override
    public final List<Event> getEvents() {
        return events;
    }

    @Override
    public final void addElement(Element element) {
        Assert.assertFalse(elements.containsKey(element.getId()), "duplicate element key");
        elements.put(element.getId(), element);
    }

    @Override
    public final void removeElement(Element element) {
        elements.remove(element.getId());
    }

    @Override
    public final Element getElement(String id) {
        return elements.get(id);
    }

    @Override
    public final Collection<Element> getElements() {
        return elements.values();
    }

    @Override
    public final void init() {
        if (init.compareAndSet(false, true)) {
            checkUnreachableNodes();
            checkCycle();
            checkLink();
            checkListener();
            recordEnds();
        }
    }

    private void checkUnreachableNodes() {
        Set<String> visited = Sets.newHashSet();

        LinkedList<Node> stack = Lists.newLinkedList();

        stack.push(start);
        visited.add(start.getId());

        while (!stack.isEmpty()) {
            Node top = stack.pop();

            for (Node successor : top.getSuccessors()) {
                if (visited.add(successor.getId())) {
                    stack.push(successor);
                }
            }
        }

        List<String> nodes = elements.values().stream()
                .filter(e -> e instanceof Node)
                .map(Element::getId)
                .collect(Collectors.toList());

        Assert.assertEquals(nodes.size(), visited.size(), () -> {
            Set<String> remainIds = Sets.newHashSet(nodes);
            remainIds.removeAll(visited);
            return String.format("remain unreachable nodes: %s", JSON.toJSONString(remainIds));
        });
    }

    private void checkCycle() {
        Set<String> visited = Sets.newHashSet();

        checkCycle(start, visited);
    }

    private void checkCycle(Node node, Set<String> visited) {
        Assert.assertTrue(visited.add(node.getId()), "cycle exists");

        for (Node successor : node.getSuccessors()) {
            checkCycle(successor, visited);
        }

        visited.remove(node.getId());
    }

    private void checkLink() {
        List<Node> nodes = elements.values().stream()
                .filter(e -> e instanceof Node)
                .map(e -> (Node) e)
                .collect(Collectors.toList());

        for (Node node : nodes) {
            // check predecessor size
            if (!(node instanceof JoinGateway)) {
                Assert.assertTrue(node.getPredecessors().size() <= 1, "nodes other than joinGateway allow only one predecessor");
            }

            // check link relationship asymmetry
            for (Node predecessor : node.getPredecessors()) {
                Assert.assertTrue(predecessor.getSuccessors().contains(node),
                        () -> String.format("link relationship asymmetry. predecessorId=%s, successorId=%s", predecessor.getId(), node.getId()));
            }
            for (Node successor : node.getSuccessors()) {
                Assert.assertTrue(successor.getPredecessors().contains(node),
                        () -> String.format("link relationship asymmetry. predecessorId=%s, successorId=%s", node.getId(), successor.getId()));
            }

            // check link type
            if (node instanceof Condition || node instanceof Flow) {
                for (Node successor : node.getSuccessors()) {
                    LinkType linkType = successor.getLinkType();
                    Assert.assertNotNull(linkType, "linkType");
                    Assert.assertNotEquals(LinkType.NORMAL, linkType);
                }
            } else {
                for (Node successor : node.getSuccessors()) {
                    if (successor instanceof JoinGateway) {
                        LinkType linkType = successor.getLinkType();
                        Assert.assertNotNull(linkType, "linkType");
                        Assert.assertEquals(LinkType.TRUE, linkType);
                    } else {
                        LinkType linkType = successor.getLinkType();
                        Assert.assertNotNull(linkType, "linkType");
                        Assert.assertEquals(LinkType.NORMAL, linkType);
                    }
                }
            }

            // check start
            if (node instanceof Start) {
                Assert.assertTrue(node.getPredecessors().isEmpty(), "start must have no predecessors");
            }

            // check exclusiveGateway's successor
            if (node instanceof ExclusiveGateway) {
                Assert.assertFalse(node.getSuccessors().isEmpty(), "exclusiveGateway has no successors");

                for (Node successor : node.getSuccessors()) {
                    Assert.assertTrue(successor instanceof Condition, "the type of exclusiveGateway's successor must be condition");
                }
            }
        }
    }

    private void checkListener() {
        List<Node> nodes = elements.values().stream()
                .filter(e -> e instanceof Node)
                .filter(e -> !(e instanceof Flow))
                .map(e -> (Node) e)
                .collect(Collectors.toList());

        List<Listener> nodeListeners = Lists.newArrayList();

        for (Node node : nodes) {
            for (Listener listener : node.getListeners()) {
                Assert.assertEquals(listener.getAttachedId(), node.getId(),
                        () -> String.format("listener relationship asymmetry. listenerId=%s", listener.getId()));
                nodeListeners.add(listener);
            }
        }

        int nodeListenerNum = elements.values().stream()
                .filter(e -> e instanceof Listener)
                .map(e -> (Listener) e)
                .filter(l -> l.getScope().isNode())
                .collect(Collectors.toList()).size();

        Assert.assertEquals(nodeListenerNum, nodeListeners.size(),
                () -> String.format("listener relationship asymmetry. nodeListenerNum=%s, flowNodeListenerNum=%s", nodeListeners.size(), nodeListenerNum));
    }

    private void recordEnds() {
        for (Element element : elements.values()) {
            // except exclusive gateway
            if (element instanceof Action
                    || element instanceof Condition
                    || element instanceof JoinGateway
                    || element instanceof Flow) {
                Node node = (Node) element;
                if (node.getSuccessors().isEmpty()) {
                    ends.add(node);
                }
            }
        }
    }
}
