package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.EXPRESSION_NAME;
import static org.liuyehcf.compile.engine.hua.GrammarDefinition.REGEX_IDENTIFIER;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Token {
    public static final Production[] PRODUCTIONS = {
            Production.create(
                    /*
                     * <expression name> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION_NAME),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            )
                            , null
                    )
                    // TODO 可以扩展更为复杂的语法
            ),
    };
}
