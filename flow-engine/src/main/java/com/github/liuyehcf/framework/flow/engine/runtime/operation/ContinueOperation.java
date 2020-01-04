package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
class ContinueOperation extends AbstractOperation<Void> {

    private final Node node;

    ContinueOperation(OperationContext context, Node node) {
        super(context);
        this.node = node;
    }

    @Override
    void operate() {
        context.setNode(node);

        if (node instanceof Action) {
            context.executeAsync(new ActionOperation(context, (Action) node));
        } else if (node instanceof Condition) {
            context.executeAsync(new ConditionOperation(context, (Condition) node));
        } else if (node instanceof JoinGateway) {
            context.executeAsync(new JoinGatewayMergeOperation(context, (JoinGateway) node, true));
        } else if (node instanceof ExclusiveGateway) {
            context.executeAsync(new ExclusiveGatewayOperation(context, (ExclusiveGateway) node));
        } else if (node instanceof Flow) {
            context.executeAsync(new SubFlowTriggerOperation(context, (Flow) node));
        }
    }
}
