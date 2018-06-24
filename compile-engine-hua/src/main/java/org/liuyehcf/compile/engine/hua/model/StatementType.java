package org.liuyehcf.compile.engine.hua.model;

/**
 * 语句类型
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public enum StatementType {
    /**
     * 赋值
     */
    ASSIGNMENT,

    /**
     * 前置递增
     */
    PRE_INCREMENT,

    /**
     * 前置递减
     */
    PRE_DECREMENT,

    /**
     * 后置递增
     */
    POST_INCREMENT,

    /**
     * 后置递减
     */
    POST_DECREMENT,

    /**
     * 方法调用
     */
    METHOD_INVOCATION
}
