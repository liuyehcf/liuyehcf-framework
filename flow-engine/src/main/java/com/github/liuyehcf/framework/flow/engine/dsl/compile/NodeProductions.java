package com.github.liuyehcf.framework.flow.engine.dsl.compile;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.ArgumentValueType;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.arg.ExpandArgumentList;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.arg.InitArgumentList;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.attr.AssignAttrsToLeftNode;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.attr.AttrFilter;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.attr.SetArgumentValue;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element.AddAction;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element.AddCondition;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element.AddListener;

import static com.github.liuyehcf.framework.flow.engine.dsl.compile.Constant.*;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
abstract class NodeProductions {

    static final Production[] PRODUCTIONS = {
            /*
             * <action>
             */
            Production.create(
                    /*
                     * <action> →  <action name> ( <epsilon or argument list> )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ACTION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ACTION_NAME),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            new AddAction(-3, -1),
                            new AttrFilter(AttrName.NODE)
                    )
            ),


            /*
             * <condition>
             */
            Production.create(
                    /*
                     * <condition> →  <condition name> ( <epsilon or argument list> )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITION_NAME),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            new AddCondition(-3, -1),
                            new AttrFilter(AttrName.NODE)
                    )
            ),


            /*
             * <listener>
             */
            Production.create(
                    /*
                     * <listener> →  <listener name> ( <epsilon or argument list> )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LISTENER),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LISTENER_NAME),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            new AddListener(-3, -1),
                            new AttrFilter(AttrName.LISTENER)
                    )
            ),


            /*
             * <epsilon or argument list>
             */
            Production.create(
                    /*
                     * <epsilon or argument list> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or argument list> → <argument list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST)
                            ),
                            new AttrFilter(AttrName.ARGUMENT_NAME_LIST, AttrName.ARGUMENT_VALUE_LIST)
                    )
            ),


            /*
             * <argument list>
             */
            Production.create(
                    /*
                     * <argument list> → <argument>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT)
                            ),
                            new InitArgumentList(),
                            new AttrFilter(AttrName.ARGUMENT_NAME_LIST, AttrName.ARGUMENT_VALUE_LIST)
                    ),
                    /*
                     * <argument list> → <argument list> , <argument>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(ARGUMENT)
                            ),
                            new ExpandArgumentList(),
                            new AttrFilter(AttrName.ARGUMENT_NAME_LIST, AttrName.ARGUMENT_VALUE_LIST)
                    )
            ),


            /*
             * <argument>
             */
            Production.create(
                    /*
                     * <argument> → <argument name> = <argument value>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_NAME),
                                    Symbol.createTerminator(NORMAL_ASSIGN),
                                    Symbol.createNonTerminator(ARGUMENT_VALUE)
                            ),
                            new AssignAttrsToLeftNode(0, AttrName.ARGUMENT_VALUE),
                            new AssignAttrsToLeftNode(-2, AttrName.ARGUMENT_NAME),
                            new AttrFilter(AttrName.ARGUMENT_NAME, AttrName.ARGUMENT_VALUE)
                    )
            ),


            /*
             * <argument value>
             */
            Production.create(
                    /*
                     * <argument value> → $ { <place holder name> }
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_VALUE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DOLLAR),
                                    Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(PLACE_HOLDER_NAME),
                                    Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES)
                            ),
                            new SetArgumentValue(-1, AttrName.PLACE_HOLDER_NAME, ArgumentValueType.PLACE_HOLDER),
                            new AttrFilter(AttrName.ARGUMENT_VALUE)
                    ),
                    /*
                     * <argument value> → <literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_VALUE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LITERAL)
                            ),
                            new SetArgumentValue(0, AttrName.LITERAL_VALUE, ArgumentValueType.LITERAL),
                            new AttrFilter(AttrName.ARGUMENT_VALUE)
                    )
            ),
    };
}
