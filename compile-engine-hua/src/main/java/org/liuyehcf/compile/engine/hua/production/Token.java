package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.hua.semantic.SetIdentifierAttr;
import org.liuyehcf.compile.engine.hua.semantic.SetAttrFromLexical;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.REGEX_IDENTIFIER;

/**
 * @author hechenfeng
 * @date 2018/5/31
 */
public class Token {
    public static final String EXPRESSION_NAME = "<expression name>"; // 294
    public static final String METHOD_NAME = "<method name>"; // 296
    public static final String LITERAL = "<literal>"; // 300
    public static final String INTEGER_LITERAL = "<integer literal>"; // 302
    public static final String DECIMAL_INTEGER_LITERAL = "<decimal integer literal>"; // 304
    public static final String HEX_INTEGER_LITERAL = "<hex integer literal>"; // 306
    public static final String OCTAL_INTEGER_LITERAL = "<octal integer literal>"; // 308
    public static final String EPSILON_OR_INTEGER_TYPE_SUFFIX = "<epsilon or integer type suffix>"; // new
    public static final String INTEGER_TYPE_SUFFIX = "<integer type suffix>"; // 310
    public static final String DECIMAL_NUMERAL = "<decimal numeral>"; // 312
    public static final String EPSILON_OR_DIGITS = "<epsilon or digits>"; // new
    public static final String DIGITS = "<digits>"; // 314
    public static final String DIGIT = "<digit>"; // 316
    public static final String NON_ZERO_DIGIT = "<non zero digit>"; // 318
    public static final String HEX_NUMERAL = "<hex numeral>"; // 320
    public static final String HEX_DIGIT = "<hex digit>"; // 322
    public static final String OCTAL_NUMERAL = "<octal numeral>"; // 324
    public static final String octal_digit = "<octal digit>"; // 326
    public static final String FLOATING_POINT_LITERAL = "<floating-point literal>"; // 328
    public static final String EXPONENT_PART = "<exponent part>"; // 332
    public static final String EXPONENT_INDICATOR = "<exponent indicator>"; // 334
    public static final String SIGNED_INTEGER = "<signed integer>"; // 336
    public static final String SIGN = "<sign>"; // 338
    public static final String FLOAT_TYPE_SUFFIX = "<float type suffix>"; // 340
    public static final String BOOLEAN_LITERAL = "<boolean literal>"; // 342
    public static final String CHARACTER_LITERAL = "<character literal>"; // 344
    public static final String SINGLE_CHARACTER = "<single character>"; // 346
    public static final String STRING_LITERAL = "<string literal>"; // 348
    public static final String STRING_CHARACTERS = "<string characters>"; // 350
    public static final String STRING_CHARACTER = "<string character>"; // 352

    public static final String REGEX_NON_ZERO_DIGIT = "@nonZeroDigit";
    public static final String REGEX_INTEGER_TYPE_SUFFIX = "@integerTypeSuffix";

    public static final String NORMAL_NUMBER_0 = "0";
    public static final String NORMAL_BOOLEAN_TRUE = "true";
    public static final String NORMAL_BOOLEAN_FALSE = "false";

    public static final Production[] PRODUCTIONS = {
            /*
             * <expression name> 294
             * LACK
             */
            Production.create(
                    /*
                     * <expression name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetIdentifierAttr()
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <expression name> → <ambiguous name> . @identifier
                     */
            ),


            /*
             * <method name> 296
             * LACK
             */
            Production.create(
                    /*
                     * <method name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(
                                    0,
                                    AttrName.METHOD_NAME.name(),
                                    0
                            )
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <method name> → <ambiguous name>. @identifier
                     */
            ),


            /*
             * <literal> 300
             * LACK
             */
            Production.create(
                    /*
                     * <literal> → <integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INTEGER_LITERAL)
                            ),
                            null
                    ),
                    /*
                     * <literal> → <boolean literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BOOLEAN_LITERAL)
                            ),
                            null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <literal> → <floating-point literal>
                     * <literal> → <character literal>
                     * <literal> → <string literal>
                     * <literal> → <null literal>
                     */

            ),


            /*
             * <integer literal> 302
             * LACK
             */
            Production.create(
                    /*
                     * <integer literal> → <decimal integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DECIMAL_INTEGER_LITERAL)
                            ),
                            null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <integer literal> → <hex integer literal>
                     * <integer literal> → <octal integer literal>
                     */
            ),


            /*
             * <decimal integer literal> 304
             * DIFFERENT  TODO 现在没法解决<integer type suffix>的问题，例如变量lo，词法分析器先解析l还是lo呢
             */
            Production.create(
                    /*
                     * <decimal integer literal> → <decimal numeral>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DECIMAL_INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DECIMAL_NUMERAL)
                            ),
                            null
                    )
            ),


//            /*
//             * <epsilon or integer type suffix>
//             * DIFFERENT
//             */
//            Production.create(
//                    /*
//                     * <epsilon or integer type suffix> → ε
//                     */
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX),
//                            SymbolString.create(
//                                    Symbol.EPSILON
//                            )
//                            , null
//                    ),
//                    /*
//                     * <epsilon or integer type suffix> → <integer type suffix>
//                     */
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(INTEGER_TYPE_SUFFIX)
//                            )
//                            , null
//                    )
//            ),


//            /*
//             * <integer type suffix> 310
//             * SAME
//             */
//            Production.create(
//                    /*
//                     * <integer type suffix> → l | L
//                     */
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(INTEGER_TYPE_SUFFIX),
//                            SymbolString.create(
//                                    Symbol.createRegexTerminator(REGEX_INTEGER_TYPE_SUFFIX)
//                            )
//                            , null
//                    )
//            ),

            /*
             * <decimal numeral> 312
             * SAME
             */
            Production.create(
                    /*
                     * <decimal numeral> → 0
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DECIMAL_NUMERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NUMBER_0)
                            ),
                            null
                    ),
                    /*
                     * <decimal numeral> → <non zero digit> <digits>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DECIMAL_NUMERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NON_ZERO_DIGIT),
                                    Symbol.createNonTerminator(EPSILON_OR_DIGITS)
                            ),
                            null
                    )
            ),


            /*
             * <epsilon or digits>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or digits> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIGITS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            null
                    ),
                    /*
                     * <epsilon or digits> → <digits>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIGITS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIGITS)
                            ),
                            null
                    )
            ),


            /*
             * <digits> 314
             * SAME
             */
            Production.create(
                    /*
                     * <digits> → <digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGITS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIGIT)
                            ),
                            null
                    ),
                    /*
                     * <digits> → <digits> <digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGITS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIGITS),
                                    Symbol.createNonTerminator(DIGIT)
                            ),
                            null
                    )
            ),


            /*
             * <digit> 316
             * SAME
             */
            Production.create(
                    /*
                     * <digit> → 0
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGIT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NUMBER_0)
                            ),
                            null
                    ),
                    /*
                     * <digit> → <non zero digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NON_ZERO_DIGIT)
                            ),
                            null
                    )
            ),


            /*
             * <non zero digit> 318
             * SAME
             */
            Production.create(
                    /*
                     * <non zero digit> → 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(NON_ZERO_DIGIT),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_NON_ZERO_DIGIT)
                            ),
                            null
                    )
            ),


            /*
             * <boolean literal> 342
             * SAME
             */
            Production.create(
                    /*
                     * <boolean literal> → true
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BOOLEAN_LITERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BOOLEAN_TRUE)
                            ),
                            null
                    ),
                    /*
                     * <boolean literal> → false
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BOOLEAN_LITERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BOOLEAN_FALSE)
                            ),
                            null
                    )
            )
    };
}
