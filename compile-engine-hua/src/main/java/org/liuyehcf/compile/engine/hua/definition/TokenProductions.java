package org.liuyehcf.compile.engine.hua.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.hua.semantic.attr.*;
import org.liuyehcf.compile.engine.hua.semantic.load.LiteralLoad;

import static org.liuyehcf.compile.engine.hua.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.REGEX_IDENTIFIER;
import static org.liuyehcf.compile.engine.hua.model.Type.TYPE_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.model.Type.TYPE_INT;

/**
 * Token相关的产生式
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
abstract class TokenProductions {
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
                            new SetIdentifierAttr(0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
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
                            new SetAttrFromLexical(0, AttrName.METHOD_NAME.name(), 0),
                            new AttrFilter(AttrName.METHOD_NAME)
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
                            new LiteralLoad(0, NORMAL_INT),
                            new SetAttrFromSystem(0, AttrName.TYPE.name(), TYPE_INT),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * <literal> → <boolean literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BOOLEAN_LITERAL)
                            ),
                            new LiteralLoad(0, NORMAL_BOOLEAN),
                            new SetAttrFromSystem(0, AttrName.TYPE.name(), TYPE_BOOLEAN),
                            new AttrFilter(AttrName.TYPE)
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
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new SetAttrFromSystem(0, AttrName.LITERAL_VALUE.name(), "0"),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new ContactLiteral(-1, 0, AttrName.LITERAL_VALUE),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new SetAttrToLeftNode(AttrName.LITERAL_VALUE.name(), ""),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * <epsilon or digits> → <digits>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIGITS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIGITS)
                            ),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new ContactLiteral(-1, 0, AttrName.LITERAL_VALUE),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE.name(), 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * <digit> → <non zero digit>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIGIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NON_ZERO_DIGIT)
                            ),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE.name(), 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE.name(), 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * <boolean literal> → false
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BOOLEAN_LITERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BOOLEAN_FALSE)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE.name(), 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    )
            )
    };
}
