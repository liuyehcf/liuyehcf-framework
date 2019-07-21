package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public interface ConditionContext extends ExecutableContext<Condition> {

    /**
     * get condition of this execution
     *
     * @return current condition
     */
    @Override
    Condition getElement();
}
