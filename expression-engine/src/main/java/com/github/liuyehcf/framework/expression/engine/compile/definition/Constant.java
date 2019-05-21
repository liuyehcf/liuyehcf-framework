package com.github.liuyehcf.framework.expression.engine.compile.definition;

/**
 * 文法定义相关的常量
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public abstract class Constant {

    /**
     * expression
     */
    public static final String NORMAL_COLON = ":";
    public static final String NORMAL_QUESTION_MARK = "?";
    public static final String NORMAL_LOGICAL_OR = "||";
    public static final String NORMAL_LOGICAL_AND = "&&";
    public static final String NORMAL_BIT_OR = "|";
    public static final String NORMAL_BIT_XOR = "^";
    public static final String NORMAL_BIT_AND = "&";
    public static final String NORMAL_EQ = "==";
    public static final String NORMAL_NE = "!=";
    public static final String NORMAL_LT = "<";
    public static final String NORMAL_GT = ">";
    public static final String NORMAL_LE = "<=";
    public static final String NORMAL_GE = ">=";
    public static final String NORMAL_SHL = "<<";
    public static final String NORMAL_SHR = ">>";
    public static final String NORMAL_USHR = ">>>";
    public static final String NORMAL_MUL = "*";
    public static final String NORMAL_DIV = "/";
    public static final String NORMAL_REM = "%";
    public static final String NORMAL_ADD = "+";
    public static final String NORMAL_SUB = "-";
    public static final String NORMAL_LOGICAL_NOT = "!";
    public static final String NORMAL_BIT_REVERSED = "~";

    /**
     * literal
     */
    public static final String NORMAL_BOOLEAN_TRUE = "true";
    public static final String NORMAL_BOOLEAN_FALSE = "false";
    public static final String NORMAL_NULL = "null";
}
