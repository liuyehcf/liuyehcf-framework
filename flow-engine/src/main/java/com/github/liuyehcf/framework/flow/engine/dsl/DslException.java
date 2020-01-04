package com.github.liuyehcf.framework.flow.engine.dsl;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class DslException extends RuntimeException {

    public DslException() {
    }

    public DslException(String message) {
        super(message);
    }

    public DslException(String message, Throwable cause) {
        super(message, cause);
    }
}
