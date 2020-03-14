package com.github.liuyehcf.framework.compile.engine.grammar.converter;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合并具有相同左部的产生式的文法转换器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class MergeGrammarConverter extends AbstractGrammarConverter implements Serializable {

    public MergeGrammarConverter(Grammar originalGrammar) {
        super(originalGrammar);
    }

    private static Production parallelProduction(Production p1, Production p2) {
        Assert.assertTrue(p1.getLeft().equals(p2.getLeft()));

        List<PrimaryProduction> primaryProductions = new ArrayList<>(p1.getPrimaryProductions());
        primaryProductions.addAll(p2.getPrimaryProductions());

        return Production.create(
                primaryProductions
        );
    }

    @Override
    protected Grammar doConvert() {
        Map<Symbol, Production> productionMap = new HashMap<>(16);

        for (Production p : originalGrammar.getProductions()) {
            Symbol nonTerminator = p.getLeft();
            Assert.assertFalse(nonTerminator.isTerminator());

            /*
             * 合并相同左部的产生式
             */
            if (productionMap.containsKey(nonTerminator)) {
                productionMap.put(
                        nonTerminator,
                        parallelProduction(
                                productionMap.get(nonTerminator),
                                p
                        )
                );
            } else {
                productionMap.put(nonTerminator, p);
            }
        }

        return Grammar.create(
                originalGrammar.getStart(),
                new ArrayList<>(productionMap.values())
        );
    }
}
