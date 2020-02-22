package com.github.liuyehcf.framework.flow.engine.promise;

import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public abstract class AbstractPromise<T> extends com.github.liuyehcf.framework.common.tools.promise.AbstractPromise<T> {

    @Override
    protected final RuntimeException createException(Throwable cause) {
        return new FlowException(FlowErrorCode.PROMISE, cause);
    }

    @Override
    protected final RuntimeException createException(String description) {
        return new FlowException(FlowErrorCode.PROMISE, description);
    }

    @Override
    protected final RuntimeException createException(String description, Throwable cause) {
        return new FlowException(FlowErrorCode.PROMISE, description, cause);
    }
}
