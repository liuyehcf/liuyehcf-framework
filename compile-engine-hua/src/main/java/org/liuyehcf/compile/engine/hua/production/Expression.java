package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.production.Token.*;
import static org.liuyehcf.compile.engine.hua.production.Type.PRIMITIVE_TYPE;
import static org.liuyehcf.compile.engine.hua.production.Type.REFERENCE_TYPE;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Expression {

    public static final String EXPRESSION = "<expression>"; // 218
    public static final String ASSIGNMENT_EXPRESSION = "<assignment expression>"; // 220
    public static final String ASSIGNMENT = "<assignment>"; // 222
    public static final String LEFT_HAND_SIDE = "<left hand side>"; // 224
    public static final String ASSIGNMENT_OPERATOR = "<assignment operator>"; // 226
    public static final String CONDITIONAL_EXPRESSION = "<conditional expression>"; // 228
    public static final String CONDITIONAL_OR_EXPRESSION = "<conditional or expression>"; // 230
    public static final String CONDITIONAL_AND_EXPRESSION = "<conditional and expression>"; // 232
    public static final String INCLUSIVE_OR_EXPRESSION = "<inclusive or expression>"; // 234
    public static final String EXCLUSIVE_OR_EXPRESSION = "<exclusive or expression>"; // 236
    public static final String AND_EXPRESSION = "<and expression>"; // 238
    public static final String EQUALITY_EXPRESSION = "<equality expression>"; // 240
    public static final String RELATIONAL_EXPRESSION = "<relational expression>"; // 242
    public static final String SHIFT_EXPRESSION = "<shift expression>"; // 244
    public static final String ADDITIVE_EXPRESSION = "<additive expression>"; // 246
    public static final String MULTIPLICATIVE_EXPRESSION = "<multiplicative expression>"; // 248
    public static final String CAST_EXPRESSION = "<cast expression>"; // 250
    public static final String UNARY_EXPRESSION = "<unary expression>"; // 252
    public static final String PREDECREMENT_EXPRESSION = "<predecrement expression>"; // 254
    public static final String PREINCREMENT_EXPRESSION = "<preincrement expression>"; // 256
    public static final String UNARY_EXPRESSION_NOT_PLUS_MINUS = "<unary expression not plus minus>"; // 258
    public static final String POSTDECREMENT_EXPRESSION = "<postdecrement expression>"; // 260
    public static final String POSTINCREMENT_EXPRESSION = "<postincrement expression>"; // 262
    public static final String POSTFIX_EXPRESSION = "<postfix expression>"; // 264
    public static final String METHOD_INVOCATION = "<method invocation>"; // 266
    public static final String PRIMARY = "<primary>"; // 270
    public static final String PRIMARY_NO_NEW_ARRAY = "<primary no new array>"; // 272
    public static final String EPSILON_OR_ARGUMENT_LIST = "<epsilon or argument list>"; // new
    public static final String ARGUMENT_LIST = "<argument list>"; // 276
    public static final String ARRAY_CREATION_EXPRESSION = "<array creation expression>"; // 278
    public static final String DIM_EXPRS = "<dim exprs>"; // 280
    public static final String DIM_EXPR = "<dim expr>"; // 282
    public static final String EPSILON_OR_DIMS = "<epsilon or dims>"; // new
    public static final String DIMS = "<dims>"; // 284
    public static final String ARRAY_ACCESS = "<array access>"; // 286

    public static final String NORMAL_MUL_ASSIGN = "*=";
    public static final String NORMAL_DIV_ASSIGN = "/=";
    public static final String NORMAL_MOD_ASSIGN = "%=";
    public static final String NORMAL_ADD_ASSIGN = "+=";
    public static final String NORMAL_MINUS_ASSIGN = "-=";
    public static final String NORMAL_LEFT_SHIFT_ASSIGN = "<<=";
    public static final String NORMAL_RIGHT_SHIFT_SIGNED_ASSIGN = ">>=";
    public static final String NORMAL_RIGHT_SHIFT_UNSIGNED_ASSIGN = ">>>=";
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
    public static final String NORMAL_LEFT_SHIFT = "<<";
    public static final String NORMAL_RIGHT_SHIFT_SIGNED = ">>";
    public static final String NORMAL_RIGHT_SHIFT_UNSIGNED = ">>>";
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

    public static final Production[] PRODUCTIONS = {
            /*
             * <expression> 218
             * SAME
             */
            Production.create(
                    /*
                     * <expression> → <assignment expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <assignment expression> 220
             * SAME
             */
            Production.create(
                    /*
                     * <assignment expression> → <conditional expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <assignment expression> → <assignment>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT)
                            )
                            , null
                    )

            ),


            /*
             * <assignment> 222
             * SAME
             */
            Production.create(
                    /*
                     * <assignment> → <left hand side> <assignment operator> <assignment expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LEFT_HAND_SIDE),
                                    Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <left hand side> 224
             * LACK
             */
            Production.create(
                    /*
                     * <left hand side> → <expression name>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LEFT_HAND_SIDE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            )
                            , null
                    ),
                    /*
                     * <left hand side> → <array access>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LEFT_HAND_SIDE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_ACCESS)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <left hand side> → <field access>
                     */
            ),


            /*
             * <assignment operator> 226
             * SAME
             */
            Production.create(
                    /*
                     * <assignment operator> → =
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → *=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MUL_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → \=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DIV_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → %=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MOD_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → +=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ADD_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → -=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MINUS_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → <<=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LEFT_SHIFT_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → >>=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_RIGHT_SHIFT_SIGNED_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → >>>=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_RIGHT_SHIFT_UNSIGNED_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → &=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_AND_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → ^=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_EXCLUSIVE_OR_ASSIGN)
                            )
                            , null
                    ),
                    /*
                     * <assignment operator> → |=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_OR_ASSIGN)
                            )
                            , null
                    )
            ),


            /*
             * <conditional expression> 228
             * SAME
             */
            Production.create(
                    /*
                     * <conditional expression> → <conditional or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <conditional expression> → <conditional or expression> ? <expression> : <conditional expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_QUESTION_MARK),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_COLON),
                                    Symbol.createNonTerminator(CONDITIONAL_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <conditional or expression> 230
             * SAME
             */
            Production.create(
                    /*
                     * <conditional or expression> → <conditional and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <conditional or expression> → <conditional or expression> || <conditional and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LOGICAL_OR),
                                    Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <conditional and expression> 232
             * SAME
             */
            Production.create(
                    /*
                     * <conditional and expression> → <inclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <conditional and expression> → <conditional and expression> && <inclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LOGICAL_AND),
                                    Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <inclusive or expression> 234
             * SAME
             */
            Production.create(
                    /*
                     * <inclusive or expression> → <exclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <inclusive or expression> → <inclusive or expression> | <exclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_BIT_OR),
                                    Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <exclusive or expression> 236
             * SAME
             */
            Production.create(
                    /*
                     * <exclusive or expression> → <and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(AND_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <exclusive or expression> → <exclusive or expression> ^ <and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_BIT_EXCLUSIVE_OR),
                                    Symbol.createNonTerminator(AND_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <and expression> 238
             * SAME
             */
            Production.create(
                    /*
                     * <and expression> → <equality expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <and expression> → <and expression> & <equality expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(AND_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_BIT_AND),
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <equality expression> 240
             * SAME
             */
            Production.create(
                    /*
                     * <equality expression> → <relational expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <equality expression> → <equality expression> == <relational expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_EQUAL),
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <equality expression> → <equality expression> != <relational expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_NOT_EQUAL),
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <relational expression> 242
             * LACK
             */
            Production.create(
                    /*
                     * <relational expression> → <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <relational expression> → <relational expression> < <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LESS),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <relational expression> → <relational expression> > <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LARGE),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <relational expression> → <relational expression> <= <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LESS_EQUAL),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <relational expression> → <relational expression> >= <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LARGE_EQUAL),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <relational expression> → <relational expression> instanceof <reference type>
                     */
            ),


            /*
             * <shift expression> 244
             * SAME
             */
            Production.create(
                    /*
                     * <shift expression> → <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <shift expression> → <shift expression> << <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LEFT_SHIFT),
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <shift expression> → <shift expression> >> <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_RIGHT_SHIFT_SIGNED),
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <shift expression> → <shift expression> >>> <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_RIGHT_SHIFT_UNSIGNED),
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <additive expression> 246
             * SAME
             */
            Production.create(
                    /*
                     * <additive expression> → <multiplicative expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <additive expression> → <additive expression> + <multiplicative expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_ADD),
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <additive expression> → <additive expression> - <multiplicative expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MINUS),
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <multiplicative expression> 248
             * SAME
             */
            Production.create(
                    /*
                     * <multiplicative expression> → <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> * <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MUL),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> / <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DIV),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> % <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MOD),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <cast expression> 250
             * SAME
             */
            Production.create(
                    /*
                     * <cast expression> → ( <primitive type> ) <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CAST_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(PRIMITIVE_TYPE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <cast expression> → ( <reference type> ) <unary expression not plus minus>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CAST_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(REFERENCE_TYPE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS)
                            ),
                            null
                    )
            ),


            /*
             * <unary expression> 252
             * SAME
             */
            Production.create(
                    /*
                     * <unary expression> → <preincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREINCREMENT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression> → <predecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREDECREMENT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression> → + <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ADD),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression> → - <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MINUS),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression> → <unary expression not plus minus>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS)
                            )
                            , null
                    )
            ),


            /*
             * <predecrement expression> 254
             * SAME
             */
            Production.create(
                    /*
                     * <predecrement expression> → -- <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PREDECREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DOUBLE_MINUS),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <preincrement expression> 256
             * SAME
             */
            Production.create(
                    /*
                     * <preincrement expression> → ++ <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PREINCREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DOUBLE_PLUS),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <unary expression not plus minus> 258
             * SAME
             */
            Production.create(
                    /*
                     * <unary expression not plus minus> → <postfix expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTFIX_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression not plus minus> → ~ <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_REVERSED),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression not plus minus> → ! <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LOGICAL_NOT),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <unary expression not plus minus> → <cast expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CAST_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <postdecrement expression> 260
             * SAME
             */
            Production.create(
                    /*
                     * <postdecrement expression> → <postfix expression> --
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTDECREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DOUBLE_MINUS)
                            )
                            , null
                    )
            ),


            /*
             * <postincrement expression> 262
             * SAME
             */
            Production.create(
                    /*
                     * <postincrement expression> → <postfix expression> ++
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTINCREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DOUBLE_PLUS)
                            )
                            , null
                    )
            ),


            /*
             * <postfix expression> 264
             * SAME
             */
            Production.create(
                    /*
                     * <postfix expression> → <primary>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY)
                            )
                            , null
                    ),
                    /*
                     * <postfix expression> → <expression name>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            )
                            , null
                    ),
                    /*
                     * <postfix expression> → <postincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTINCREMENT_EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <postfix expression> → <postdecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTDECREMENT_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <method invocation> 266
             * LACK
             */
            Production.create(
                    /*
                     * <method invocation> → <method name> ( <argument list>? )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_INVOCATION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_NAME),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <method invocation> → <primary> . <identifier> ( <argument list>? )
                     * <method invocation> → super . <identifier> ( <argument list>? )
                     */
            ),


            /*
             * <epsilon or argument list>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or argument list> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <epsilon or argument list> → <argument list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST)
                            )
                            , null
                    )
            ),


            /*
             * <primary> 270
             * SAME
             */
            Production.create(
                    /*
                     * <primary> → <primary no new array>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY)
                            )
                            , null
                    ),
                    /*
                     * <primary> → <array creation expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_CREATION_EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <primary no new array> 272
             * LACK
             */
            Production.create(
                    /*
                     * <primary no new array> → <literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <primary no new array> → ( <expression> )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            )
                            , null
                    ),
                    /*
                     * <primary no new array> → <method invocation>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_INVOCATION)
                            )
                            , null
                    ),
                    /*
                     * <primary no new array> → <array access>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_ACCESS)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <primary no new array> → this
                     * <primary no new array> → <class instance creation expression>
                     * <primary no new array> → <field access>
                     */
            ),


            /*
             * <argument list> 276
             * SAME
             */
            Production.create(
                    /*
                     * <argument list> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            )
                            , null
                    ),
                    /*
                     * <argument list> → <argument list> , <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(EXPRESSION)
                            )
                            , null
                    )
            ),


            /*
             * <array creation expression> 278
             * LACK
             */
            Production.create(
                    /*
                     * <array creation expression> → new <primitive type> <dim exprs> <dims>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_CREATION_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NEW),
                                    Symbol.createNonTerminator(PRIMITIVE_TYPE),
                                    Symbol.createNonTerminator(DIM_EXPRS),
                                    Symbol.createNonTerminator(EPSILON_OR_DIMS)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <array creation expression> → new <class or interface type> <dim exprs> <dims>?
                     */
            ),


            /*
             * <epsilon or dims>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or dims> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIMS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <epsilon or dims> → <dims>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIMS)
                            )
                            , null
                    )
            ),


            /*
             * <dim exprs> 280
             * SAME
             */
            Production.create(
                    /*
                     * <dim exprs> → <dim expr>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIM_EXPRS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIM_EXPR)
                            )
                            , null
                    ),
                    /*
                     * <dim exprs> → <dim exprs> <dim expr>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIM_EXPRS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIM_EXPRS),
                                    Symbol.createNonTerminator(DIM_EXPR)
                            )
                            , null
                    )
            ),


            /*
             * <dim expr> 282
             * SAME
             */
            Production.create(
                    /*
                     * <dim expr> → [ <expression> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIM_EXPR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),


            /*
             * <dims> 284
             * SAME
             */
            Production.create(
                    /*
                     * <dims> → [ ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIMS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    ),
                    /*
                     * <dims> → <dims> [ ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIMS),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),


            /*
             * <array access> 286
             * SAME
             */
            Production.create(
                    /*
                     * <array access> → <expression name> [ <expression> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_ACCESS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    ),
                    /*
                     * <array access> → <primary no new array> [ <expression>]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_ACCESS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
    };
}
