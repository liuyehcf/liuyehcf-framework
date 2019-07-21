package com.github.liuyehcf.framework.rule.engine.runtime.exception;

import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class InstanceExecutionTerminateException extends RuleException {

    private static final RuleErrorCode CODE = RuleErrorCode.INSTANCE_TERMINATE;

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
