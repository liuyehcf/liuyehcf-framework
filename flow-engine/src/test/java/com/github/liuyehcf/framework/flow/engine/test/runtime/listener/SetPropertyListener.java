package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class SetPropertyListener extends BaseListener {

    private DelegateField name;
    private DelegateField value;

    @Override
    public void onBefore(ListenerContext context) {
        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }
}