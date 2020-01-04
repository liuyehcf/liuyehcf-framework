package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.util.ElementUtils;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
class JoinGatewayMergeOperation extends AbstractOperation<Void> {

    private final JoinGateway joinGateway;
    private final boolean increaseReaches;

    JoinGatewayMergeOperation(OperationContext context, JoinGateway joinGateway, boolean increaseReaches) {
        super(context);
        Assert.assertNotNull(joinGateway, "joinGateway");
        this.joinGateway = joinGateway;
        this.increaseReaches = increaseReaches;
    }

    @Override
    void operate() {
        context.setNode(joinGateway);

        if (increaseReaches) {
            context.linkReachesJoinGateway(joinGateway);
        }

        ReentrantLock lock = context.getElementLock(joinGateway);

        boolean selected = false;
        ExecutionLink mergedExecutionLink = null;

        // if many node reaches this joinGateway concurrently
        // element lock make sure only one of them can execute this logic
        if (isReached() && lock.tryLock()) {

            // guarantee the following statements only be execute once
            // this condition cannot move to the upper condition(tryLock)
            if (context.isJoinGatewayAggregated(joinGateway)) {
                if (ElementUtils.isOrJoinMode(joinGateway)) {
                    // JoinGatewayMergeOperation may trigger by MarkSuccessorUnreachableOperation
                    // but only for softAnd mode, so context.getExecutionLink() is not null in or mode
                    ExecutionLink currentLink = context.getExecutionLink();
                    Assert.assertNotNull(currentLink, "currentLink");
                    context.getExecutionInstance().removeLink(currentLink);
                    context.getExecutionInstance().addUnreachableLink(currentLink);
                }
                lock.unlock();
                return;
            }

            try {
                if (!ElementUtils.isOrJoinMode(joinGateway)) {
                    List<ExecutionLink> reachedExecutionLinks = getReachedExecutionLinks();
                    mergedExecutionLink = mergeLinks(reachedExecutionLinks);
                    reachedExecutionLinks.forEach(context.getExecutionInstance()::removeLink);
                } else {
                    // JoinGatewayMergeOperation may trigger by MarkSuccessorUnreachableOperation
                    // but only for softAnd mode, so context.getExecutionLink() is not null in or mode
                    mergedExecutionLink = context.getExecutionLink();
                    Assert.assertNotNull(mergedExecutionLink, "mergedExecutionLink");
                    context.getExecutionInstance().removeLink(mergedExecutionLink);
                }

                selected = true;
            } finally {
                context.markJoinGatewayAggregated(joinGateway);
                lock.unlock();
            }
        }

        // execute beyond lock
        if (selected) {
            context.executeAsync(new JoinGatewayOperation(context.cloneLinkedContext(mergedExecutionLink), joinGateway));
        }
    }

    private boolean isReached() {
        switch (joinGateway.getJoinMode()) {
            case hard_and:
                return isHardModeReached();
            case soft_and:
                return isSoftModeReached();
            case or:
                return isOrModeReached();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean isHardModeReached() {
        // if the following statement is false
        // means that at least one node is not reaches this gateway
        // and is bound to continue
        return context.getJoinGatewayReachesNum(joinGateway) == joinGateway.getPredecessors().size();
    }

    private boolean isSoftModeReached() {
        // if soft_and mode join gateway has 3 predecessors
        // two of them reaches this gateway, and the last one unReaches this gateway
        // in this cases, the MarkSuccessorUnreachableOperation must trigger this gateway to continue
        //
        // if soft_and mode join gateway has 2 predecessors
        // two of them move in to gateway concurrently
        // in this cases, there must be one thread execution the trigger gateway continue operation
        int numOfUnreachable = getUnreachableNumOfJoinGateway(joinGateway);

        int numOfMayReachable = joinGateway.getPredecessors().size() - numOfUnreachable;

        // if the following statement is false
        // means that at least one node is not reaches this gateway or not unReaches this gateway
        // either cases, gateway is bound to continue
        return context.getJoinGatewayReachesNum(joinGateway) == numOfMayReachable;
    }

    private boolean isOrModeReached() {
        // allow access to any link arrival
        return true;
    }

    private List<ExecutionLink> getReachedExecutionLinks() {
        List<ExecutionLink> linkIds = Lists.newArrayList();

        ExecutionInstance executionInstance = context.getExecutionInstance();

        for (ExecutionLink link : executionInstance.getLinks()) {
            if (context.isLinkReachedJoinGateway(joinGateway, link)) {
                linkIds.add(link);
            }
        }

        return linkIds;
    }
}
