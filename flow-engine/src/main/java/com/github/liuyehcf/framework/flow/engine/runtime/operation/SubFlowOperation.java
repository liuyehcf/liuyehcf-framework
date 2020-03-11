package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
class SubFlowOperation extends AbstractOperation<Void> {

    private final Flow subFlow;
    private final long startTimestamp;
    private final long startNanos;
    private final LinkType linkType;
    private final LinkType reverseLinkType;

    SubFlowOperation(OperationContext context, Flow subFlow, long startTimestamp, long startNanos, LinkType linkType) {
        super(context);
        this.subFlow = subFlow;
        this.startTimestamp = startTimestamp;
        this.startNanos = startNanos;
        if (LinkType.TRUE.equals(linkType)) {
            this.linkType = LinkType.TRUE;
            reverseLinkType = LinkType.FALSE;
        } else {
            Assert.assertEquals(LinkType.FALSE, linkType);
            this.linkType = LinkType.FALSE;
            reverseLinkType = LinkType.TRUE;
        }
    }

    @Override
    void operate() throws Throwable {
        context.setNode(subFlow);

        context.addTraceToExecutionLink(new DefaultTrace(
                context.getNextExecutionId(),
                subFlow.getId(),
                subFlow.getType(),
                subFlow.getName(),
                null,
                null,
                null,
                null,
                null,
                startTimestamp,
                System.currentTimeMillis(),
                System.nanoTime() - startNanos));

        invokeNodeSuccessListeners(subFlow, LinkType.TRUE.equals(linkType), this::continueForward);
    }

    private void continueForward() {
        context.markElementFinished(subFlow);

        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), subFlow, reverseLinkType));
        forward(linkType, subFlow.getSuccessors());
    }
}
