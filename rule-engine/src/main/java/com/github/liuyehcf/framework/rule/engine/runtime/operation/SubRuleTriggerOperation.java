package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.ExecutionCondition;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
class SubRuleTriggerOperation extends AbstractOperation<Void> {

    private final Rule subRule;

    SubRuleTriggerOperation(OperationContext context, Rule subRule) {
        super(context);
        this.subRule = subRule;
    }

    @Override
    void operate() throws Throwable {
        context.setNode(subRule);

        invokeNodeBeforeListeners(subRule, this::continueSubRule);
    }

    private void continueSubRule() {
        context.getEngine().startRule(
                new ExecutionCondition(subRule)
                        .instanceId(context.getExecutionInstance().getId())
                        .env(CloneUtils.cloneEnv(context.getEngine(), context.getLinkEnv()))
                        .executionIdGenerator(context.getExecutionIdGenerator())
        ).addListener(new SubRuleMergeOperation(context, subRule, System.nanoTime()));
    }
}
