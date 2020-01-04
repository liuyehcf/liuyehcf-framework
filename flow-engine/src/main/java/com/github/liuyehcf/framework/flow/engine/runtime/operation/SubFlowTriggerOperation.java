package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.util.CloneUtils;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
class SubFlowTriggerOperation extends AbstractOperation<Void> {

    private final Flow subFlow;

    SubFlowTriggerOperation(OperationContext context, Flow subFlow) {
        super(context);
        this.subFlow = subFlow;
    }

    @Override
    void operate() throws Throwable {
        context.setNode(subFlow);

        invokeNodeBeforeListeners(subFlow, this::continueSubFlow);
    }

    private void continueSubFlow() {
        context.getEngine().startFlow(
                new ExecutionCondition(subFlow)
                        .instanceId(context.getExecutionInstance().getId())
                        .env(CloneUtils.cloneEnv(context.getEngine(), context.getLinkEnv()))
                        .executionIdGenerator(context.getExecutionIdGenerator())
        ).addListener(new SubFlowMergeOperation(context, subFlow, System.nanoTime()));
    }
}
