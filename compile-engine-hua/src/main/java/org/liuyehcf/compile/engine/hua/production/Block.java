package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.action.BlockAction.ACTION_146_1;
import static org.liuyehcf.compile.engine.hua.action.BlockAction.ACTION_146_1_1;
import static org.liuyehcf.compile.engine.hua.production.Expression.*;
import static org.liuyehcf.compile.engine.hua.production.Program.VARIABLE_DECLARATORS;
import static org.liuyehcf.compile.engine.hua.production.Type.TYPE;

/**
 * @author hechenfeng
 * @date 2018/5/31
 */
public class Block {
    public static final String BLOCK = "<block>"; // 139
    public static final String EPSILON_OR_BLOCK_STATEMENTS = "<epsilon or block statements>"; // new
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
    public static final String WHILE_STATEMENT = "<while statement>"; // 182
    public static final String WHILE_STATEMENT_NO_SHORT_IF = "<while statement no short if>"; // 184
    public static final String DO_STATEMENT = "<do statement>"; // 186
    public static final String FOR_STATEMENT = "<for statement>"; // 188
    public static final String FOR_STATEMENT_NO_SHORT_IF = "<for statement no short if>"; // 190
    public static final String EPSILON_OR_FOR_INIT = "<epsilon or for init>"; // new
    public static final String FOR_INIT = "<for init>"; // 192
    public static final String EPSILON_OR_EXPRESSION = "<epsilon or expression>"; // new
    public static final String EPSILON_OR_FOR_UPDATE = "<epsilon or for update>"; // new
    public static final String FOR_UPDATE = "<for update>"; // 194
    public static final String STATEMENT_EXPRESSION_LIST = "<statement expression list>"; // 196
    public static final String RETURN_STATEMENT = "<return statement>"; // 202
    public static final String NORMAL_IF = "if";
    public static final String NORMAL_ELSE = "else";
    public static final String NORMAL_WHILE = "while";
    public static final String NORMAL_FOR = "for";
    public static final String NORMAL_DO = "do";
    public static final String NORMAL_RETURN = "return";
    private static final String MARK_146_1_1 = "<mark 146_1_1>";
    public static final Production[] PRODUCTIONS = {

            /*
             * <block> 139
             * SAME
             */
            Production.create(
                    /*
                     * <block> → { <block statements>? }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_BLOCK_STATEMENTS),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            ),
                            null
                    )
            ),


            /*
             * <epsilon or block statements>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or block statements> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            null
                    ),
                    /*
                     * <epsilon or block statements> → <block statements>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS)
                            ),
                            null
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
                            ),
                            null
                    ),
                    /*
                     * <block statements> → <block statements> <block statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            ),
                            null
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
                            ),
                            null
                    ),
                    /*
                     * <block statement> → <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            null
                    )
            ),


            /*
             * <local variable declaration statement> 144
             * SAME
             */
            Production.create(
                    /*
                     * (1) <local variable declaration statement> → <local variable declaration> ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            ),
                            null
                    )
            ),


            /*
             * <local variable declaration> 146
             * SAME
             */
            Production.create(
                    /*
                     * <local variable declaration> → <type> <mark 146_1_1> <variable declarators>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE),
                                    Symbol.createNonTerminator(MARK_146_1_1),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS)
                            ),
                            ACTION_146_1
                    )
            ),


            /*
             * <mark 146_1_1>
             */
            Production.create(
                    /*
                     * <mark 146_1_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_146_1_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            ACTION_146_1_1
                    )
            ),


            /*
             * <statement> 148
             * LACK
             */
            Production.create(
                    /*
                     * <statement> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement> → <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement> → <if then else statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement> → <while statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(WHILE_STATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement> → <for statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_STATEMENT)
                            ),
                            null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <statement> → <labeled statement>
                     */
            ),


            /*
             * <statement no short if> 150
             * LACK
             */
            Production.create(
                    /*
                     * <statement no short if> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement no short if> → <if then else statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF)
                            ),
                            null
                    ),
                    /*
                     * <statement no short if> → <while statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(WHILE_STATEMENT_NO_SHORT_IF)
                            ),
                            null
                    ),
                    /*
                     * <statement no short if> → <for statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_STATEMENT_NO_SHORT_IF)
                            ),
                            null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <statement no short if> → <labeled statement no short if>
                     */
            ),


            /*
             * <statement without trailing substatement> 152
             * LACK
             */
            Production.create(
                    /*
                     * <statement without trailing substatement> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            null
                    ),
                    /*
                     * <statement without trailing substatement> → <empty statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EMPTY_STATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement without trailing substatement> → <expression statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_STATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement without trailing substatement> → <do statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DO_STATEMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement without trailing substatement> → <return statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RETURN_STATEMENT)
                            ),
                            null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <statement without trailing substatement> → <switch statement>
                     * <statement without trailing substatement> → <break statement>
                     * <statement without trailing substatement> → <continue statement>
                     * <statement without trailing substatement> → <synchronized statement>
                     * <statement without trailing substatement> → <throws statements>
                     * <statement without trailing substatement> → <try statement>
                     */
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
                            ),
                            null
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
                            ),
                            null
                    )
            ),


            /*
             * <statement expression> 162
             * LACK
             */
            Production.create(
                    /*
                     * <statement expression> → <assignment>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT)
                            ),
                            null
                    ),
                    /*
                     * <statement expression> → <preincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREINCREMENT_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <statement expression> → <postincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTINCREMENT_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <statement expression> → <predecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREDECREMENT_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <statement expression> → <postdecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTDECREMENT_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <statement expression> → <method invocation>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_INVOCATION)
                            ),
                            null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <statement expression> → <class instance creation expression>
                     */
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
                            ),
                            null
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
                            ),
                            null
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
                            ),
                            null
                    )
            ),


            /*
             * <while statement> 182
             * SAME
             */
            Production.create(
                    /*
                     * <while statement> → while ( <expression> ) <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(WHILE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            null
                    )
            ),


            /*
             * <while statement no short if> 184
             * SAME
             */
            Production.create(
                    /*
                     * <while statement no short if> → while ( <expression> ) <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(WHILE_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            ),
                            null
                    )
            ),


            /*
             * <do statement> 186
             * SAME
             */
            Production.create(
                    /*
                     * <do statement> → do <statement> while ( <expression> ) ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DO_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DO),
                                    Symbol.createNonTerminator(STATEMENT),
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            ),
                            null
                    )
            ),


            /*
             * <for statement> 188
             * SAME
             */
            Production.create(
                    /*
                     * <for statement> → for ( <for init>? ; <expression>? ; <for update>? ) <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_FOR),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            null
                    )
            ),


            /*
             * <epsilon or for init>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or for init> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            null
                    ),
                    /*
                     * <epsilon or for init> → <for init>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_INIT)
                            ),
                            null
                    )
            ),


            /*
             * <epsilon or expression>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or expression> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            null
                    ),
                    /*
                     * <epsilon or expression> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            null
                    )
            ),


            /*
             * <epsilon or for update>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or for update> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            null
                    ),
                    /*
                     * <epsilon or for update> → <for update>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_UPDATE)
                            ),
                            null
                    )
            ),


            /*
             * <for statement no short if> 190
             * SAME
             */
            Production.create(
                    /*
                     * <for statement no short if> → for ( <for init>? ; <expression>? ; <for update>? ) <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_FOR),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            ),
                            null
                    )
            ),


            /*
             * <for init> 192
             * SAME
             */
            Production.create(
                    /*
                     * <for init> → <statement expression list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST)
                            ),
                            null
                    ),
                    /*
                     * <for init> → <local variable declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION)
                            ),
                            null
                    )
            ),


            /*
             * <for update> 194
             * SAME
             */
            Production.create(
                    /*
                     * <for update> → <statement expression list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST)
                            ),
                            null
                    )
            ),


            /*
             * <statement expression list> 196
             * SAME
             */
            Production.create(
                    /*
                     * <statement expression list> → <statement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <statement expression list> → <statement expression list> , <statement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION)
                            ),
                            null
                    )
            ),


            /*
             * <return statement> 202
             * SAME
             */
            Production.create(
                    /*
                     * <return statement> → return <expression>? ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RETURN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_RETURN),
                                    Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            ),
                            null
                    )
            )
    };

}
