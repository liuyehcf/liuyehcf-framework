package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
public class SubRuleTriggerOperation extends AbstractOperation<Void> {

    private final Rule subRule;

    SubRuleTriggerOperation(OperationContext context, Rule subRule) {
        super(context);
        this.subRule = subRule;
    }

    @Override
    protected void execute() throws Throwable {
        context.setNode(subRule);

        invokeListeners(getNodeListenerByEvent(subRule, ListenerEvent.start));

        RuleEngine.startRule(subRule, context.getExecutionInstance().getId(), CloneUtils.hessianClone(context.getEnv()), context.getExecutionIdGenerator())
                .addListener(new SubRuleMergeOperation(context, subRule, System.nanoTime()));
    }
}
