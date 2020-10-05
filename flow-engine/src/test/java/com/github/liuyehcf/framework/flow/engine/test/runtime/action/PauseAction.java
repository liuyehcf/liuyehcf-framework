package com.github.liuyehcf.framework.flow.engine.test.runtime.action;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.time.TimeUtils;
import com.github.liuyehcf.framework.flow.engine.promise.ExecutionLinkPausePromise;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/10/2
 */
public class PauseAction extends BaseAction {

    private DelegateField pause;
    private DelegateField isCancel;
    private DelegateField isSuccess;
    private DelegateField cause;

    public void setPause(DelegateField pause) {
        this.pause = pause;
    }

    public void setIsCancel(DelegateField isCancel) {
        this.isCancel = isCancel;
    }

    public void setIsSuccess(DelegateField isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setCause(DelegateField cause) {
        this.cause = cause;
    }

    @Override
    public void onAction(ActionContext context) {
        ExecutionLinkPausePromise promise = context.pauseExecutionLink();
        Assert.assertEquals(promise, context.pauseExecutionLink());
        getAsyncExecutor().execute(() -> {
            TimeUtils.sleep(pause.getValue(), TimeUnit.MILLISECONDS);
            if (isCancel.getValueOrDefault(false)) {
                promise.tryCancel();
            } else if (isSuccess.getValueOrDefault(false)) {
                promise.trySuccess(null);
            } else {
                promise.tryFailure(cause.getValue());
            }
        });
    }
}
