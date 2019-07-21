package com.github.liuyehcf.framework.rule.engine.test.runtime.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class SetPropertyAction implements ActionDelegate {

    private DelegateField name;
    private DelegateField value;

    @Override
    public void onAction(ActionContext context) {
        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }
}
