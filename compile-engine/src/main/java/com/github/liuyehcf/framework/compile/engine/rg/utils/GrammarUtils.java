package com.github.liuyehcf.framework.compile.engine.rg.utils;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则文法工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class GrammarUtils {

    private static Symbol DEFAULT_NON_TERMINATOR = Symbol.createNonTerminator("DEFAULT_NON_TERMINATOR");

    public static Grammar createGrammarWithRegex(String regex) {
        return Grammar.create(
                DEFAULT_NON_TERMINATOR,
                Production.create(
                        PrimaryProduction.create(
                                DEFAULT_NON_TERMINATOR,
                                createPrimaryProduction(regex),
                                // todo 正则文法无需语义动作
                                null
                        )
                )
        );
    }

    public static SymbolString createPrimaryProduction(Object... objects) {
        List<Symbol> symbols = new ArrayList<>();
        for (Object obj : objects) {
            if (obj instanceof Character) {
                char c = (char) obj;
                symbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
            } else if (obj instanceof String) {
                for (char c : ((String) obj).toCharArray()) {
                    symbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
                }
            } else if (obj instanceof Symbol) {
                Symbol symbol = (Symbol) obj;
                Assert.assertFalse(symbol.isTerminator());
                symbols.add(symbol);
            }
        }
        return SymbolString.create(
                symbols
        );
    }

    public static List<Symbol> extractSymbolsFromGrammar(Grammar grammar) {
        Assert.assertTrue(grammar.getProductions().size() == 1);

        Production p = grammar.getProductions().get(0);

        Assert.assertTrue(p.getPrimaryProductions().size() == 1);

        return p.getPrimaryProductions().get(0).getRight().getSymbols();
    }
}
