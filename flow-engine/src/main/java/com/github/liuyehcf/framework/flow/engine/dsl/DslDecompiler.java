package com.github.liuyehcf.framework.flow.engine.dsl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.*;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerScope;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2021/3/25
 */
public class DslDecompiler {

    private static final char SMALL_LEFT_PARENTHESES = '(';
    private static final char SMALL_RIGHT_PARENTHESES = ')';
    private static final char MIDDLE_LEFT_PARENTHESES = '[';
    private static final char MIDDLE_RIGHT_PARENTHESES = ']';
    private static final char BIG_LEFT_PARENTHESES = '{';
    private static final char BIG_RIGHT_PARENTHESES = '}';
    private static final char SPACE = ' ';
    private static final char COMMA = ',';
    private static final char DOLLAR = '$';
    private static final char BIT_AND = '&';
    private static final char BIT_OR = '|';
    private static final char BIT_REVERSE = '~';
    private static final char ASSIGN = '=';
    private static final char NEW_LINE = '\n';

    private static final String KEY_WORD_IF = "if";
    private static final String KEY_WORD_ELSE = "else";
    private static final String KEY_WORD_JOIN = "join";
    private static final String KEY_WORD_THEN = "then";
    private static final String KEY_WORD_SELECT = "select";
    private static final String KEY_WORD_SUB = "sub";

    private final Flow flow;
    private final StringBuilder buffer = new StringBuilder();

    /**
     * ancestor node -> Ancestor
     */
    private final Map<Node, List<Ancestor>> ancestorMap = Maps.newHashMap();

    /**
     * offspring joinGateway -> ancestor joinGateway
     */
    private final Map<JoinGateway, JoinGateway> joinGatewayMap = Maps.newHashMap();

    private final int identStepWidth;
    private final String identStep;
    private String ident = "";

    public DslDecompiler(Flow flow) {
        this(flow, "", 4);
    }

    private DslDecompiler(Flow flow, String ident, int identStepWidth) {
        Assert.assertNotNull(flow, "flow");
        this.flow = flow;
        Assert.assertNotNull(ident, "ident");
        this.ident = ident;
        StringBuilder identStepBuffer = new StringBuilder();
        for (int i = 0; i < identStepWidth; i++) {
            identStepBuffer.append(SPACE);
        }
        this.identStepWidth = identStepWidth;
        this.identStep = identStepBuffer.toString();
    }

    public String decompile() {
        handleRule();
        return buffer.toString();
    }

    private void handleRule() {
        parseJoinGateway();

        if (flow.getFlow() != null) {
            appendln(true, KEY_WORD_SUB, SPACE, BIG_LEFT_PARENTHESES);
        } else {
            appendln(true, BIG_LEFT_PARENTHESES);
        }
        increaseIdent();

        handleSuccessorsOf(flow.getStart(), LinkType.NORMAL);

        decreaseIdent();
        append(true, BIG_RIGHT_PARENTHESES, getGlobalListeners());
    }

    private void parseJoinGateway() {
        // find common ancestor of join gateway
        List<JoinGateway> joinGateways = flow.getElements().stream()
                .filter(element -> ElementType.JOIN_GATEWAY.equals(element.getType()))
                .map(element -> (JoinGateway) element)
                .collect(Collectors.toList());

        for (JoinGateway joinGateway : joinGateways) {
            Ancestor ancestor = findCommonAncestor(joinGateway, flow.getStart(), joinGateway.getPredecessors());

            if (ElementType.EXCLUSIVE_GATEWAY.equals(ancestor.getAncestor().getType())) {
                Node ancestorNode = ancestor.getAncestor();
                // exclusiveGateway has only one predecessor
                ancestor = new Ancestor(joinGateway, ancestorNode.getPredecessors().get(0), Sets.newHashSet(ancestorNode));
            } else if (ElementType.JOIN_GATEWAY.equals(ancestor.getAncestor().getType())) {

            } else if (!ancestor.containsSingleLinkType()) {
                Node ancestorNode = ancestor.getAncestor();
                // when method containsSingleLinkType returns false
                // it means ancestor must not be joinGateway, so it has only one predecessor
                ancestor = new Ancestor(joinGateway, ancestorNode.getPredecessors().get(0), Sets.newHashSet(ancestorNode));
            }

            ancestor.buildRelatedLinkType();
            ancestorMap.putIfAbsent(ancestor.getAncestor(), Lists.newArrayList());
            ancestorMap.get(ancestor.getAncestor()).add(ancestor);
        }
    }

    private Ancestor findCommonAncestor(JoinGateway joinGateway, Node root, List<Node> joinNodes) {
        for (Node joinNode : joinNodes) {
            if (joinNode == root) {
                return new Ancestor(joinGateway, root, Sets.newHashSet());
            }
        }

        Map<Node, Ancestor> subAncestors = Maps.newHashMap();
        for (Node successor : root.getSuccessors()) {
            subAncestors.put(successor, findCommonAncestor(joinGateway, successor, joinNodes));
        }

        List<Ancestor> nonNullSubAncestors = subAncestors.values().stream()
                .filter((ancestor) -> !Objects.equals(ancestor, Ancestor.NULL_ANCESTOR))
                .collect(Collectors.toList());

        if (nonNullSubAncestors.size() == 1) {
            return nonNullSubAncestors.get(0);
        } else if (nonNullSubAncestors.size() > 1) {
            // due to nodes can aggregated, this subAncestors may have common offspring
            Ancestor commonOffspring = findCommonOffspring(joinGateway, nonNullSubAncestors, joinNodes);
            if (commonOffspring != null) {
                return commonOffspring;
            } else {
                Set<Node> relatedSuccessors = Sets.newHashSet();
                for (Node successor : root.getSuccessors()) {
                    if (!Objects.equals(subAncestors.get(successor), Ancestor.NULL_ANCESTOR)) {
                        relatedSuccessors.add(successor);
                    }
                }
                return new Ancestor(joinGateway, root, relatedSuccessors);
            }
        } else {
            return Ancestor.NULL_ANCESTOR;
        }
    }

    private Ancestor findCommonOffspring(JoinGateway joinGateway, List<Ancestor> ancestors, List<Node> joinNodes) {
        LinkedList<List<Node>> stack = Lists.newLinkedList();

        for (Ancestor ancestor : ancestors) {
            stack.push(Lists.newArrayList(ancestor.getAncestor()));
        }

        List<List<Node>> successorsLinks = Lists.newArrayList();

        while (!stack.isEmpty()) {
            List<Node> peek = stack.peek();
            Node lastNode = peek.get(peek.size() - 1);
            if (joinNodes.contains(lastNode)) {
                stack.pop();
                successorsLinks.add(peek);
                continue;
            }

            List<Node> successors = lastNode.getSuccessors();
            if (successors.isEmpty()) {
                stack.pop();
                successorsLinks.add(peek);
            } else if (successors.size() == 1) {
                Node successor = successors.get(0);
                peek.add(successor);
            } else {
                List<Node> prototype = Lists.newArrayList(peek);
                for (int i = 0; i < successors.size(); i++) {
                    Node successor = successors.get(i);
                    if (i == 0) {
                        peek.add(successor);
                    } else {
                        ArrayList<Node> newLink = Lists.newArrayList(prototype);
                        newLink.add(successor);
                        stack.push(newLink);
                    }
                }
            }
        }

        List<Node> firstLink = successorsLinks.get(0);
        Node offspring = null;
        for (int i = firstLink.size() - 1; i >= 0; i--) {
            Node node = firstLink.get(i);
            boolean existInAllLink = true;
            for (int j = 1; j < successorsLinks.size(); j++) {
                if (!successorsLinks.get(j).contains(node)) {
                    existInAllLink = false;
                    break;
                }
            }
            if (existInAllLink) {
                offspring = node;
                break;
            }
        }

        if (offspring != null) {
            Set<Node> relatedSuccessors = Sets.newHashSet();
            List<Node> successors = offspring.getSuccessors();
            for (Node successor : successors) {
                Set<Node> allSuccessors = getAllSuccessors(successor);
                long count = joinNodes.stream()
                        .filter(allSuccessors::contains)
                        .count();

                if (count > 0) {
                    relatedSuccessors.add(successor);
                }
            }

            return new Ancestor(joinGateway, offspring, relatedSuccessors);
        }

        return null;
    }

    private Set<Node> getAllSuccessors(Node node) {
        Set<Node> allSuccessors = Sets.newHashSet();

        LinkedList<Node> stack = Lists.newLinkedList();
        stack.push(node);

        while (!stack.isEmpty()) {
            Node pop = stack.pop();
            allSuccessors.add(pop);

            pop.getSuccessors().forEach(stack::push);
        }

        return allSuccessors;
    }

    private void handleSuccessorsOf(Node node, LinkType linkType) {
        List<Ancestor> ancestors = ancestorMap.get(node);
        if (CollectionUtils.isNotEmpty(ancestors)) {
            ancestors = ancestors.stream()
                    .filter((ancestor) -> Objects.equals(ancestor.getLinkType(), linkType))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(ancestors)) {
            // ordinary means joinGateway isn't another joinGateway's ancestor
            List<Ancestor> ordinaryAncestors = Lists.newArrayList();
            LinkedList<Ancestor> stack = Lists.newLinkedList();
            ancestors.forEach(stack::push);

            // offspring joinGateway -> super joinGateway
            Map<JoinGateway, JoinGateway> superJoinGatewayMap = Maps.newHashMap();
            while (!stack.isEmpty()) {
                Ancestor ancestor = stack.pop();
                List<Ancestor> superAncestors = ancestorMap.get(ancestor.getJoinGateway());
                if (CollectionUtils.isNotEmpty(superAncestors)) {
                    superAncestors.forEach(superAncestor -> superJoinGatewayMap.put(ancestor.getJoinGateway(), superAncestor.getJoinGateway()));
                    superAncestors.forEach(stack::push);
                } else {
                    ordinaryAncestors.add(ancestor);
                }
            }

            List<Node> successors = node.getSuccessorsOf(linkType);
            parseSegmentAndSort(ordinaryAncestors, successors);
            handleJoinGateways(ordinaryAncestors, superJoinGatewayMap, successors);
        } else {
            List<Node> successors = node.getSuccessorsOf(linkType);
            handleSuccessors(successors, false);
        }
    }

    private void handleJoinGateways(List<Ancestor> ancestors, Map<JoinGateway, JoinGateway> superJoinGatewayMap, List<Node> successors) {
        List<Node> unrelatedSuccessors = Lists.newArrayList(successors);
        for (int i = 0; i < ancestors.size(); i++) {
            Ancestor ancestor = ancestors.get(i);

            unrelatedSuccessors.removeAll(ancestor.getRelatedSuccessors());
            handleJoinGateway(ancestor, Lists.newArrayList(ancestor.getRelatedSuccessors()));

            if (i != ancestors.size() - 1) {
                appendln(false, COMMA);
            }
        }

        // last part
        if (!unrelatedSuccessors.isEmpty()) {
            appendln(false, COMMA);
            handleSuccessors(unrelatedSuccessors, false);
        } else {
            appendln(false);
        }
    }

    private void handleJoinGateway(Ancestor ancestor, List<Node> relatedSuccessors) {
        // join gateway and related successors
        JoinGateway joinGateway = ancestor.getJoinGateway();
        appendln(true, KEY_WORD_JOIN, getJoinModeOf(joinGateway), BIG_LEFT_PARENTHESES);
        increaseIdent();
        handleSuccessors(relatedSuccessors, false);
        decreaseIdent();
        append(true, BIG_RIGHT_PARENTHESES, getNormalListenersOf(joinGateway));
        List<Node> joinGatewaySuccessors = joinGateway.getSuccessorsOf(LinkType.NORMAL);
        if (!joinGatewaySuccessors.isEmpty()) {
            appendln(false, SPACE, KEY_WORD_THEN, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();
            handleSuccessorsOf(joinGateway, LinkType.NORMAL);
            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }
    }

    private void handleSuccessors(List<Node> successors, boolean hasMoreSuccessors) {
        successors = successors.stream()
                .filter((successor) -> !ElementType.JOIN_GATEWAY.equals(successor.getType()))
                .collect(Collectors.toList());

        int size = successors.size();
        int lastIndex = size - 1;
        for (int i = 0; i < size; i++) {
            Node successor = successors.get(i);
            handleBaseType(successor);

            if (hasMoreSuccessors || i != lastIndex) {
                appendln(false, COMMA);
            } else {
                appendln(false);
            }
        }
    }

    private void handleBaseType(Node node) {
        switch (node.getType()) {
            case ACTION:
                handleAction((Action) node);
                break;
            case CONDITION:
                handleCondition((Condition) node);
                break;
            case EXCLUSIVE_GATEWAY:
                handleExclusiveGateway((ExclusiveGateway) node);
                break;
            case SUB_FLOW:
                handleSubFlow((Flow) node);
                break;
        }
    }

    private void handleAction(Action action) {
        append(true, action.getName(), getParamsOf(action), getNormalListenersOf(action), getJoinMarkOf(action));

        if (hasNonJoinGatewaySuccessors(action, LinkType.NORMAL)) {
            appendln(false, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();

            handleSuccessorsOf(action, LinkType.NORMAL);

            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }
    }

    private void handleCondition(Condition condition) {
        append(true, KEY_WORD_IF, SMALL_LEFT_PARENTHESES, condition.getName(), getParamsOf(condition), getNormalListenersOf(condition), SMALL_RIGHT_PARENTHESES, getJoinMarkOf(condition));

        if (hasNonJoinGatewaySuccessors(condition, LinkType.TRUE)) {
            appendln(false, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();

            handleSuccessorsOf(condition, LinkType.TRUE);

            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }

        if (hasNonJoinGatewaySuccessors(condition, LinkType.FALSE)) {
            appendln(false, SPACE, KEY_WORD_ELSE, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();

            handleSuccessorsOf(condition, LinkType.FALSE);

            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }
    }

    private void handleExclusiveGateway(ExclusiveGateway exclusiveGateway) {
        List<Node> successors = exclusiveGateway.getSuccessors();

        Assert.assertNotEmpty(successors, "exclusiveGateway must at least have one successor in dsl");

        for (Node successor : successors) {
            Assert.assertEquals(ElementType.CONDITION, successor.getType(), "only condition can be successor of exclusiveGateway in dsl");
        }

        appendln(true, KEY_WORD_SELECT, SPACE, BIG_LEFT_PARENTHESES);

        if (hasNonJoinGatewaySuccessors(exclusiveGateway, LinkType.NORMAL)) {
            increaseIdent();

            handleSuccessorsOf(exclusiveGateway, LinkType.NORMAL);

            decreaseIdent();
        }

        append(true, BIG_RIGHT_PARENTHESES, getNormalListenersOf(exclusiveGateway));
    }

    private void handleSubFlow(Flow subFlow) {
        DslDecompiler subFlowDecompiler = new DslDecompiler(subFlow, this.ident, this.identStepWidth);

        String subDsl = subFlowDecompiler.decompile();

        append(false, subDsl, getJoinMarkOf(subFlow));

        if (hasNonJoinGatewaySuccessors(subFlow, LinkType.TRUE)) {
            appendln(false, SPACE, KEY_WORD_THEN, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();

            handleSuccessorsOf(subFlow, LinkType.TRUE);

            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }

        if (hasNonJoinGatewaySuccessors(subFlow, LinkType.FALSE)) {
            appendln(false, SPACE, KEY_WORD_ELSE, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();

            handleSuccessorsOf(subFlow, LinkType.FALSE);

            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }
    }

    private void parseSegmentAndSort(List<Ancestor> ancestors, List<Node> successors) {
        for (Ancestor ancestor : ancestors) {
            Set<Node> relatedSuccessors = ancestor.getRelatedSuccessors();
            Assert.assertNotEmpty(relatedSuccessors, "joinGateway's ancestor has no related successors");

            int firstIndex = -1;
            for (int i = 0; i < successors.size(); i++) {
                Node successor = successors.get(i);
                if (relatedSuccessors.contains(successor)) {
                    firstIndex = i;
                    break;
                }
            }
            Assert.assertNotEquals(-1, firstIndex, "cannot find first index of related successor");
            ancestor.setFirstIndexOfSuccessor(firstIndex);

            int lastIndex = -1;
            for (int i = successors.size() - 1; i >= 0; i--) {
                Node successor = successors.get(i);
                if (relatedSuccessors.contains(successor)) {
                    lastIndex = i;
                    break;
                }
            }
            Assert.assertNotEquals(-1, lastIndex, "cannot find last index of related successor");
            ancestor.setLastIndexOfSuccessor(lastIndex);
        }

        ancestors.sort(Comparator.comparingInt(o -> (int) o.getFirstIndexOfSuccessor()));
    }

    private boolean hasNonJoinGatewaySuccessors(Node node, LinkType linkType) {
        List<Node> successors = node.getSuccessorsOf(linkType);
        return successors.stream().anyMatch((successor) -> !ElementType.JOIN_GATEWAY.equals(successor.getType()));
    }

    private String getJoinModeOf(JoinGateway joinGateway) {
        StringBuilder joinModeBuffer = new StringBuilder();
        joinModeBuffer.append(SPACE);
        switch (joinGateway.getJoinMode()) {
            case hard_and:
                joinModeBuffer.append(BIT_AND).append(SPACE);
                break;
            case or:
                joinModeBuffer.append(BIT_OR).append(SPACE);
                break;
        }
        return joinModeBuffer.toString();
    }

    private String getParamsOf(Executable executable) {
        StringBuilder paramBuffer = new StringBuilder();
        paramBuffer.append(SMALL_LEFT_PARENTHESES);

        String[] argumentNames = executable.getArgumentNames();
        Object[] argumentValues = executable.getArgumentValues();

        int paramNum = argumentNames.length;
        if (paramNum > 0) {
            paramBuffer.append(argumentNames[0])
                    .append(SPACE)
                    .append(ASSIGN)
                    .append(SPACE)
                    .append(valueOf(argumentValues[0]));
        }
        for (int i = 1; i < paramNum; i++) {
            paramBuffer.append(COMMA)
                    .append(SPACE)
                    .append(argumentNames[i])
                    .append(SPACE)
                    .append(ASSIGN)
                    .append(SPACE)
                    .append(valueOf(argumentValues[i]));
        }

        paramBuffer.append(SMALL_RIGHT_PARENTHESES);
        return paramBuffer.toString();
    }

    private String getJoinMarkOf(Node node) {
        List<Node> joinGateways = node.getSuccessors().stream()
                .filter((successor) -> ElementType.JOIN_GATEWAY.equals(successor.getType()))
                .collect(Collectors.toList());
        Assert.assertFalse(joinGateways.size() > 1, "successors contains more than one joinGateway");

        if (joinGateways.isEmpty()) {
            return null;
        }
        Node joinGateway = joinGateways.get(0);
        LinkType linkType = node.getLinkTypeOf(joinGateway);
        Assert.assertNotNull(linkType);
        if (linkType == LinkType.FALSE) {
            return String.format("%s%s", BIT_REVERSE, BIT_AND);
        }
        return String.valueOf(BIT_AND);
    }

    private String getNormalListenersOf(Node node) {
        return parseListeners(node.getListeners());
    }

    private String getGlobalListeners() {
        return parseListeners(flow.getListeners()
                .stream()
                .filter(listener -> ListenerScope.global.equals(listener.getScope()))
                .collect(Collectors.toList()));
    }

    private String parseListeners(List<Listener> listeners) {
        if (CollectionUtils.isEmpty(listeners)) {
            return null;
        }

        StringBuilder listenerBuffer = new StringBuilder();
        listenerBuffer.append(MIDDLE_LEFT_PARENTHESES);
        int size = listeners.size();

        for (int i = 0; i < size; i++) {
            Listener listener = listeners.get(i);
            if (i != 0) {
                listenerBuffer.append(COMMA)
                        .append(SPACE);
            }
            listenerBuffer.append(listener.getName())
                    .append(getParamsOf(listener));
        }

        listenerBuffer.append(MIDDLE_RIGHT_PARENTHESES);
        return listenerBuffer.toString();
    }

    private Object valueOf(Object object) {
        if (object instanceof String) {
            String str = (String) object;
            if (str.length() > 3
                    && str.charAt(0) == DOLLAR
                    && str.charAt(1) == BIG_LEFT_PARENTHESES
                    && str.charAt(str.length() - 1) == BIG_RIGHT_PARENTHESES) {
                return str;
            } else {
                return '\"' + str + '\"';
            }
        }
        return object;
    }

    private DslDecompiler append(boolean needIdent, Object... contents) {
        if (needIdent) {
            buffer.append(ident);
        }
        for (Object content : contents) {
            if (content != null) {
                buffer.append(content);
            }
        }
        return this;
    }

    private DslDecompiler appendln(boolean needIdent, Object... contents) {
        return append(needIdent, contents)
                .append(false, NEW_LINE);
    }

    private void increaseIdent() {
        ident += identStep;
    }

    private void decreaseIdent() {
        ident = ident.substring(0, ident.length() - identStep.length());
    }

    private static final class Ancestor {
        private static final Ancestor NULL_ANCESTOR = new Ancestor(null, null, null);

        private final JoinGateway joinGateway;
        private final Node ancestor;
        private final Set<Node> relatedSuccessors;
        private LinkType linkType;
        private int firstIndexOfSuccessor;
        private int lastIndexOfSuccessor;

        private Ancestor(JoinGateway joinGateway, Node ancestor, Set<Node> relatedSuccessors) {
            this.joinGateway = joinGateway;
            this.ancestor = ancestor;
            this.relatedSuccessors = relatedSuccessors;
        }

        public boolean containsSingleLinkType() {
            if (ElementType.JOIN_GATEWAY.equals(ancestor.getType())) {
                return true;
            }
            if (CollectionUtils.isEmpty(relatedSuccessors)) {
                return false;
            }
            return relatedSuccessors.stream()
                    .map(ancestor::getLinkTypeOf)
                    .distinct()
                    .count() == 1L;
        }

        public void buildRelatedLinkType() {
            LinkType linkType = null;
            if (CollectionUtils.isNotEmpty(relatedSuccessors)) {
                List<LinkType> linkTypes = relatedSuccessors.stream()
                        .map(ancestor::getLinkTypeOf)
                        .distinct()
                        .collect(Collectors.toList());
                Assert.assertTrue(linkTypes.size() == 1, "relatedSuccessors has more than one linkType");
                linkType = linkTypes.get(0);
            }
            this.linkType = linkType;
        }

        public JoinGateway getJoinGateway() {
            return joinGateway;
        }

        public Node getAncestor() {
            return ancestor;
        }

        public Set<Node> getRelatedSuccessors() {
            return relatedSuccessors;
        }

        public LinkType getLinkType() {
            return linkType;
        }

        public int getFirstIndexOfSuccessor() {
            return firstIndexOfSuccessor;
        }

        public void setFirstIndexOfSuccessor(int firstIndexOfSuccessor) {
            this.firstIndexOfSuccessor = firstIndexOfSuccessor;
        }

        public int getLastIndexOfSuccessor() {
            return lastIndexOfSuccessor;
        }

        public void setLastIndexOfSuccessor(int lastIndexOfSuccessor) {
            this.lastIndexOfSuccessor = lastIndexOfSuccessor;
        }
    }
}
