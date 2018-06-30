package org.liuyehcf.compile.engine.hua.compile.definition.model;

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
     * 控制转移类型
     */
    CONTROL_TRANSFER_TYPE,

    /**
     * 是否为布尔表达式
     * 单个的布尔变量或者布尔字面值不是布尔表达式
     */
    IS_COMPLEX_BOOLEAN_EXPRESSION,

    /**
     * 是否为空的条件表达式
     */
    IS_EMPTY_EXPRESSION,

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

    /**
     * 维度表达式大小
     */
    EXPRESSION_DIM_SIZE,

    /**
     * 空维度大小
     */
    EMPTY_DIM_SIZE
}
