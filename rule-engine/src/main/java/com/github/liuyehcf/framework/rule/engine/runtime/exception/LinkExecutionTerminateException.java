package com.github.liuyehcf.framework.rule.engine.runtime.exception;

import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class LinkExecutionTerminateException extends RuleException {

    private static final RuleErrorCode CODE = RuleErrorCode.LINK_TERMINATE;

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
