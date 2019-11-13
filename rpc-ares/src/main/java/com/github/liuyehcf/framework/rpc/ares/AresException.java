package com.github.liuyehcf.framework.rpc.ares;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
public class AresException extends RuntimeException {

    public AresException() {
        super();
    }

    public AresException(String message) {
        super(message);
    }

    public AresException(String message, Throwable cause) {
        super(message, cause);
    }

    public AresException(Throwable cause) {
        super(cause);
    }
}
