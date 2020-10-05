package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.PAUSE_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public class SuccessResultListener extends BaseListener {

    private DelegateField result;

    @Override
    public void onBefore(ListenerContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        Assert.assertEquals(this.result.getValue(), result);
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        throw new UnsupportedOperationException();
    }
}
