package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class SetTraceAttrCondition implements ConditionDelegate {

    private DelegateField output;
    private DelegateField name;
    private DelegateField value;

    @Override
    public boolean onCondition(ConditionContext context) {
        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);

        return output.getValue();
    }
}
