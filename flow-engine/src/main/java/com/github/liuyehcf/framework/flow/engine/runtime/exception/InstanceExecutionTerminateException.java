package com.github.liuyehcf.framework.flow.engine.runtime.exception;

import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class InstanceExecutionTerminateException extends FlowException {

    private static final FlowErrorCode CODE = FlowErrorCode.INSTANCE_TERMINATE;

    public InstanceExecutionTerminateException() {
        super(CODE);
    }

    public InstanceExecutionTerminateException(String message) {
        super(CODE, message);
    }

    public InstanceExecutionTerminateException(Throwable cause) {
        super(CODE, cause);
    }

    public InstanceExecutionTerminateException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
