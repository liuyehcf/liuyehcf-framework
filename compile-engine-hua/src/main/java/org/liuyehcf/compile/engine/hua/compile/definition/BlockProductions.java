package org.liuyehcf.compile.engine.hua.compile.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.BackFillType;
import org.liuyehcf.compile.engine.hua.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.compile.definition.model.StatementType;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr.*;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill.ControlTransferByteCodeBackFill;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill.ControlTransferByteCodeBackFillWithLoop;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.code.*;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.node.AddFutureSyntaxNode;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable.EnterNamespace;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable.ExitNamespace;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.ExpressionProductions.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.ProgramProductions.VARIABLE_DECLARATORS;
import static org.liuyehcf.compile.engine.hua.compile.definition.TypeProductions.TYPE;
import static org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction.NOT_NULL;

/**
 * Block相关的产生式
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
abstract class BlockProductions {
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
    public static final String MARK_TRUE_BLOCK = "<mark true block>";
    public static final String MARK_FALSE_BLOCK = "<mark false block>";
    private static final String MARK_139_1_1 = "<mark 139_1_1>";
    private static final String MARK_146_1_1 = "<mark 146_1_1>";
    private static final String MARK_LOOP_OFFSET = "<mark loop offset>";
    private static final String MARK_BEFORE_INIT = "<mark before init>";
    private static final String MARK_BEFORE_UPDATE = "<mark before update>";
    private static final String MARK_AFTER_UPDATE = "<mark after update>";

    public static final Production[] PRODUCTIONS = {

            /*
             * <block> 139
             * SAME
             */
            Production.create(
                    /*
                     * (1) <block> → { <mark 139_1_1> <block statements>? }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_139_1_1),
                                    Symbol.createNonTerminator(EPSILON_OR_BLOCK_STATEMENTS),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            ),
                            new ExitNamespace(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark 139_1_1>
             */
            Production.create(
                    /*
                     * <mark 139_1_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_139_1_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new EnterNamespace(),
                            new AttrFilter()
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
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or block statements> → <block statements>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS)
                            ),
                            new AttrFilter()
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
                            new AttrFilter()
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
                            new AttrFilter()
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
                            new AttrFilter()
                    ),
                    /*
                     * <block statement> → <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new AttrFilter()
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
                            new AttrFilter()
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
                            new AttrFilter()
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
                            new AddFutureSyntaxNode(1),
                            new AssignAttr(0, 1, AttrName.TYPE),
                            new AttrFilter()
                    )
            ),


            /*
             * <statement> 148
             * LACK
             */
            Production.create(
                    /*
                     * (1) <statement> → <statement without trailing substatement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (3) <statement> → <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (4) <statement> → <if then else statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (5) <statement> → <while statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(WHILE_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (6) <statement> → <for statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_STATEMENT)
                            ),
                            new AttrFilter()
                    )
                    /*
                     * TODO 缺少以下产生式
                     * (2) <statement> → <labeled statement>
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
                            new AttrFilter()
                    ),
                    /*
                     * <statement no short if> → <if then else statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement no short if> → <while statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(WHILE_STATEMENT_NO_SHORT_IF)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement no short if> → <for statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_STATEMENT_NO_SHORT_IF)
                            ),
                            new AttrFilter()
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
                     * (1) <statement without trailing substatement> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (2) <statement without trailing substatement> → <empty statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EMPTY_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (3) <statement without trailing substatement> → <expression statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (5) <statement without trailing substatement> → <do statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DO_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (8) <statement without trailing substatement> → <return statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RETURN_STATEMENT)
                            ),
                            new AttrFilter()
                    )
                    /*
                     * TODO 缺少以下产生式
                     * (4) <statement without trailing substatement> → <switch statement>
                     * (6) <statement without trailing substatement> → <break statement>
                     * (7) <statement without trailing substatement> → <continue statement>
                     * (9) <statement without trailing substatement> → <synchronized statement>
                     * (10) <statement without trailing substatement> → <throws statements>
                     * (11) <statement without trailing substatement> → <try statement>
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
                            new AttrFilter()
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
                            new RemoveRedundantLoadByteCode(-1),
                            new AttrFilter()
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
                            new SetAttrFromSystem(0, AttrName.STATEMENT_TYPE, StatementType.ASSIGNMENT),
                            new AttrFilter(AttrName.STATEMENT_TYPE)
                    ),
                    /*
                     * <statement expression> → <preincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREINCREMENT_EXPRESSION)
                            ),
                            new SetAttrFromSystem(0, AttrName.STATEMENT_TYPE, StatementType.PRE_INCREMENT),
                            new AttrFilter(AttrName.STATEMENT_TYPE)
                    ),
                    /*
                     * <statement expression> → <postincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTINCREMENT_EXPRESSION)
                            ),
                            new SetAttrFromSystem(0, AttrName.STATEMENT_TYPE, StatementType.POST_INCREMENT),
                            new AttrFilter(AttrName.STATEMENT_TYPE)
                    ),
                    /*
                     * <statement expression> → <predecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREDECREMENT_EXPRESSION)
                            ),
                            new SetAttrFromSystem(0, AttrName.STATEMENT_TYPE, StatementType.PRE_DECREMENT),
                            new AttrFilter(AttrName.STATEMENT_TYPE)
                    ),
                    /*
                     * <statement expression> → <postdecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTDECREMENT_EXPRESSION)
                            ),
                            new SetAttrFromSystem(0, AttrName.STATEMENT_TYPE, StatementType.POST_DECREMENT),
                            new AttrFilter(AttrName.STATEMENT_TYPE)
                    ),
                    /*
                     * <statement expression> → <method invocation>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_INVOCATION)
                            ),
                            new SetAttrFromSystem(0, AttrName.STATEMENT_TYPE, StatementType.METHOD_INVOCATION),
                            new AttrFilter(AttrName.STATEMENT_TYPE)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <statement expression> → <class instance creation expression>
                     */
            ),


            /*
             * <mark true block>
             */
            Production.create(
                    /*
                     * <mark true block> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushControlTransferByteCodeByType(-1, -1, BackFillType.FALSE, false),
                            new ControlTransferByteCodeBackFill(-1, BackFillType.TRUE),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark false block>
             */
            Production.create(
                    /*
                     * <mark false block> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_FALSE_BLOCK),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushControlTransferByteCode(-4, ControlTransferType.GOTO, BackFillType.NEXT),
                            new ControlTransferByteCodeBackFill(-4, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <if then statement> 164
             * SAME
             */
            Production.create(
                    /*
                     * (1) <if then statement> → if ( <expression> ) <mark true block> <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new ControlTransferByteCodeBackFill(-3, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <if then else statement> 166
             * SAME
             */
            Production.create(
                    /*
                     * (1) <if then else statement> → if ( <expression> ) <mark true block> <statement no short if> else <mark false block> <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(MARK_FALSE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new ControlTransferByteCodeBackFill(-6, BackFillType.NEXT),
                            new AttrFilter()
                    )
            ),


            /*
             * <if then else statement no short if> 168
             * SAME
             */
            Production.create(
                    /*
                     * (1) <if then else statement no short if> → if ( <expression> ) <mark true block> <statement no short if> else <mark false block> <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(MARK_FALSE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            ),
                            new ControlTransferByteCodeBackFill(-6, BackFillType.NEXT),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark loop offset>
             */
            Production.create(
                    /*
                     * <mark loop offset> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_LOOP_OFFSET),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetCodeOffsetAttr(),
                            new AttrFilter(AttrName.CODE_OFFSET)
                    )
            ),


            /*
             * <while statement> 182
             * SAME
             */
            Production.create(
                    /*
                     * <while statement> → while ( <mark loop offset> <expression> ) <mark true block> <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(WHILE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_LOOP_OFFSET),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new PushGotoByteCode(-4),
                            new ControlTransferByteCodeBackFill(-3, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <while statement no short if> 184
             * SAME
             */
            Production.create(
                    /*
                     * <while statement no short if> → while ( <mark loop offset> <expression> ) <mark true block> <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(WHILE_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_LOOP_OFFSET),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            ),
                            new PushGotoByteCode(-4),
                            new ControlTransferByteCodeBackFill(-3, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <do statement> 186
             * SAME
             */
            Production.create(
                    /*
                     * <do statement> → do <mark loop offset> <statement> while ( <expression> ) ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DO_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DO),
                                    Symbol.createNonTerminator(MARK_LOOP_OFFSET),
                                    Symbol.createNonTerminator(STATEMENT),
                                    Symbol.createTerminator(NORMAL_WHILE),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            ),
                            new PushControlTransferByteCodeByType(-2, -2, BackFillType.TRUE, true),
                            new ControlTransferByteCodeBackFillWithLoop(-2, -6, BackFillType.TRUE),
                            new ControlTransferByteCodeBackFill(-2, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
            * <mark before init>
            */
            Production.create(
                    /*
                     * <mark before init> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_BEFORE_INIT),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new EnterNamespace(),
                            new AttrFilter()
                    )
            ),


            /*
            * <mark before update>
            */
            Production.create(
                    /*
                     * <mark before update> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_BEFORE_UPDATE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetCodeOffsetAttr(),
                            new AttrFilter(AttrName.CODE_OFFSET)
                    )
            ),


            /*
            * <mark after update>
            */
            Production.create(
                    /*
                     * <mark after update> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_AFTER_UPDATE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetCodeOffsetAttr(),
                            new PushControlTransferByteCodeWhenNecessary(-4, -4, BackFillType.FALSE, false, -4),
                            new ControlTransferByteCodeBackFill(-4, BackFillType.TRUE),
                            new AttrFilter(AttrName.CODE_OFFSET)
                    )
            ),


            /*
             * <for statement> 188
             * SAME
             */
            Production.create(
                    /*
                     * (1) <for statement> → for ( <mark before init> <for init>? ; <mark loop offset> <expression>? ; <mark before update> <for update>? ) <mark after update> <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_FOR),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_BEFORE_INIT),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(MARK_LOOP_OFFSET),
                                    Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(MARK_BEFORE_UPDATE),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_AFTER_UPDATE),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new MoveUpdateByteCodes(-4, -1),
                            new PushGotoByteCode(-7),
                            new ControlTransferByteCodeBackFill(-6, BackFillType.FALSE),
                            new ExitNamespace(),
                            new AttrFilter()
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
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or for init> → <for init>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_INIT)
                            ),
                            new AttrFilter()
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
                            new SetAttrToLeftNode(AttrName.IS_EMPTY_EXPRESSION, NOT_NULL),
                            new AttrFilter(AttrName.IS_EMPTY_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <epsilon or expression> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or for update> → <for update>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FOR_UPDATE)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <for statement no short if> 190
             * SAME
             */
            Production.create(
                    /*
                     * (1) <for statement no short if> → for ( <mark before init> <for init>? ; <mark loop offset> <expression>? ; <mark before update> <for update>? ) <mark after update> <statement no short if>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_STATEMENT_NO_SHORT_IF),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_FOR),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_BEFORE_INIT),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_INIT),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(MARK_LOOP_OFFSET),
                                    Symbol.createNonTerminator(EPSILON_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SEMICOLON),
                                    Symbol.createNonTerminator(MARK_BEFORE_UPDATE),
                                    Symbol.createNonTerminator(EPSILON_OR_FOR_UPDATE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_AFTER_UPDATE),
                                    Symbol.createNonTerminator(STATEMENT_NO_SHORT_IF)
                            ),
                            new MoveUpdateByteCodes(-4, -1),
                            new PushGotoByteCode(-7),
                            new ControlTransferByteCodeBackFill(-6, BackFillType.FALSE),
                            new ExitNamespace(),
                            new AttrFilter()
                    )
            ),


            /*
             * <for init> 192
             * SAME
             */
            Production.create(
                    /*
                     * (1) <for init> → <statement expression list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION_LIST)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * (2) <for init> → <local variable declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FOR_INIT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION)
                            ),
                            new AttrFilter()
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
                            new AttrFilter()
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
                            new RemoveRedundantLoadByteCode(0),
                            new AttrFilter()
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
                            new RemoveRedundantLoadByteCode(0),
                            new AttrFilter()
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
                            new PushReturnByteCode(-1),
                            new AttrFilter()
                    )
            )
    };

}
