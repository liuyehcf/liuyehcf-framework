package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class ThrowLinkTerminateCondition extends BaseCondition {

    @Override
    boolean doCondition(ConditionContext context) {
        throw new LinkExecutionTerminateException();
    }
}
