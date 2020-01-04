package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
class StartOperation extends AbstractOperation<Void> {

    StartOperation(OperationContext context) {
        super(context);
    }

    @Override
    void operate() {
        Start start = context.getFlow().getStart();

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
