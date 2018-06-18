package org.liuyehcf.compile.engine.hua.model;

/**
 * 一元操作符
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public enum UnaryOperator {
    ;

    private final String sign;

    UnaryOperator(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
