package org.liuyehcf.compile.engine.expression.compile.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.expression.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.expression.compile.definition.model.LiteralType;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.AttrFilter;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.SetAttrFromLexical;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.SetIdentifierAttr;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.code.PushConstByteCode;

import static org.liuyehcf.compile.engine.expression.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.expression.compile.definition.GrammarDefinition.REGEX_IDENTIFIER;

/**
 * Token相关的产生式
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
abstract class TokenProductions {
    public static final String EXPRESSION_NAME = "<expression name>"; // 294
    public static final String METHOD_NAME = "<method name>"; // 296
    public static final String LITERAL = "<literal>"; // 300
    public static final String INTEGER_LITERAL = "<integer literal>"; // 302
    public static final String FLOATING_POINT_LITERAL = "<floating-point literal>"; // 328
    public static final String BOOLEAN_LITERAL = "<boolean literal>"; // 342
    public static final String STRING_LITERAL = "<string literal>"; // 348
    public static final String NULL_LITERAL = "<null literal>"; // 354

    public static final String IDENTIFIER_INTEGER_LITERAL = "#integerLiteral";
    public static final String IDENTIFIER_FLOATING_POINT_LITERAL = "#floatingPointLiteral";
    public static final String IDENTIFIER_STRING_LITERAL = "#StringLiteral";

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
                            ),
                            new SetIdentifierAttr(0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME)
                    )
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
                            ),
                            new SetAttrFromLexical(0, AttrName.METHOD_NAME, 0),
                            new AttrFilter(AttrName.METHOD_NAME)
                    )
            ),


            /*
             * <literal> 300
             */
            Production.create(
                    /*
                     * (1) <literal> → <integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INTEGER_LITERAL)
                            ),
                            new PushConstByteCode(0, LiteralType.INTEGER),
                            new AttrFilter()
                    ),
                    /*
                     * (2) <literal> → <floating-point literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FLOATING_POINT_LITERAL)
                            ),
                            new PushConstByteCode(0, LiteralType.FLOAT),
                            new AttrFilter()
                    ),
                    /*
                     * (3) <literal> → <boolean literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BOOLEAN_LITERAL)
                            ),
                            new PushConstByteCode(0, LiteralType.BOOLEAN),
                            new AttrFilter()
                    ),
                    /*
                     * (5) <literal> → <string literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STRING_LITERAL)
                            ),
                            new PushConstByteCode(0, LiteralType.STRING),
                            new AttrFilter()
                    ),
                    /*
                     * (5) <literal> → <null literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NULL_LITERAL)
                            ),
                            new PushConstByteCode(0, LiteralType.NULL),
                            new AttrFilter()
                    )
            ),


            /*
             * <integer literal> 302
             */
            Production.create(
                    /*
                     * (1) <integer literal> → #integerLiteral
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGER_LITERAL),
                            SymbolString.create(
                                    Symbol.createIdentifierTerminator(IDENTIFIER_INTEGER_LITERAL)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE, 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    )
            ),


            /*
             * <floating-point literal> 328
             */
            Production.create(
                    /*
                     * (1) <floating-point literal> → #floatingPointLiteral
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FLOATING_POINT_LITERAL),
                            SymbolString.create(
                                    Symbol.createIdentifierTerminator(IDENTIFIER_FLOATING_POINT_LITERAL)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE, 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    )
            ),


            /*
             * <boolean literal> 342
             */
            Production.create(
                    /*
                     * (1) <boolean literal> → true
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BOOLEAN_LITERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BOOLEAN_TRUE)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE, 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * (2) <boolean literal> → false
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BOOLEAN_LITERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BOOLEAN_FALSE)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE, 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    )
            ),


            /*
             * <string literal> 348
             */
            Production.create(
                    /*
                     * <string literal> → #StringLiteral
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STRING_LITERAL),
                            SymbolString.create(
                                    Symbol.createIdentifierTerminator(IDENTIFIER_STRING_LITERAL)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE, 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    )
            ),


            /*
             * <null literal> 354
             */
            Production.create(
                    /*
                     * <string literal> → null
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(NULL_LITERAL),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NULL)
                            ),
                            new AttrFilter()
                    )
            )
    };
}
