package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.*;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Block {
    public static final Production[] PRODUCTIONS = {
            Production.create(
                    /*
                     * <block> → { }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    ),
                    /*
                     * <block> → { <block statements> }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <block statements> → <block statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <block statements> → <block statements> <block statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <block statement> → <local variable declaration statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <block statement> → <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <local variable declaration statement> → <local variable declaration> ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <local variable declaration> → @type <variable declarators>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_TYPE),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <statement> →  <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),
            Production.create(
                    /*
                     * <statement without trailing substatement> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            )
                            , null
                    ),
                    /*
                     * <statement without trailing substatement> → <empty statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EMPTY_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement without trailing substatement> → <expression statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_STATEMENT)
                            )
                            , null
                    )
//                    /*
//                     * <statement without trailing substatement> → <return statement>
//                     */
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(RETURN_STATEMENT)
//                            )
//                            , null
//                    )
                    // TODO 可以扩展更为复杂的语法
            ),
            Production.create(
                    /*
                     * <empty statement> → ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EMPTY_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <expression statement> → <statement expression> ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <statement expression> → <assignment>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),
    };
}
