package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class GetPropertyListener implements ListenerDelegate {

    private DelegateField name;
    private DelegateField expectedValue;

    @Override
    public void onListener(ListenerContext context) {
        String propertyName = name.getValue();
        Object expectedValue = this.expectedValue.getValue();

        Object propertyValue = context.getProperty(propertyName);

        Assert.assertEquals(expectedValue, propertyValue, "property value not equals");
    }
}
