package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.PAUSE_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class SetPropertyListener extends BaseListener {

    private DelegateField name;
    private DelegateField value;

    @Override
    public void onBefore(ListenerContext context) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }
}
