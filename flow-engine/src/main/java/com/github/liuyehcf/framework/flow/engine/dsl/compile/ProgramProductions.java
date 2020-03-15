package com.github.liuyehcf.framework.flow.engine.dsl.compile;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.attr.AssignAttrsToLeftNode;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.attr.AttrFilter;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element.AddGlobalListeners;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element.InitListenerList;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.iterator.PushIteratorFlow;

import static com.github.liuyehcf.framework.flow.engine.dsl.compile.Constant.*;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
abstract class ProgramProductions {

    static final Production[] PRODUCTIONS = {
            /*
             * <programs>
             */
            Production.create(
                    /*
                     * <programs> →  ( <flow name>, <flow id> ) <mark create flow> <block> <epsilon or global listeners>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(FLOW_NAME),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(FLOW_ID),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(MARK_CREATE_FLOW),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS)
                            ),
                            new AddGlobalListeners(),
                            new AttrFilter()
                    ),
                    /*
                     * <programs> →  <mark create flow> <block> <epsilon or global listeners>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MARK_CREATE_FLOW),
                                    Symbol.createNonTerminator(BLOCK),
                                    Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS)
                            ),
                            new AddGlobalListeners(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark create flow>
             */
            Production.create(
                    /*
                     * <mark create flow> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_CREATE_FLOW),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushIteratorFlow(false),
                            new AttrFilter()
                    )
            ),


            /*
             * <epsilon or global listeners>
             */
            Production.create(
                    /*
                     * <epsilon or global listeners> →  ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new InitListenerList(),
                            new AttrFilter(AttrName.LISTENER_LIST)
                    ),
                    /*
                     * <epsilon or global listeners> →  [ <listeners> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_GLOBAL_LISTENERS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(LISTENERS),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new AssignAttrsToLeftNode(-1, AttrName.LISTENER_LIST),
                            new AttrFilter(AttrName.LISTENER_LIST)
                    )
            )
    };
}
