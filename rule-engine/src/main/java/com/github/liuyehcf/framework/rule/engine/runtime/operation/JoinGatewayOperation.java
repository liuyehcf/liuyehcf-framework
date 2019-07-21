package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultTrace;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class JoinGatewayOperation extends AbstractOperation<Void> {

    private final JoinGateway joinGateway;

    JoinGatewayOperation(OperationContext context, JoinGateway joinGateway) {
        super(context);
        Assert.assertNotNull(joinGateway);
        this.joinGateway = joinGateway;
    }

    @Override
    protected void execute() throws Throwable {
        context.setNode(joinGateway);

        long startNanos = System.nanoTime();

        invokeListeners(getNodeListenerByEvent(joinGateway, ListenerEvent.start));

        context.addTrace(new DefaultTrace(
                context.getNextExecutionId(),
                joinGateway.getId(),
                joinGateway.getType(),
                null,
                null,
                null,
                null,
                null,
                null,
                startNanos,
                System.nanoTime()));
        context.markElementFinished(joinGateway);

        invokeListeners(getNodeListenerByEvent(joinGateway, ListenerEvent.end));

        forward(LinkType.NORMAL, joinGateway.getSuccessors());
    }
}
