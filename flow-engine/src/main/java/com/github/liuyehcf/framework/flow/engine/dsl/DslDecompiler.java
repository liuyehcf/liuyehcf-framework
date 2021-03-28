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
     * ancestor node -> ancestor list
     */
    private final Map<Node, List<Ancestor>> ancestorAncestorsMap = Maps.newHashMap();

    /**
     * joinGateway -> joinGateway's ancestor
     */
    private final Map<JoinGateway, Ancestor> joinGatewayAncestorMap = Maps.newHashMap();

    /**
     * ancestor joinGateway -> offspring joinGateway
     */
    private final Map<JoinGateway, JoinGateway> ancestorJoinGatewayOffSpringJoinGatewayMap = Maps.newHashMap();

    /**
     * reached nodes
     */
    private final Set<Node> reachedNodes = Sets.newHashSet();

    private final int identStepWidth;
    private final String identStep;
    private String ident;

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

        int count = (int) flow.getElements().stream()
                .filter(element -> element instanceof Node)
                .filter(element -> !(element instanceof Start))
                .count();
        Assert.assertEquals(count, reachedNodes.size(), "some nodes are not traversed");
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
                if (isLinked(ancestor.getAncestor(), ancestor.getJoinGateway())) {
                    ancestorJoinGatewayOffSpringJoinGatewayMap.put((JoinGateway) ancestor.getAncestor(), ancestor.getJoinGateway());
                }
            } else if (!ancestor.containsSingleLinkType()) {
                Node ancestorNode = ancestor.getAncestor();
                // non joinGateway has only one predecessor
                ancestor = new Ancestor(joinGateway, ancestorNode.getPredecessors().get(0), Sets.newHashSet(ancestorNode));
            }

            ancestor.buildRelatedLinkType();
            ancestorAncestorsMap.putIfAbsent(ancestor.getAncestor(), Lists.newArrayList());
            ancestorAncestorsMap.get(ancestor.getAncestor()).add(ancestor);
            joinGatewayAncestorMap.put(ancestor.getJoinGateway(), ancestor);
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

    private void handleSuccessorsOf(Node node, LinkType linkType) {
        List<Ancestor> ancestors = ancestorAncestorsMap.get(node);

        // direct cascade ancestors may reached early, so wo filter those ancestors to avoiding reaching repeatedly
        if (CollectionUtils.isNotEmpty(ancestors)) {
            ancestors = ancestors.stream()
                    .filter(ancestor -> Objects.equals(ancestor.getLinkType(), linkType))
                    .filter(ancestor -> !reachedNodes.contains(ancestor.getJoinGateway()))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(ancestors)) {
            List<Node> successors = node.getSuccessorsOf(linkType);
            calculateFirstAndLastIndex(ancestors, successors);

            // now, we just guarantee there is no overlap between groups
            List<List<Ancestor>> ancestorGroups = getSortedNonOverlapAncestorGroups(ancestors);
            List<Node> unRelatedSuccessors = Lists.newArrayList(successors);
            ancestorGroups.forEach(group -> group.forEach(ancestor -> unRelatedSuccessors.removeAll(ancestor.getRelatedSuccessors())));
            handleAncestorGroups(ancestorGroups, unRelatedSuccessors);
        } else {
            List<Node> successors = node.getSuccessorsOf(linkType);
            handleSuccessors(successors);
        }
    }

    private void calculateFirstAndLastIndex(List<Ancestor> ancestors, List<Node> successors) {
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
        }
    }

    private void handleAncestorGroups(List<List<Ancestor>> ancestorGroups, List<Node> unRelatedSuccessors) {
        if (CollectionUtils.isEmpty(ancestorGroups)) {
            return;
        }

        for (int i = 0; i < ancestorGroups.size(); i++) {
            List<Ancestor> ancestorGroup = ancestorGroups.get(i);

            handleAncestorGroup(ancestorGroup);

            if (i != ancestorGroups.size() - 1) {
                appendln(false, COMMA);
            }
        }

        // last part
        if (CollectionUtils.isNotEmpty(unRelatedSuccessors)) {
            appendln(false, COMMA);
            handleSuccessors(Lists.newArrayList(unRelatedSuccessors));
        } else {
            appendln(false);
        }
    }

    private void handleAncestorGroup(List<Ancestor> ancestorGroup) {
        Ancestor firstAncestor = ancestorGroup.get(0);

        // first ancestor just need to process distinct related successors
        // because overlap related successors will be process recursively
        List<Node> distinctRelatedSuccessors = Lists.newArrayList(firstAncestor.getRelatedSuccessors());
        for (int i = 1; i < ancestorGroup.size(); i++) {
            distinctRelatedSuccessors.removeAll(ancestorGroup.get(i).getRelatedSuccessors());
        }

        List<Ancestor> subAncestors = ancestorGroup.subList(1, ancestorGroup.size());
        handleAncestor(firstAncestor, subAncestors, distinctRelatedSuccessors);
    }

    private void handleAncestor(Ancestor ancestor, List<Ancestor> subAncestors, List<Node> distinctRelatedSuccessors) {
        List<Ancestor> directCascadeAncestorGroup = Lists.newArrayList();
        directCascadeAncestorGroup.add(ancestor);
        Ancestor iter = ancestor;
        JoinGateway offspringJoinGateway;
        while ((offspringJoinGateway = ancestorJoinGatewayOffSpringJoinGatewayMap.get(iter.getJoinGateway())) != null) {
            Ancestor offSpringAncestor = joinGatewayAncestorMap.get(offspringJoinGateway);
            directCascadeAncestorGroup.add(0, offSpringAncestor);
            iter = offSpringAncestor;
        }

        handleDirectCascadeAncestorGroup(directCascadeAncestorGroup, 0,
                getSortedNonOverlapAncestorGroups(subAncestors), distinctRelatedSuccessors);
    }

    private void handleDirectCascadeAncestorGroup(List<Ancestor> directCascadeAncestorGroup, int index, List<List<Ancestor>> nextNonOverlapAncestorGroups, List<Node> distinctRelatedSuccessors) {
        if (index >= directCascadeAncestorGroup.size()) {
            return;
        }
        Ancestor ancestor = directCascadeAncestorGroup.get(index);
        // join gateway and related successors
        JoinGateway joinGateway = ancestor.getJoinGateway();
        reachedNodes.add(joinGateway);
        appendln(true, KEY_WORD_JOIN, getJoinModeOf(joinGateway), BIG_LEFT_PARENTHESES);
        increaseIdent();

        // first processing direct cascade ancestors recursive
        handleDirectCascadeAncestorGroup(directCascadeAncestorGroup, index + 1, nextNonOverlapAncestorGroups, distinctRelatedSuccessors);
        if (CollectionUtils.isNotEmpty(distinctRelatedSuccessors)) {
            // avoid reaching multiply times by direct cascade ancestors
            if (!reachedNodes.containsAll(distinctRelatedSuccessors)) {
                handleSuccessors(distinctRelatedSuccessors);
            } else {
                appendln(false);
            }
        }

        // inner of the direct cascade ancestor group
        if (index == directCascadeAncestorGroup.size() - 1) {
            // recursive processing of remaining ancestor in group
            handleAncestorGroups(nextNonOverlapAncestorGroups, null);
        }

        decreaseIdent();
        append(true, BIG_RIGHT_PARENTHESES, getNormalListenersOf(joinGateway), getJoinMarkOf(joinGateway));
        if (hasNonJoinGatewaySuccessors(joinGateway, LinkType.NORMAL)) {
            appendln(false, SPACE, KEY_WORD_THEN, SPACE, BIG_LEFT_PARENTHESES);
            increaseIdent();
            handleSuccessorsOf(joinGateway, LinkType.NORMAL);
            decreaseIdent();
            append(true, BIG_RIGHT_PARENTHESES);
        }
    }

    private void handleSuccessors(List<Node> successors) {
        successors = successors.stream()
                .filter((successor) -> !ElementType.JOIN_GATEWAY.equals(successor.getType()))
                .collect(Collectors.toList());

        int size = successors.size();
        int lastIndex = size - 1;
        for (int i = 0; i < size; i++) {
            Node successor = successors.get(i);
            handleBaseType(successor);

            if (i != lastIndex) {
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
        reachedNodes.add(action);
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
        reachedNodes.add(condition);
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

        reachedNodes.add(exclusiveGateway);
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

        reachedNodes.add(subFlow);
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

    private List<List<Ancestor>> getSortedNonOverlapAncestorGroups(List<Ancestor> ancestors) {
        List<List<Ancestor>> ancestorGroups = Lists.newArrayList();
        for (Ancestor ancestor : ancestors) {
            List<Ancestor> overlapGroup = getOverlapGroup(ancestorGroups, ancestor);
            if (overlapGroup != null) {
                overlapGroup.add(ancestor);
                overlapGroup.sort((ancestor1, ancestor2) -> ancestor2.getRelatedSuccessors().size() - ancestor1.getRelatedSuccessors().size());
            } else {
                ancestorGroups.add(Lists.newArrayList(ancestor));
            }
        }

        while (true) {
            ancestorGroups.sort(Comparator.comparingInt(group -> group.get(0).getFirstIndexOfSuccessor()));
            boolean isGroupNotOverlap = false;
            for (int i = 0; i < ancestorGroups.size() - 1; i++) {
                List<Ancestor> group1 = ancestorGroups.get(i);
                List<Ancestor> group2 = ancestorGroups.get(i + 1);
                Ancestor firstAncestorOfGroup1 = group1.get(0);
                Ancestor firstAncestorOfGroup2 = group2.get(0);

                if (isOverlap(firstAncestorOfGroup1, firstAncestorOfGroup2)) {
                    isGroupNotOverlap = true;
                    group1.addAll(group2);
                    group1.sort((ancestor1, ancestor2) -> ancestor2.getRelatedSuccessors().size() - ancestor1.getRelatedSuccessors().size());
                    ancestorGroups.remove(i + 1);
                    break;
                }
            }
            if (!isGroupNotOverlap) {
                break;
            }
        }

        ancestorGroups.sort(Comparator.comparingInt(group -> group.get(0).getFirstIndexOfSuccessor()));
        return ancestorGroups;
    }

    private List<Ancestor> getOverlapGroup(List<List<Ancestor>> nonOverlapAncestorGroups, Ancestor ancestor) {
        for (List<Ancestor> nonOverlapAncestorGroup : nonOverlapAncestorGroups) {
            Ancestor firstAncestor = nonOverlapAncestorGroup.get(0);
            if (isOverlap(firstAncestor, ancestor)) {
                return nonOverlapAncestorGroup;
            }
        }

        return null;
    }

    private boolean isLinked(Node leftNode, Node rightNode) {
        return leftNode.getSuccessors().contains(rightNode)
                && rightNode.getPredecessors().contains(leftNode);
    }

    private boolean isOverlap(Ancestor ancestor1, Ancestor ancestor2) {
        return ancestor1.getRelatedSuccessors().containsAll(ancestor2.getRelatedSuccessors())
                || ancestor2.getRelatedSuccessors().containsAll(ancestor1.getRelatedSuccessors());
    }

    private boolean hasNonJoinGatewaySuccessors(Node node, LinkType linkType) {
        List<Node> successors = node.getSuccessorsOf(linkType);
        return successors.stream().anyMatch((successor) -> !ElementType.JOIN_GATEWAY.equals(successor.getType()));
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

    private void appendln(boolean needIdent, Object... contents) {
        append(needIdent, contents)
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

        private Ancestor(JoinGateway joinGateway, Node ancestor, Set<Node> relatedSuccessors) {
            this.joinGateway = joinGateway;
            this.ancestor = ancestor;
            this.relatedSuccessors = relatedSuccessors;
        }

        public boolean containsSingleLinkType() {
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
    }
}
