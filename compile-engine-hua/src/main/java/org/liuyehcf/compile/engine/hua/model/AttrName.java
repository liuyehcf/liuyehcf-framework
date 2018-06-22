package org.liuyehcf.compile.engine.hua.model;

/**
 * 属性名称
 *
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
     * 参数类型列表
     */
    ARGUMENT_TYPE_LIST,

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
     * 是否为空的条件表达式
     */
    IS_EMPTY_CONDITION_EXPRESSION,

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
     * 自增字节码
     */
    IINC_BYTE_CODE,

    /**
     * 语句类型
     */
    STATEMENT_TYPE,

    /**
     * 代码偏移量
     */
    CODE_OFFSET,
}
