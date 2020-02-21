package com.github.liuyehcf.framework.common.tools.promise;

/**
 * @author hechenfeng
 * @date 2020/2/11
 */
public class PromiseException extends RuntimeException {

    public PromiseException() {
    }

    public PromiseException(String message) {
        super(message);
    }

    public PromiseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PromiseException(Throwable cause) {
        super(cause);
    }

    public PromiseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
