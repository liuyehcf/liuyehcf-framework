package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
class MarkSuccessorUnreachableOperation extends AbstractOperation<Void> {

    private final Node node;

    private final LinkType unReachableLinkType;

    MarkSuccessorUnreachableOperation(OperationContext context, Node node, LinkType unReachableLinkType) {
        super(context);
        Assert.assertNotNull(node, "node");
        Assert.assertNotNull(unReachableLinkType, "unReachableLinkType");
        this.node = node;
        this.unReachableLinkType = unReachableLinkType;
    }

    @Override
    void operate() {
        List<Node> unreachableDirectSuccessors = getDirectUnreachableSuccessors();

        Map<String, Node> unreachableSuccessors = unreachableDirectSuccessors.stream()
                .collect(Collectors.toMap(Node::getId, node -> node));

        LinkedList<Node> stack = Lists.newLinkedList(unreachableDirectSuccessors);

        while (!stack.isEmpty()) {
            Node top = stack.pop();

            // Must be marked as unreachable first,
            // otherwise the judgment of joinGateway soft_and mode will have concurrency problems.
            context.markNodeUnreachable(top);

            for (Node successor : top.getSuccessors()) {
                if (unreachableSuccessors.containsKey(successor.getId())) {
                    continue;
                }

                if (successor instanceof JoinGateway) {
                    // If the gateway has the possibility of arrival
                    if (!isJoinGatewayUnreachable((JoinGateway) successor)) {
                        continue;
                    }
                }

                unreachableSuccessors.put(successor.getId(), successor);
                stack.push(successor);
            }
        }

        context.executeAsync(new FinishOperation(this.context));
    }

    private List<Node> getDirectUnreachableSuccessors() {
        List<Node> unreachableDirectSuccessors = Lists.newArrayList();

        for (Node successor : node.getSuccessors()) {
            if (Objects.equals(unReachableLinkType, successor.getLinkType())) {
                if (successor instanceof JoinGateway) {
                    if (!isJoinGatewayUnreachable((JoinGateway) successor)) {
                        continue;
                    }
                }

                unreachableDirectSuccessors.add(successor);
            }
        }

        return unreachableDirectSuccessors;
    }

    private boolean isJoinGatewayUnreachable(JoinGateway joinGateway) {
        switch (joinGateway.getJoinMode()) {
            case hard_and:
                return isHardAndJoinGatewayUnreachable(joinGateway);
            case soft_and:
                return isSoftAndJoinGatewayUnreachable(joinGateway);
            case or:
                return isOrJoinGatewayUnreachable(joinGateway);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean isHardAndJoinGatewayUnreachable(JoinGateway joinGateway) {
        return true;
    }

    private boolean isSoftAndJoinGatewayUnreachable(JoinGateway joinGateway) {
        int numOfUnreachable = getUnreachableNumOfJoinGateway(joinGateway);
        int numOfReachable = joinGateway.getPredecessors().size() - numOfUnreachable;

        if (numOfReachable > 0) {
            if (context.getJoinGatewayReachesNum(joinGateway) == numOfReachable) {
                // do the compensate operation
                context.executeAsync(new JoinGatewayMergeOperation(context, joinGateway, false));
            }

            return false;
        }
        return true;
    }

    private boolean isOrJoinGatewayUnreachable(JoinGateway joinGateway) {
        int numOfUnreachable = getUnreachableNumOfJoinGateway(joinGateway);
        int numOfReachable = joinGateway.getPredecessors().size() - numOfUnreachable;

        return numOfReachable == 0;
    }
}
