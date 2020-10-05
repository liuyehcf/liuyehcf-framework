package com.github.liuyehcf.framework.flow.engine.test.runtime.action;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.PAUSE_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class SetPropertyAction extends BaseAction {

    private DelegateField name;
    private DelegateField value;

    @Override
    public void onAction(ActionContext context) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }
}
