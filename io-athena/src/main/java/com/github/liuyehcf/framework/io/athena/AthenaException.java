package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class AthenaException extends RuntimeException {

    public AthenaException() {
    }

    public AthenaException(String message) {
        super(message);
    }

    public AthenaException(String message, Throwable cause) {
        super(message, cause);
    }

    public AthenaException(Throwable cause) {
        super(cause);
    }

    public AthenaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
