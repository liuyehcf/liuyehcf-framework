package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
public class SubRuleOperation extends AbstractOperation<Void> {

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
    protected void execute() throws Throwable {
        context.setNode(subRule);

        context.addTrace(new DefaultTrace(
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

        invokeListeners(getNodeListenerByEvent(subRule, ListenerEvent.end));

        context.markElementFinished(subRule);

        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), subRule, reverseLinkType));
        forward(linkType, subRule.getSuccessors());
    }
}
