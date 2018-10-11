package org.liuyehcf.compile.engine.expression.core;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class ExpressionException extends RuntimeException {
    public ExpressionException() {

    }

    public ExpressionException(String message) {
        super(message);
    }

    public ExpressionException(Throwable e) {
        super(e);
    }

    public ExpressionException(String message, Throwable e) {
        super(message, e);
    }
}
