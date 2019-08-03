package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public class SuccessResultListener extends BaseListener {

    private DelegateField result;

    @Override
    void doBefore(ListenerContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    void doSuccess(ListenerContext context, Object result) {
        Assert.assertEquals(this.result.getValue(), result);
    }

    @Override
    void doFailure(ListenerContext context, Throwable cause) {
        throw new UnsupportedOperationException();
    }
}
