package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class SetTraceAttrCondition extends BaseCondition {

    private DelegateField output;
    private DelegateField name;
    private DelegateField value;

    @Override
    boolean doCondition(ConditionContext context) {
        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);

        return output.getValue();
    }
}
