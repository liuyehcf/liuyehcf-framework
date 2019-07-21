package com.github.liuyehcf.framework.rule.engine.dsl.compile;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.LiteralType;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr.AttrFilter;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr.SetAttrFromLexical;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr.SetAttrToLeftNode;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr.SetLiteral;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinMode;

import static com.github.liuyehcf.framework.rule.engine.dsl.compile.Constant.*;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
abstract class TokenProductions {

    static final Production[] PRODUCTIONS = {

            /*
             * <join mode>
             */
            Production.create(
                    /*
                     * <join mode> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(JOIN_MODE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetAttrToLeftNode(AttrName.JOIN_MODE, JoinMode.soft_and),
                            new AttrFilter(AttrName.JOIN_MODE)
                    ),
                    /*
                     * <join mode> → &
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(JOIN_MODE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_AND)
                            ),
                            new SetAttrToLeftNode(AttrName.JOIN_MODE, JoinMode.hard_and),
                            new AttrFilter(AttrName.JOIN_MODE)
                    ),
                    /*
                     * <join mode> → |
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(JOIN_MODE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_OR)
                            ),
                            new SetAttrToLeftNode(AttrName.JOIN_MODE, JoinMode.or),
                            new AttrFilter(AttrName.JOIN_MODE)
                    )
            ),


            /*
             * <action name>
             */
            Production.create(
                    /*
                     * <action name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ACTION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.ACTION_NAME, 0),
                            new AttrFilter(AttrName.ACTION_NAME)
                    ),
                    /*
                     * <action name> → @dotIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ACTION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_DOT_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.ACTION_NAME, 0),
                            new AttrFilter(AttrName.ACTION_NAME)
                    ),
                    /*
                     * <action name> → @slashIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ACTION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_SLASH_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.ACTION_NAME, 0),
                            new AttrFilter(AttrName.ACTION_NAME)
                    )
            ),


            /*
             * <condition name>
             */
            Production.create(
                    /*
                     * <condition name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.CONDITION_NAME, 0),
                            new AttrFilter(AttrName.CONDITION_NAME)
                    ),
                    /*
                     * <condition name> → @dotIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_DOT_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.CONDITION_NAME, 0),
                            new AttrFilter(AttrName.CONDITION_NAME)
                    ),
                    /*
                     * <condition name> → @slashIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_SLASH_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.CONDITION_NAME, 0),
                            new AttrFilter(AttrName.CONDITION_NAME)
                    )
            ),


            /*
             * <listener name>
             */
            Production.create(
                    /*
                     * <listener name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LISTENER_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.LISTENER_NAME, 0),
                            new AttrFilter(AttrName.LISTENER_NAME)
                    ),
                    /*
                     * <listener name> → @dotIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LISTENER_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_DOT_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.LISTENER_NAME, 0),
                            new AttrFilter(AttrName.LISTENER_NAME)
                    ),
                    /*
                     * <listener name> → @slashIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LISTENER_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_SLASH_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.LISTENER_NAME, 0),
                            new AttrFilter(AttrName.LISTENER_NAME)
                    )
            ),


            /*
             * <argument name>
             */
            Production.create(
                    /*
                     * <argument name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.ARGUMENT_NAME, 0),
                            new AttrFilter(AttrName.ARGUMENT_NAME)
                    )
            ),


            /*
             * <place holder name>
             */
            Production.create(
                    /*
                     * <place holder name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PLACE_HOLDER_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.PLACE_HOLDER_NAME, 0),
                            new AttrFilter(AttrName.PLACE_HOLDER_NAME)
                    ),
                    /*
                     * <place holder name> → @dotIdentifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PLACE_HOLDER_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_DOT_IDENTIFIER)
                            ),
                            new SetAttrFromLexical(0, AttrName.PLACE_HOLDER_NAME, 0),
                            new AttrFilter(AttrName.PLACE_HOLDER_NAME)
                    )
            ),


            /*
             * <literal>
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
                            new SetLiteral(LiteralType.INTEGER),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * <literal> → <floating-point literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FLOATING_POINT_LITERAL)
                            ),
                            new SetLiteral(LiteralType.FLOAT),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * <literal> → <boolean literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BOOLEAN_LITERAL)
                            ),
                            new SetLiteral(LiteralType.BOOLEAN),
                            new AttrFilter(AttrName.LITERAL_VALUE)
                    ),
                    /*
                     * <literal> → <string literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LITERAL),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STRING_LITERAL)
                            ),
                            new SetLiteral(LiteralType.STRING),
                            new AttrFilter(AttrName.LITERAL_VALUE)
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
             * <floating-point literal>
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
             * <boolean literal>
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
             * <string literal>
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
