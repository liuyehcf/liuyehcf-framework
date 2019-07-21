package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/5/10
 */
class ExclusiveGatewayOperation extends AbstractOperation<Void> {

    private final ExclusiveGateway exclusiveGateway;

    ExclusiveGatewayOperation(OperationContext context, ExclusiveGateway exclusiveGateway) {
        super(context);
        this.exclusiveGateway = exclusiveGateway;
    }

    @Override
    protected void execute() throws Throwable {
        context.setNode(exclusiveGateway);

        invokeListeners(getNodeListenerByEvent(exclusiveGateway, ListenerEvent.start));

        context.addTrace(new DefaultTrace(
                context.getNextExecutionId(),
                exclusiveGateway.getId(),
                exclusiveGateway.getType(),
                null,
                null,
                null,
                null,
                null,
                null,
                System.nanoTime(),
                System.nanoTime())
        );

        invokeListeners(getNodeListenerByEvent(exclusiveGateway, ListenerEvent.end));

        // clean redundant link
        if (!exclusiveGateway.getSuccessors().isEmpty()) {
            context.getExecutionInstance().getLinks().remove(context.getExecutionLink());
            context.getExecutionInstance().getUnreachableLinks().remove(context.getExecutionLink());
        }

        context.executeAsync(new ExclusiveGatewayContinueOperation(context, exclusiveGateway, 0));
    }
}
