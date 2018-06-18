package org.liuyehcf.compile.engine.hua.model;

/**
 * @author hechenfeng
 * @date 2018/6/18
 */
public enum BinaryOperator {
    /**
     * 位或
     */
    BIT_OR("|"),

    /**
     * 位异或
     */
    BIT_XOR("^"),

    /**
     * 位与
     */
    BIT_AND("&"),

    /**
     * 左循环移位
     */
    SHIFT_LEFT("<<"),

    /**
     * 右循环移位，带符号
     */
    SHIFT_RIGHT(">>"),

    /**
     * 右循环移位，无符号
     */
    UNSIGNED_SHIFT_RIGHT(">>>"),

    /**
     * 加法
     */
    ADDITION("+"),

    /**
     * 减法
     */
    SUBTRACTION("-"),

    /**
     * 乘法
     */
    MULTIPLICATION("*"),

    /**
     * 除法
     */
    DIVISION("/"),

    /**
     * 求余
     */
    REMAINDER("%");

    private final String sign;

    BinaryOperator(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
