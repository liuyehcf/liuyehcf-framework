package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class ThrowLinkTerminateCondition implements ConditionDelegate {

    @Override
    public boolean onCondition(ConditionContext context) {
        throw new LinkExecutionTerminateException();
    }
}
