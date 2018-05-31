package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.production.Block.BLOCK;
import static org.liuyehcf.compile.engine.hua.production.Expression.EXPRESSION;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Program {
    public static final String PROGRAMS = "<programs>";
    public static final String FORMAL_PARAMETER_LIST = "<formal parameter list>";
    public static final String VARIABLE_DECLARATORS = "<variable declarators>";
    public static final String VARIABLE_DECLARATOR = "<variable declarator>";
    public static final String VARIABLE_DECLARATOR_ID = "<variable declarator id>";
    public static final String VARIABLE_INITIALIZER = "<variable initializer>";
    public static final String METHOD_DECLARATIONS = "<method declarations>";
    public static final String METHOD_DECLARATION = "<method declaration>";
    public static final String METHOD_BODY = "<method body>";

    public static final String FORMAL_PARAMETERS = "<formal parameters>";
    public static final String FORMAL_PARAMETER = "<formal parameter>";

    public static final Production[] PRODUCTIONS = {
            Production.create(
                    /*
                     * <programs> → <method declarations>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATIONS)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <variable declarators> → <variable declarator>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR)
                            )
                            , null
                    ),
                    /*
                     * <variable declarators> → <variable declarators> , <variable declarator>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <variable declarator> → <variable declarator id>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID)
                            )
                            , null
                    ),
                    /*
                     * <variable declarator> → <variable declarator id> = <variable initializer>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                                    Symbol.createTerminator(NORMAL_ASSIGN),
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZER)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <variable declarator id> →  @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            )
                            , null
                    ),
                    /*
                     * <variable declarator id> →  <variable declarator id> []
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <variable initializer> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_INITIALIZER),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <method declarations> →  <method declarations>  <method declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATIONS),
                                    Symbol.createNonTerminator(METHOD_DECLARATION)
                            )
                            , null
                    ),
                    /*
                     * <method declarations> → <method declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATION)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <method declaration> → @type @identifier ( <formal parameters> ) <method body>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATION),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_TYPE),
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(FORMAL_PARAMETERS),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(METHOD_BODY)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <formal parameters> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETERS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    ),
                    /*
                     * <formal parameters> → <formal parameter list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER_LIST)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <formal parameter list> → <formal parameter list> , <formal parameter>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(FORMAL_PARAMETER)
                            )
                            , null
                    ),
                    /*
                     * <formal parameter list> → <formal parameter>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <formal parameter> → @type @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_TYPE),
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <method body> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_BODY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            )
                            , null
                    )
            ),
    };
}
