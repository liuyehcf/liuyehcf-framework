package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class StartOperation extends AbstractOperation<Void> {

    public StartOperation(OperationContext context) {
        super(context);
    }

    @Override
    protected void execute() {
        Start start = context.getRule().getStart();

        context.setNode(start);

        context.addTraceToExecutionLink(new DefaultTrace(
                context.getNextExecutionId(),
                start.getId(),
                start.getType(),
                null,
                null,
                null,
                null,
                null,
                null,
                System.nanoTime(),
                System.nanoTime()));
        context.markElementFinished(start);

        forward(LinkType.NORMAL, start.getSuccessors());
    }
}
