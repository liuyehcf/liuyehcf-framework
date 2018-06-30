package org.liuyehcf.compile.engine.hua.compile.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr.AttrFilter;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr.SetAttrFromLexical;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr.SetIdentifierAttr;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.load.LoadLiteralAndSetType;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_BOOLEAN_FALSE;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_BOOLEAN_TRUE;
import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.REGEX_IDENTIFIER;

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
    public static final String BOOLEAN_LITERAL = "<boolean literal>"; // 342
    public static final String CHARACTER_LITERAL = "<character literal>"; // 344
    public static final String STRING_LITERAL = "<string literal>"; // 348

    public static final String IDENTIFIER_INTEGER_LITERAL = "#integerLiteral";
    public static final String IDENTIFIER_CHARACTER_LITERAL = "#CharacterLiteral";
    public static final String IDENTIFIER_STRING_LITERAL = "#StringLiteral";

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
                            new SetAttrFromLexical(0, AttrName.METHOD_NAME, 0),
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
                     * (1) <literal> → <integer literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INTEGER_LITERAL)
                            ),
                            new LoadLiteralAndSetType(0, Type.TYPE_INT),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * (3) <literal> → <boolean literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BOOLEAN_LITERAL)
                            ),
                            new LoadLiteralAndSetType(0, Type.TYPE_BOOLEAN),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * (4) <literal> → <character literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CHARACTER_LITERAL)
                            ),
                            new LoadLiteralAndSetType(0, Type.TYPE_CHAR),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * (5) <literal> → <string literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STRING_LITERAL)
                            ),
                            new LoadLiteralAndSetType(0, Type.TYPE_CHAR_ARRAY),
                            new AttrFilter(AttrName.TYPE)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * (2) <literal> → <floating-point literal>
                     * (6) <literal> → <null literal>
                     */

            ),


            /*
             * <integer literal> 302
             * DIFFERENT
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
             * <boolean literal> 342
             * SAME
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
             * <character literal> 344
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <character literal> → #CharacterLiteral
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CHARACTER_LITERAL),
                            SymbolString.create(
                                    Symbol.createIdentifierTerminator(IDENTIFIER_CHARACTER_LITERAL)
                            ),
                            new SetAttrFromLexical(0, AttrName.LITERAL_VALUE, 0),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    )
            ),


            /*
             * <string literal> 348
             * DIFFERENT
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
            )
    };
}
