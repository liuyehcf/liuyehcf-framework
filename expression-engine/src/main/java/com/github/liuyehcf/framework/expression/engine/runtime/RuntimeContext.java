package com.github.liuyehcf.framework.expression.engine.runtime;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public interface RuntimeContext {

    /**
     * 操作数入栈
     *
     * @param operator 操作数
     */
    void push(ExpressionValue operator);

    /**
     * 操作数出栈
     *
     * @return 操作数
     */
    ExpressionValue pop();

    /**
     * 递增字节码偏移量
     */
    void increaseCodeOffset();

    /**
     * 设置字节码偏移量
     *
     * @param offset 字节码偏移量
     */
    void setCodeOffset(int offset);

    /**
     * 依据属性名取出属性值
     *
     * @param propertyName 属性名
     * @return 属性值
     */
    Object getProperty(String propertyName);
}
