package org.liuyehcf.compile.engine.expression.compile.definition.model;

/**
 * 属性名称
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public enum AttrName {
    /**
     * 标志符名称
     */
    IDENTIFIER_NAME,

    /**
     * 方法名称
     */
    METHOD_NAME,

    /**
     * 字面值
     */
    LITERAL_VALUE,

    /**
     * 控制转移类型
     */
    CONTROL_TRANSFER_TYPE,

    /**
     * 是否为布尔表达式
     * 单个的布尔变量或者布尔字面值不是布尔表达式
     */
    IS_COMPLEX_BOOLEAN_EXPRESSION,

    /**
     * 布尔表达式为true时的控制转移字节码
     */
    TRUE_BYTE_CODE,

    /**
     * 布尔表达式为false时的控制转移字节码
     */
    FALSE_BYTE_CODE,

    /**
     * goto控制转移字节码
     */
    NEXT_BYTE_CODE,

    /**
     * 参数长度
     */
    ARGUMENT_SIZE,

    /**
     * 初始化列表长度
     */
    ARRAY_SIZE,
    ;
}
