package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.PAUSE_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class SetTraceAttrCondition extends BaseCondition {

    private DelegateField output;
    private DelegateField name;
    private DelegateField value;

    @Override
    public boolean onCondition(ConditionContext context) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);

        return output.getValue();
    }
}
