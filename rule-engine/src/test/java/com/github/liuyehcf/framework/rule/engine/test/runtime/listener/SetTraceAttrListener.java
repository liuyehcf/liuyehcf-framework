package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class SetTraceAttrListener extends BaseListener {

    private DelegateField name;
    private DelegateField value;

    @Override
    void doBefore(ListenerContext context) {
        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);
    }

    @Override
    void doSuccess(ListenerContext context, Object result) {
        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);
    }

    @Override
    void doFailure(ListenerContext context, Throwable cause) {
        String attrName = name.getValue();
        Object attrValue = value.getValue();

        context.addLocalAttribute(attrName, attrValue);
    }
}
