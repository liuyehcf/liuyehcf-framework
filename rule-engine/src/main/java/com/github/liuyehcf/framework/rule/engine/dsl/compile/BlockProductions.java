package com.github.liuyehcf.framework.rule.engine.dsl.compile;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr.AssignAttrsToLeftNode;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr.AttrFilter;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element.*;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.iterator.*;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.join.AddJoinNode;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.join.PopJoinScope;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.join.PushJoinScope;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;

import static com.github.liuyehcf.framework.rule.engine.dsl.compile.Constant.*;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
abstract class BlockProductions {

    static final Production[] PRODUCTIONS = {
            /*
             * <block>
             */
            Production.create(
                    /*
                     * <block> → { <statements> }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(STATEMENTS),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <statements>
             */
            Production.create(
                    /*
                     * <statements> → <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statements> → <statements> , <statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENTS),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(STATEMENT)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <statement>
             */
            Production.create(
                    /*
                     * <statement> → <action expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ACTION_EXPRESSION)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <if then else statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <select statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SELECT_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <join statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(JOIN_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <join then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(JOIN_THEN_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <sub statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SUB_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <sub then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SUB_THEN_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <statement> → <sub then else statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SUB_THEN_ELSE_STATEMENT)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <action expression>
             */
            Production.create(
                    /*
                     * <action expression> → <action> <epsilon or listeners> <mark action add listener> <epsilon or choose or block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ACTION_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ACTION),
                                    Symbol.createNonTerminator(EPSILON_OR_LISTENERS),
                                    Symbol.createNonTerminator(MARK_ACTION_ADD_LISTENER),
                                    Symbol.createNonTerminator(EPSILON_OR_CHOOSE_OR_BLOCK)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark action add listener>
             */
            Production.create(
                    /*
                     * <mark action add listener> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_ACTION_ADD_LISTENER),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddNodeListeners(0, -1),
                            new AttrFilter()
                    )
            ),


            /*
             * <epsilon or listeners>
             */
            Production.create(
                    /*
                     * <epsilon or listeners> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_LISTENERS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new InitListenerList(),
                            new AttrFilter(AttrName.LISTENER_LIST)
                    ),
                    /*
                     * <epsilon or listeners> → [ <listeners> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_LISTENERS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(LISTENERS),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new AssignAttrsToLeftNode(-1, AttrName.LISTENER_LIST),
                            new AttrFilter(AttrName.LISTENER_LIST)
                    )
            ),


            /*
             * <listeners>
             */
            Production.create(
                    /*
                     * <listeners> → <listener>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LISTENERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LISTENER)
                            ),
                            new InitListenerList(),
                            new ExpandListenerList(0, 0),
                            new AttrFilter(AttrName.LISTENER_LIST)
                    ),
                    /*
                     * <listeners> → <listeners> , <listener>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LISTENERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LISTENERS),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(LISTENER)
                            ),
                            new ExpandListenerList(0, -2),
                            new AttrFilter(AttrName.LISTENER_LIST)
                    )
            ),


            /*
             * <epsilon or choose or block>
             */
            Production.create(
                    /*
                     * <epsilon or choose or block> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_CHOOSE_OR_BLOCK),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or choose or block> → &
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_CHOOSE_OR_BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_AND)
                            ),
                            new AddJoinNode(-3),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or choose or block> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_CHOOSE_OR_BLOCK),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <if then statement>
             */
            Production.create(
                    /*
                     * <if then statement> → if ( <condition expression> )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(CONDITION_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            new PushIteratorLinkType(LinkType.TRUE),// just for pop logic's consistency
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    ),
                    /*
                     * <if then statement> → if ( <condition expression> ) &
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(CONDITION_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_BIT_AND)
                            ),
                            new AddJoinNode(-2),
                            new PushIteratorLinkType(LinkType.TRUE),// just for pop logic's consistency
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    ),
                    /*
                     * <if then statement> → if ( <condition expression> ) <mark link type true> <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(CONDITION_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_LINK_TYPE_TRUE),
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <condition expression>
             */
            Production.create(
                    /*
                     * <condition expression> → <condition> <epsilon or listeners>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITION_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITION),
                                    Symbol.createNonTerminator(EPSILON_OR_LISTENERS)
                            ),
                            new AddNodeListeners(0, -1),
                            new AssignAttrsToLeftNode(-1, AttrName.NODE),
                            new AttrFilter(AttrName.NODE)
                    )
            ),


            /*
             * <mark link type true>
             */
            Production.create(
                    /*
                     * <mark link type true> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_LINK_TYPE_TRUE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushIteratorLinkType(LinkType.TRUE),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark link type false>
             */
            Production.create(
                    /*
                     * <mark link type false> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_LINK_TYPE_FALSE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PopIteratorLinkType(),
                            new PushIteratorLinkType(LinkType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <if then else statement>
             */
            Production.create(
                    /*
                     * <if then else statement> → if ( <condition expression> ) <mark link type true> <block> else <mark link type false> <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_ELSE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_IF),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(CONDITION_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_LINK_TYPE_TRUE),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(MARK_LINK_TYPE_FALSE),
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <select statement>
             */
            Production.create(
                    /*
                     * <select statement> → select { <mark enter select> <if then statements> <mark exit select> } <epsilon or listeners>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SELECT_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SELECT),
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_ENTER_SELECT),
                                    Symbol.createNonTerminator(IF_THEN_STATEMENTS),
                                    Symbol.createNonTerminator(MARK_EXIT_SELECT),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_LISTENERS)
                            ),
                            new AddNodeListeners(0, -5),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark enter select>
             */
            Production.create(
                    /*
                     * <mark enter select> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_ENTER_SELECT),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddExclusiveGateway(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark exit select>
             */
            Production.create(
                    /*
                     * <mark exit select> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_EXIT_SELECT),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <if then statements>
             */
            Production.create(
                    /*
                     * <if then statements> → <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <if then statements> → <if then statements> , <if then statement>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(IF_THEN_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(IF_THEN_STATEMENTS),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(IF_THEN_STATEMENT)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <join statement>
             */
            Production.create(
                    /*
                     * <join statement> → join <join mode> <mark enter join scope> <block> <mark exit join scope> <epsilon or listeners> <mark join add listener>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(JOIN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_JOIN),
                                    Symbol.createNonTerminator(JOIN_MODE),
                                    Symbol.createNonTerminator(MARK_ENTER_JOIN_SCOPE),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(MARK_EXIT_JOIN_SCOPE),
                                    Symbol.createNonTerminator(EPSILON_OR_LISTENERS),
                                    Symbol.createNonTerminator(MARK_JOIN_ADD_LISTENER)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <join then statement>
             */
            Production.create(
                    /*
                     * <join then statement> → join <join mode> <mark enter join scope> <block> <mark exit join scope> <epsilon or listeners> <mark join add listener> then <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(JOIN_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_JOIN),
                                    Symbol.createNonTerminator(JOIN_MODE),
                                    Symbol.createNonTerminator(MARK_ENTER_JOIN_SCOPE),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(MARK_EXIT_JOIN_SCOPE),
                                    Symbol.createNonTerminator(EPSILON_OR_LISTENERS),
                                    Symbol.createNonTerminator(MARK_JOIN_ADD_LISTENER),
                                    Symbol.createTerminator(NORMAL_THEN),
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark enter join scope>
             */
            Production.create(
                    /*
                     * <mark enter join scope> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_ENTER_JOIN_SCOPE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushJoinScope(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark exit join scope>
             */
            Production.create(
                    /*
                     * <mark exit join scope> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_EXIT_JOIN_SCOPE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PopJoinScope(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark join add listener>
             */
            Production.create(
                    /*
                     * <mark join add listener> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_JOIN_ADD_LISTENER),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddNodeListeners(0, -2),
                            new AttrFilter()
                    )
            ),


            /*
             * <sub statement>
             */
            Production.create(
                    /*
                     * <sub statement> → sub <mark enter sub or sub then> <block> <mark add sub rule> <epsilon or global listeners> <mark add global listener> <mark exit sub> <epsilon or choose>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SUB_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SUB),
                                    Symbol.createNonTerminator(MARK_ENTER_SUB_OR_SUB_THEN),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(MARK_ADD_SUB_RULE),
                                    Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS),
                                    Symbol.createNonTerminator(MARK_ADD_GLOBAL_LISTENER),
                                    Symbol.createNonTerminator(MARK_EXIT_SUB),
                                    Symbol.createNonTerminator(EPSILON_OR_CHOOSE)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <sub then statement>
             */
            Production.create(
                    /*
                     * <sub then statement> → sub <mark enter sub or sub then> <block> <mark add sub rule> <epsilon or global listeners> <mark add global listener> <mark exit sub then> then <mark link type true> <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SUB_THEN_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SUB),
                                    Symbol.createNonTerminator(MARK_ENTER_SUB_OR_SUB_THEN),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(MARK_ADD_SUB_RULE),
                                    Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS),
                                    Symbol.createNonTerminator(MARK_ADD_GLOBAL_LISTENER),
                                    Symbol.createNonTerminator(MARK_EXIT_SUB_THEN),
                                    Symbol.createTerminator(NORMAL_THEN),
                                    Symbol.createNonTerminator(MARK_LINK_TYPE_TRUE),
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <sub then else statement>
             */
            Production.create(
                    /*
                     * <sub then else statement> → sub <mark enter sub or sub then> <block> <mark add sub rule> <epsilon or global listeners> <mark add global listener> <mark exit sub then> then <mark link type true> <block> else <mark link type false> <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SUB_THEN_ELSE_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SUB),
                                    Symbol.createNonTerminator(MARK_ENTER_SUB_OR_SUB_THEN),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(MARK_ADD_SUB_RULE),
                                    Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS),
                                    Symbol.createNonTerminator(MARK_ADD_GLOBAL_LISTENER),
                                    Symbol.createNonTerminator(MARK_EXIT_SUB_THEN),
                                    Symbol.createTerminator(NORMAL_THEN),
                                    Symbol.createNonTerminator(MARK_LINK_TYPE_TRUE),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createTerminator(NORMAL_ELSE),
                                    Symbol.createNonTerminator(MARK_LINK_TYPE_FALSE),
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new PopIteratorNodeAndType(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark enter sub or sub then>
             */
            Production.create(
                    /*
                     * <mark enter sub or sub then> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_ENTER_SUB_OR_SUB_THEN),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushIteratorRule(true),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark exit sub>
             */
            Production.create(
                    /*
                     * <mark exit sub> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_EXIT_SUB),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PopIteratorRule(),
                            new PopIteratorNode(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark exit sub then>
             */
            Production.create(
                    /*
                     * <mark exit sub then> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_EXIT_SUB_THEN),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PopIteratorRule(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark add sub rule>
             */
            Production.create(
                    /*
                     * <mark add sub rule> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_ADD_SUB_RULE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddSubRule(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark add global listener>
             */
            Production.create(
                    /*
                     * <mark add global listener> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_ADD_GLOBAL_LISTENER),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddGlobalListeners(),
                            new AttrFilter()
                    )
            ),


            /*
             * <epsilon or choose>
             */
            Production.create(
                    /*
                     * <epsilon or choose> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_CHOOSE),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or choose> → &
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_CHOOSE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_AND)
                            ),
                            new AddJoinNode(-5),
                            new AttrFilter()
                    )
            ),
    };
}
