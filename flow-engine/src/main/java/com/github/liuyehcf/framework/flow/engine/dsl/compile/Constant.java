package com.github.liuyehcf.framework.flow.engine.dsl.compile;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
class Constant {

    /**
     * non-terminate symbol
     */
    static final String BLOCK = "<block>";
    static final String STATEMENTS = "<statements>";
    static final String STATEMENT = "<statement>";
    static final String IF_THEN_STATEMENT = "<if then statement>";
    static final String IF_THEN_STATEMENTS = "<if then statements>";
    static final String IF_THEN_ELSE_STATEMENT = "<if then else statement>";
    static final String SELECT_STATEMENT = "<select statement>";
    static final String JOIN_STATEMENT = "<join statement>";
    static final String JOIN_THEN_STATEMENT = "<join then statement>";
    static final String SUB_STATEMENT = "<sub statement>";
    static final String SUB_THEN_STATEMENT = "<sub then statement>";
    static final String SUB_THEN_ELSE_STATEMENT = "<sub then else statement>";
    static final String ACTION_EXPRESSION = "<action expression>";
    static final String CONDITION_EXPRESSION = "<condition expression>";
    static final String EPSILON_OR_LISTENERS = "<epsilon or listeners>";
    static final String LISTENERS = "<listeners>";
    static final String EPSILON_OR_CHOOSE_OR_BLOCK = "<epsilon or choose or block>";
    static final String EPSILON_OR_CHOOSE = "<epsilon or choose>";

    static final String MARK_CREATE_FLOW = "<mark create flow>";
    static final String MARK_LINK_TYPE_TRUE = "<mark link type true>";
    static final String MARK_LINK_TYPE_FALSE = "<mark link type false>";
    static final String MARK_ACTION_ADD_LISTENER = "<mark action add listener>";
    static final String MARK_JOIN_ADD_LISTENER = "<mark join add listener>";
    static final String MARK_ENTER_JOIN_SCOPE = "<mark enter join scope>";
    static final String MARK_EXIT_JOIN_SCOPE = "<mark exit join scope>";
    static final String MARK_ENTER_SELECT = "<mark enter select>";
    static final String MARK_EXIT_SELECT = "<mark exit select>";
    static final String MARK_ENTER_SUB_OR_SUB_THEN = "<mark enter sub or sub then>";
    static final String MARK_EXIT_SUB = "<mark exit sub>";
    static final String MARK_EXIT_SUB_THEN = "<mark exit sub then>";
    static final String MARK_ADD_SUB_FLOW = "<mark add sub flow>";
    static final String MARK_ADD_GLOBAL_LISTENER = "<mark add global listener>";

    static final String ACTION = "<action>";
    static final String CONDITION = "<condition>";
    static final String LISTENER = "<listener>";
    static final String EPSILON_OR_ARGUMENT_LIST = "<epsilon or argument list>";
    static final String ARGUMENT_LIST = "<argument list>";
    static final String ARGUMENT = "<argument>";
    static final String ARGUMENT_VALUE = "<argument value>";

    static final String PROGRAMS = "<programs>";
    static final String EPSILON_OR_GLOBAL_LISTENERS = "<epsilon or global listeners>";

    static final String JOIN_MODE = "<join mode>";
    static final String FLOW_NAME = "<flow name>";
    static final String FLOW_ID = "<flow id>";
    static final String ACTION_NAME = "<action name>";
    static final String CONDITION_NAME = "<condition name>";
    static final String LISTENER_NAME = "<listener name>";
    static final String ARGUMENT_NAME = "<argument name>";
    static final String PLACE_HOLDER_NAME = "<place holder name>";
    static final String LITERAL = "<literal>";
    static final String INTEGER_LITERAL = "<integer literal>";
    static final String FLOATING_POINT_LITERAL = "<floating-point literal>";
    static final String BOOLEAN_LITERAL = "<boolean literal>";
    static final String STRING_LITERAL = "<string literal>";

    static final String IDENTIFIER_INTEGER_LITERAL = "#integerLiteral";
    static final String IDENTIFIER_FLOATING_POINT_LITERAL = "#floatingPointLiteral";
    static final String IDENTIFIER_STRING_LITERAL = "#StringLiteral";

    /**
     * special symbol
     */
    static final String NORMAL_DOLLAR = "$";
    static final String NORMAL_SMALL_LEFT_PARENTHESES = "(";
    static final String NORMAL_SMALL_RIGHT_PARENTHESES = ")";
    static final String NORMAL_MIDDLE_LEFT_PARENTHESES = "[";
    static final String NORMAL_MIDDLE_RIGHT_PARENTHESES = "]";
    static final String NORMAL_LARGE_LEFT_PARENTHESES = "{";
    static final String NORMAL_LARGE_RIGHT_PARENTHESES = "}";
    static final String NORMAL_COMMA = ",";
    static final String NORMAL_ASSIGN = "=";
    static final String NORMAL_BIT_AND = "&";
    static final String NORMAL_BIT_OR = "|";

    /**
     * regex
     */
    static final String REGEX_IDENTIFIER = "@identifier";
    static final String REGEX_DOT_IDENTIFIER = "@dotIdentifier";
    static final String REGEX_SLASH_IDENTIFIER = "@slashIdentifier";
    static final String REGEX_DASH_IDENTIFIER = "@dashIdentifier";

    /**
     * key word
     */
    static final String NORMAL_IF = "if";
    static final String NORMAL_ELSE = "else";

    /**
     * literal
     */
    static final String NORMAL_SUB = "sub";
    static final String NORMAL_SELECT = "select";
    static final String NORMAL_JOIN = "join";
    static final String NORMAL_THEN = "then";
    static final String NORMAL_BOOLEAN_TRUE = "true";
    static final String NORMAL_BOOLEAN_FALSE = "false";
}
