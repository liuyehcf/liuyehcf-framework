package com.github.liuyehcf.framework.flow.engine.promise;

import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class DefaultPromise<T> extends com.github.liuyehcf.framework.common.tools.promise.DefaultPromise<T> {

    @Override
    protected RuntimeException reportCancel() {
        return new FlowException(FlowErrorCode.PROMISE, "promise cancel");
    }

    @Override
    protected RuntimeException reportTimeout() {
        return new FlowException(FlowErrorCode.PROMISE, "promise timeout");
    }

    @Override
    protected RuntimeException reportFailure(Throwable cause) {
        if (cause instanceof FlowException) {
            return (FlowException) cause;
        }
        return new FlowException(FlowErrorCode.PROMISE, "promise failed", cause);
    }

    @Override
    protected RuntimeException reportUnknownError(Throwable cause) {
        if (cause instanceof FlowException) {
            return (FlowException) cause;
        }
        return new FlowException(FlowErrorCode.PROMISE, "unknown", cause);
    }
}
