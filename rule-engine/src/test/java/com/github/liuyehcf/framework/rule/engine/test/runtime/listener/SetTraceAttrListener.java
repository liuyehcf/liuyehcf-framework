package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class SetTraceAttrListener implements ListenerDelegate {

    private DelegateField name;
    private DelegateField value;

    @Override
    public void onListener(ListenerContext context) {
        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);
    }
}
