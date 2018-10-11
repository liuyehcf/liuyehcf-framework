package org.liuyehcf.compile.engine.expression.core.model;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public enum OperatorType {
    /**
     * 位或
     */
    BIT_OR("|"),

    /**
     * 位与
     */
    BIT_AND("&"),

    /**
     * 位异或
     */
    BIT_XOR("^"),

    /**
     * 循环左移
     */
    SHL("<<"),

    /**
     * 循环右移
     */
    SHR(">>"),

    /**
     * 无符号循环右移
     */
    USHR(">>>"),

    /**
     * 乘
     */
    MUL("*"),

    /**
     * 除
     */
    DIV("/"),

    /**
     * 求模
     */
    REM("%"),

    /**
     * 加
     */
    ADD("+"),

    /**
     * 减
     */
    SUB("-"),

    /**
     * 取反
     */
    NEG("-"),

    /**
     * 比较
     */
    CMP("cmp"),
    ;

    private String symbol;

    OperatorType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
