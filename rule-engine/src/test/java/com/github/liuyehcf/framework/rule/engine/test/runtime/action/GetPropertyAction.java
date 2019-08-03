package com.github.liuyehcf.framework.rule.engine.test.runtime.action;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class GetPropertyAction extends BaseAction {

    private DelegateField name;
    private DelegateField expectedValue;

    @Override
    void doAction(ActionContext context) {
        String propertyName = name.getValue();
        Object expectedValue = this.expectedValue.getValue();

        Object propertyValue = context.getProperty(propertyName);

        Assert.assertEquals(expectedValue, propertyValue, "property value not equals");
    }
}
