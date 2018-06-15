package org.liuyehcf.compile.engine.hua.definition;

/**
 * @author chenlu
 * @date 2018/6/12
 */
public abstract class Constant {
    /**
     * type
     */
    public static final String NORMAL_BOOLEAN = "boolean";
    public static final String NORMAL_INT = "int";
    public static final String NORMAL_FLOAT = "float";
    public static final String NORMAL_VOID = "void";
    public static final String COMPLEX_BOOLEAN_EXPRESSION = "complex_boolean_expression";

    /**
     * control
     */
    public static final String NORMAL_IF = "if";
    public static final String NORMAL_ELSE = "else";
    public static final String NORMAL_WHILE = "while";
    public static final String NORMAL_FOR = "for";
    public static final String NORMAL_DO = "do";
    public static final String NORMAL_RETURN = "return";

    /**
     * expression
     */
    public static final String NORMAL_MUL_ASSIGN = "*=";
    public static final String NORMAL_DIV_ASSIGN = "/=";
    public static final String NORMAL_MOD_ASSIGN = "%=";
    public static final String NORMAL_ADD_ASSIGN = "+=";
    public static final String NORMAL_MINUS_ASSIGN = "-=";
    public static final String NORMAL_SHIFT_LEFT_ASSIGN = "<<=";
    public static final String NORMAL_SHIFT_RIGHT_ASSIGN = ">>=";
    public static final String NORMAL_UNSIGNED_SHIFT_RIGHT_ASSIGN = ">>>=";
    public static final String NORMAL_BIT_AND_ASSIGN = "&=";
    public static final String NORMAL_BIT_EXCLUSIVE_OR_ASSIGN = "^=";
    public static final String NORMAL_BIT_OR_ASSIGN = "|=";
    public static final String NORMAL_COLON = ":";
    public static final String NORMAL_QUESTION_MARK = "?";
    public static final String NORMAL_LOGICAL_OR = "||";
    public static final String NORMAL_LOGICAL_AND = "&&";
    public static final String NORMAL_BIT_OR = "|";
    public static final String NORMAL_BIT_EXCLUSIVE_OR = "^";
    public static final String NORMAL_BIT_AND = "&";
    public static final String NORMAL_EQUAL = "==";
    public static final String NORMAL_NOT_EQUAL = "!=";
    public static final String NORMAL_LESS = "<";
    public static final String NORMAL_LARGE = ">";
    public static final String NORMAL_LESS_EQUAL = "<=";
    public static final String NORMAL_LARGE_EQUAL = ">=";
    public static final String NORMAL_SHIFT_LEFT = "<<";
    public static final String NORMAL_SHIFT_RIGHT = ">>";
    public static final String NORMAL_UNSIGNED_SHIFT_RIGHT = ">>>";
    public static final String NORMAL_MUL = "*";
    public static final String NORMAL_DIV = "/";
    public static final String NORMAL_MOD = "%";
    public static final String NORMAL_ADD = "+";
    public static final String NORMAL_MINUS = "-";
    public static final String NORMAL_DOUBLE_PLUS = "++";
    public static final String NORMAL_DOUBLE_MINUS = "--";
    public static final String NORMAL_LOGICAL_NOT = "!";
    public static final String NORMAL_BIT_REVERSED = "~";
    public static final String NORMAL_NEW = "new";

    /**
     * literal
     */
    public static final String NORMAL_NUMBER_0 = "0";
    public static final String NORMAL_BOOLEAN_TRUE = "true";
    public static final String NORMAL_BOOLEAN_FALSE = "false";
}
