package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
class SubRuleOperation extends AbstractOperation<Void> {

    private final Rule subRule;
    private final long startNanos;
    private final LinkType linkType;
    private final LinkType reverseLinkType;

    SubRuleOperation(OperationContext context, Rule subRule, long startNanos, LinkType linkType) {
        super(context);
        this.subRule = subRule;
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
        context.setNode(subRule);

        context.addTraceToExecutionLink(new DefaultTrace(
                context.getNextExecutionId(),
                subRule.getId(),
                subRule.getType(),
                subRule.getName(),
                null,
                null,
                null,
                null,
                null,
                startNanos,
                System.nanoTime()));

        invokeNodeSuccessListeners(subRule, LinkType.TRUE.equals(linkType), this::continueForward);
    }

    private void continueForward() {
        context.markElementFinished(subRule);

        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), subRule, reverseLinkType));
        forward(linkType, subRule.getSuccessors());
    }
}
