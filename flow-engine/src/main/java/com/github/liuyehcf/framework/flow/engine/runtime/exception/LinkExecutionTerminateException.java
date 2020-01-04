package com.github.liuyehcf.framework.flow.engine.runtime.exception;

import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class LinkExecutionTerminateException extends FlowException {

    private static final FlowErrorCode CODE = FlowErrorCode.LINK_TERMINATE;

    public LinkExecutionTerminateException() {
        super(CODE);
    }

    public LinkExecutionTerminateException(String message) {
        super(CODE, message);
    }

    public LinkExecutionTerminateException(Throwable cause) {
        super(CODE, cause);
    }

    public LinkExecutionTerminateException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
