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
public class Expression {
    public static final Production[] PRODUCTIONS = {
            Production.create(
                    /*
                     * <assignment> → <left hand side> <assignment operator> <assignment expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LEFT_HAND_SIDE),
                                    Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
                            )
                            , null
                    )
            ),
            Production.create(
                    /*
                     * <left hand side> → <expression name>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LEFT_HAND_SIDE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),
            Production.create(
                    /*
                     * <assignment operator> → =
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ASSIGN)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),
            Production.create(
                    /*
                     * <assignment expression> → <expression name>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法（这个是最复杂的一部分）
            ),
            Production.create(
                    /*
                     * <expression> → <assignment expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
                            )
                            , null
                    )
            )
    };
}
