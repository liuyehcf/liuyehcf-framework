package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class GetPropertyListener extends BaseListener {

    private DelegateField name;
    private DelegateField expectedValue;

    @Override
    public void onBefore(ListenerContext context) {
        String propertyName = name.getValue();
        Object expectedValue = this.expectedValue.getValue();

        Object propertyValue = context.getProperty(propertyName);

        Assert.assertEquals(expectedValue, propertyValue, "property value not equals");
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        String propertyName = name.getValue();
        Object expectedValue = this.expectedValue.getValue();

        Object propertyValue = context.getProperty(propertyName);

        Assert.assertEquals(expectedValue, propertyValue, "property value not equals");
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        String propertyName = name.getValue();
        Object expectedValue = this.expectedValue.getValue();

        Object propertyValue = context.getProperty(propertyName);

        Assert.assertEquals(expectedValue, propertyValue, "property value not equals");
    }
}
