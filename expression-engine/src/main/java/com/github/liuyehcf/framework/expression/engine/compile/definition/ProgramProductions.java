package com.github.liuyehcf.framework.expression.engine.compile.definition;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.attr.AttrFilter;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code.PushNewArrayByteCode;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code.PushReturnByteCode;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.initializer.IncreaseArraySize;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.initializer.InitArraySizeIfNecessary;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.statement.BooleanExpressionEnding;

import static com.github.liuyehcf.framework.expression.engine.compile.definition.ExpressionProductions.EXPRESSION;
import static com.github.liuyehcf.framework.expression.engine.compile.definition.GrammarDefinition.*;

/**
 * Program相关的产生式
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
abstract class ProgramProductions {

    /**
     * 非终结符
     */
    public static final String PROGRAMS = "<programs>";
    public static final String ARRAY_INITIALIZER = "<array initializer>"; // 112
    public static final String EPSILON_OR_VARIABLE_INITIALIZERS = "<epsilon or variable initializers>";
    public static final String VARIABLE_INITIALIZERS = "<variable initializers>"; // 114
    public static final String VARIABLE_INITIALIZER = "<variable initializer>"; // 116

    public static final Production[] PRODUCTIONS = {
            /*
             * <programs>
             */
            Production.create(
                    /*
                     * <programs> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            new BooleanExpressionEnding(0),
                            new PushReturnByteCode(),
                            new AttrFilter()
                    )
            ),


            /*
             * <array initializer> 112
             */
            Production.create(
                    /*
                     * <array initializer> → [ <variable initializers>? ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_INITIALIZER),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_VARIABLE_INITIALIZERS),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new InitArraySizeIfNecessary(-1, 0),
                            new PushNewArrayByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <epsilon or variable initializers>
             */
            Production.create(
                    /*
                     * <epsilon or variable initializers> → <variable initializers>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_VARIABLE_INITIALIZERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZERS)
                            ),
                            new AttrFilter(AttrName.ARRAY_SIZE)
                    ),
                    /*
                     * <epsilon or variable initializers> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_VARIABLE_INITIALIZERS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <variable initializers> 114
             */
            Production.create(
                    /*
                     * <variable initializers> → <variable initializers> , <variable initializer>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_INITIALIZERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZERS),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZER)
                            ),
                            new IncreaseArraySize(-2),
                            new AttrFilter(AttrName.ARRAY_SIZE)
                    ),
                    /*
                     * <variable initializers> → <variable initializer>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_INITIALIZERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZER)
                            ),
                            new InitArraySizeIfNecessary(0, 1),
                            new AttrFilter(AttrName.ARRAY_SIZE)
                    )
            ),


            /*
             * <variable initializer> 116
             */
            Production.create(
                    /*
                     * <variable initializer> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_INITIALIZER),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            new BooleanExpressionEnding(0),
                            new AttrFilter()
                    )
            ),
    };
}
