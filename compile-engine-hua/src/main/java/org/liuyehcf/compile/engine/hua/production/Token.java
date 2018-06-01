package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.REGEX_IDENTIFIER;

/**
 * @author chenlu
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

    public static final String REGEX_INTEGER_TYPE_SUFFIX = "l|L";
    public static final String REGEX_NON_ZERO_DIGIT = "[1-9]";
    public static final String REGEX_HEX_DIGIT = "[0-9A-Fa-f]";

    public static final String NORMAL_NUMBER_0 = "0";
    public static final String NORMAL_CHAR_SMALL_X = "x";
    public static final String NORMAL_CHAR_BIG_X = "X";

    public static final Production[] PRODUCTIONS = {
            /*
             * <expression name> 294
             */
            Production.create(
                    /*
                     * <expression name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            )
                            , null
                    )
                    // TODO
            ),


            /*
             * <method name> 296
             */
            Production.create(
                    /*
                     * <method name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            )
                            , null
                    )
                    // TODO
            ),


            /*
             * <literal> 300
             */
            Production.create(
                    /*
                     * <literal> → <integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INTEGER_LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <literal> → <floating-point literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FLOATING_POINT_LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <literal> → <boolean literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BOOLEAN_LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <literal> → <character literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CHARACTER_LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <literal> → <string literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STRING_LITERAL)
                            )
                            , null
                    )
                    // TODO

            ),


            /*
             * <integer literal> 302
             * SAME
             */
            Production.create(
                    /*
                     * <integer literal> → <decimal integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DECIMAL_INTEGER_LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <integer literal> → <hex integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(HEX_INTEGER_LITERAL)
                            )
                            , null
                    ),
                    /*
                     * <integer literal> → <octal integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(OCTAL_INTEGER_LITERAL)
                            )
                            , null
                    )
            ),


            /*
             * <decimal integer literal> 304
             * SAME
             */
            Production.create(
                    /*
                     * <decimal integer literal> → <decimal numeral> <integer type suffix>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DECIMAL_INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DECIMAL_NUMERAL),
                                    Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX)
                            )
                            , null
                    )
            ),


            /*
             * <epsilon or integer type suffix>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or integer type suffix> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <epsilon or integer type suffix> → <integer type suffix>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INTEGER_TYPE_SUFFIX)
                            )
                            , null
                    )
            ),


            /*
             * <hex integer literal> 306
             * SAME
             */
            Production.create(
                    /*
                     * <hex integer literal> → <hex numeral> <integer type suffix>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(HEX_INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(HEX_NUMERAL),
                                    Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX)
                            )
                            , null
                    )
            ),


            /*
             * <octal integer literal> 308
             * SAME
             */
            Production.create(
                    /*
                     * <octal integer literal> → <octal numeral> <integer type suffix>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(OCTAL_INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(OCTAL_NUMERAL),
                                    Symbol.createNonTerminator(EPSILON_OR_INTEGER_TYPE_SUFFIX)
                            )
                            , null
                    )
            ),


            /*
             * <integer type suffix> 310
             * SAME
             */
            Production.create(
                    /*
                     * <integer type suffix> → l | L
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGER_TYPE_SUFFIX),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_INTEGER_TYPE_SUFFIX)
                            )
                            , null
                    )
            ),


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
                            )
                            , null
                    ),
                    /*
                     * <decimal numeral> → <non zero digit> <digits>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DECIMAL_NUMERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NON_ZERO_DIGIT),
                                    Symbol.createNonTerminator(EPSILON_OR_DIGITS)
                            )
                            , null
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
                            )
                            , null
                    ),
                    /*
                     * <epsilon or digits> → <digits>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIGITS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIGITS)
                            )
                            , null
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
                            )
                            , null
                    ),
                    /*
                     * <digits> → <digits> <digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGITS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIGITS),
                                    Symbol.createNonTerminator(DIGIT)
                            )
                            , null
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
                            )
                            , null
                    ),
                    /*
                     * <digit> → <non zero digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NON_ZERO_DIGIT)
                            )
                            , null
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
                            )
                            , null
                    )
            ),


            /*
             * <hex numeral> 320
             * SAME
             */
            Production.create(
                    /*
                     * <hex numeral> → 0 x <hex digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(HEX_NUMERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NUMBER_0),
                                    Symbol.createTerminator(NORMAL_CHAR_SMALL_X),
                                    Symbol.createNonTerminator(HEX_DIGIT)
                            )
                            , null
                    ),
                    /*
                     * <hex numeral> → 0 X <hex digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(HEX_NUMERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NUMBER_0),
                                    Symbol.createTerminator(NORMAL_CHAR_BIG_X),
                                    Symbol.createNonTerminator(HEX_DIGIT)
                            )
                            , null
                    ),
                    /*
                     * <hex numeral> → <hex numeral> <hex digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(HEX_NUMERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(HEX_NUMERAL),
                                    Symbol.createNonTerminator(HEX_DIGIT)
                            )
                            , null
                    )
            ),


            /*
             * <hex digit> 322
             * SAME
             */
            Production.create(
                    /*
                     * <hex digit> → 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | a | b | c | d | e | f | A | B | C | D | E | F
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(HEX_DIGIT),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_HEX_DIGIT)
                            )
                            , null
                    )
            ),


            /*
             * <octal numeral> 324
             */
            Production.create(
                    /*
                     * <octal numeral> →
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(),
                            SymbolString.create(

                            )
                            , null
                    )
            )


            /*
             *  →
             */

            /*

            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(),
                            SymbolString.create(

                            )
                            , null
                    )
            )


             */
    };
}
