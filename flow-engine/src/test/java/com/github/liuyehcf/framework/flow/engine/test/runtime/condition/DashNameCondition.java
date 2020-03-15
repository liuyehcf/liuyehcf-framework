package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2020/3/15
 */
public class DashNameCondition extends BaseCondition {

    private DelegateField output;

    @Override
    public boolean onCondition(ConditionContext context) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println(getClass().getSimpleName());
        }
        return output.getValue();
    }
}
