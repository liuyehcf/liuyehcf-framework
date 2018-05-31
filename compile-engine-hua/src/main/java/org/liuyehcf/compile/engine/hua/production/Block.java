package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.production.Expression.ASSIGNMENT;
import static org.liuyehcf.compile.engine.hua.production.Expression.EXPRESSION;
import static org.liuyehcf.compile.engine.hua.production.Program.VARIABLE_DECLARATORS;
import static org.liuyehcf.compile.engine.hua.production.Type.TYPE;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Block {
    public static final String BLOCK = "<block>"; // 139
    public static final String BLOCK_STATEMENTS = "<block statements>"; // 140
    public static final String BLOCK_STATEMENT = "<block statement>"; // 142
    public static final String LOCAL_VARIABLE_DECLARATION_STATEMENT = "<local variable declaration statement>"; // 144
    public static final String LOCAL_VARIABLE_DECLARATION = "<local variable declaration>"; // 146
    public static final String STATEMENT = "<statement>"; // 148
    public static final String STATEMENT_NO_SHORT_IF = "<statement no short if>"; // 150
    public static final String STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT = "<statement without trailing substatement>"; // 152
    public static final String EMPTY_STATEMENT = "<empty statement>"; // 154
    public static final String EXPRESSION_STATEMENT = "<expression statement>"; // 160
    public static final String STATEMENT_EXPRESSION = "<statement expression>"; // 162
    public static final String IF_THEN_STATEMENT = "<if then statement>"; // 164
    public static final String IF_THEN_ELSE_STATEMENT = "<if then else statement>"; // 166
    public static final String IF_THEN_ELSE_STATEMENT_NO_SHORT_IF = "<if then else statement no short if>"; // 168

    public static final String NORMAL_IF = "if";
    public static final String NORMAL_ELSE = "else";

    public static final Production[] PRODUCTIONS = {

            /*
             * <block> 139
             * SAME
             */
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


            /*
             * <block statements> 140
             * SAME
             */
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


            /*
             * <block statement> 142
             * SAME
             */
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


            /*
             * <local variable declaration statement> 144
             * SAME
             */
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


            /*
             * <local variable declaration> 146
             * SAME
             */
            Production.create(
                    /*
                     * <local variable declaration> → <type> <variable declarators>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS)
                            )
                            , null
                    )
            ),


            /*
             * <statement> 148
             */
            Production.create(
                    /*
                     * <statement> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement> → <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement> → <if then else statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <statement no short if> 150
             */
            Production.create(
                    /*
                     * <statement no short if> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            )
                            , null
                    ),
                    /*
                     * <statement no short if> → <if then else statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <statement without trailing substatement> 152
             */
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


            /*
             * <empty statement> 154
             * SAME
             */
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


            /*
             * <expression statement> 160
             * SAME
             */
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


            /*
             * <statement expression> 162
             */
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


            /*
             * <if then statement> 164
             * SAME
             */
            Production.create(
                    /*
                     * <if then statement> → if ( <expression> ) <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <if then else statement> 166
             * SAME
             */
            Production.create(
                    /*
                     * <if then else statement> → if ( <expression> ) <statement no short if> else <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),


            /*
             * <if then else statement no short if> 168
             * SAME
             */
            Production.create(
                    /*
                     * <if then else statement no short if> → if ( <expression> ) <statement no short if> else <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            )
                            , null
                    )
            ),
    };
}
