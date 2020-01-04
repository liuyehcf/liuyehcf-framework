package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.exception.LinkExecutionTerminateException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class ThrowLinkTerminateCondition extends BaseCondition {

    @Override
    public boolean onCondition(ConditionContext context) {
        throw new LinkExecutionTerminateException();
    }
}
