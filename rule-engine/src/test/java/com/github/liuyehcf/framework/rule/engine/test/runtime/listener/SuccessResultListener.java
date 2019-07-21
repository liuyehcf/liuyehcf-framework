package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public class SuccessResultListener implements ListenerDelegate {

    private DelegateField result;

    @Override
    public void onBefore(ListenerContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        Assert.assertEquals(this.result.getValue(), result);
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        throw new UnsupportedOperationException();
    }
}
