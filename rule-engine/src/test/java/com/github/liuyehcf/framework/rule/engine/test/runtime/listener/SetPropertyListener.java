package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class SetPropertyListener implements ListenerDelegate {

    private DelegateField name;
    private DelegateField value;

    @Override
    public void onListener(ListenerContext context) {
        String propertyName = name.getValue();
        Object propertyValue = value.getValue();

        context.setProperty(propertyName, propertyValue);
    }
}
