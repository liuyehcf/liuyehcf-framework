package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public class DotNameCondition extends BaseCondition {

    private DelegateField output;

    @Override
    public boolean onCondition(ConditionContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println(getClass().getSimpleName());
        }
        return output.getValue();
    }
}
