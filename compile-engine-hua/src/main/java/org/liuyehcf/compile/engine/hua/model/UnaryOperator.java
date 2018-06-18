package org.liuyehcf.compile.engine.hua.model;

/**
 * @author chenlu
 * @date 2018/6/18
 */
public enum UnaryOperator {
    /**
     * 递增
     */
    INCREMENT("++"),

    /**
     * 递减
     */
    DECREMENT("--"),;

    private final String sign;

    UnaryOperator(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
