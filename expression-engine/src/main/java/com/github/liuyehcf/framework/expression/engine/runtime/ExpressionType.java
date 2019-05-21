package com.github.liuyehcf.framework.expression.engine.runtime;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public enum ExpressionType {
    /**
     * null
     */
    NULL,

    /**
     * 布尔值
     */
    BOOLEAN,

    /**
     * 比较值
     */
    COMPARABLE_VALUE,

    /**
     * 长整型
     */
    LONG,

    /**
     * 双精度浮点数
     */
    DOUBLE,

    /**
     * 字符串
     */
    STRING,

    /**
     * 数组
     */
    ARRAY,

    /**
     * 集合
     */
    LIST,

    /**
     * 其他Java Object
     */
    OBJECT,
    ;
}
