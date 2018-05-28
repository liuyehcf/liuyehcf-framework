package org.liuyehcf.compile.engine.core.rg.utils;

import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则文法工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class GrammarUtils {
    private static Symbol DEFAULT_NON_TERMINATOR = Symbol.createNonTerminator("DEFAULT_NON_TERMINATOR");

    private GrammarUtils() {

    }

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
                AssertUtils.assertFalse(symbol.isTerminator());
                symbols.add(symbol);
            }
        }
        return SymbolString.create(
                symbols
        );
    }

    public static List<Symbol> extractSymbolsFromGrammar(Grammar grammar) {
        AssertUtils.assertTrue(grammar.getProductions().size() == 1);

        Production p = grammar.getProductions().get(0);

        AssertUtils.assertTrue(p.getPrimaryProductions().size() == 1);

        return p.getPrimaryProductions().get(0).getRight().getSymbols();
    }
}
