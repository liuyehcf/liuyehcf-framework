package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class GetPropertyCondition extends BaseCondition {

    private DelegateField name;
    private DelegateField expectedValue;
    private DelegateField output;

    @Override
    public boolean onCondition(ConditionContext context) {
        String propertyName = name.getValue();
        Object expectedValue = this.expectedValue.getValue();
        boolean conditionOutput = output.getValue();

        Object propertyValue = context.getProperty(propertyName);

        Assert.assertEquals(expectedValue, propertyValue, "property value not equals");

        return conditionOutput;
    }
}
