package org.liuyehcf.compile.engine.hua.definition;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public enum AttrName {
    /**
     * 标志符名称
     */
    IDENTIFIER_NAME,

    /**
     * 类型
     */
    TYPE,

    /**
     * 方法名称
     */
    METHOD_NAME,

    /**
     * 方法参数列表
     */
    PARAMETER_LIST,

    /**
     * 参数长度
     */
    ARGUMENT_SIZE,

    /**
     * 赋值符号
     */
    ASSIGN_OPERATOR,

    /**
     * 字面值
     */
    LITERAL_VALUE,

    /**
     * 布尔表达式类型
     */
    BOOLEAN_EXPRESSION_TYPE,

    /**
     * 是否为复合的布尔表达式
     */
    IS_COMPLEX_BOOLEAN_EXPRESSION,

    /**
     * 布尔表达式为true时的字节码
     */
    TRUE_BYTE_CODE,

    /**
     * 布尔表达式为false时的字节码
     */
    FALSE_BYTE_CODE,

    /**
     * 布尔表达式后续的字节码
     */
    NEXT_BYTE_CODE,

    /**
     * 循环偏移量（code）
     */
    LOOP_CODE_OFFSET,
}
