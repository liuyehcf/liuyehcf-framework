package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/13
 */
public class ThrowExceptionCondition implements ConditionDelegate {
    @Override
    public boolean onCondition(ConditionContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwExceptionCondition.");
        }
        throw new RuntimeException("throw exception condition");
    }
}