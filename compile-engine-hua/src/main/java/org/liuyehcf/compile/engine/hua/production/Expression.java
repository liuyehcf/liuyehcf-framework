package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.NORMAL_ASSIGN;
import static org.liuyehcf.compile.engine.hua.production.Token.EXPRESSION_NAME;

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
    public static final String ARGUMENT_LIST = "<argument list>"; // 276
    public static final String ARRAY_CREATION_EXPRESSION = "<array creation expression>"; // 278
    public static final String DIM_EXPRS = "<dim exprs>"; // 280
    public static final String dim_expr = "<dim expr>"; // 282
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
                    // TODO 可以扩展更为复杂的语法
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
    };
}
