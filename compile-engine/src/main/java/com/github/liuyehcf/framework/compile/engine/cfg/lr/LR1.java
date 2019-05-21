package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.compile.engine.utils.SetUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LR1文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class LR1<T> extends AbstractLRCompiler<T> implements Serializable {

    protected LR1(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer, boolean needMerge) {
        super(originalGrammar, lexicalAnalyzer, needMerge);
    }

    public LR1(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
        super(originalGrammar, lexicalAnalyzer, false);
    }

    @Override
    Item createFirstItem() {
        /*
         * origin production
         */
        PrimaryProduction ppStart;

        Assert.assertTrue(getProductionMap().get(Symbol.START).getPrimaryProductions().size() == 2);

        /*
         * 第一个子产生式
         */
        if (getProductionMap().get(Symbol.START).getPrimaryProductions().get(0)
                .getRight().getIndexOfDot() == 0) {
            ppStart = getProductionMap().get(Symbol.START).getPrimaryProductions().get(0);
        } else {
            ppStart = getProductionMap().get(Symbol.START).getPrimaryProductions().get(1);
        }

        return new Item(ppStart, SetUtils.of(Symbol.DOLLAR));
    }

    @Override
    List<Item> findEqualItems(Item item) {

        /*
         * 对于每个 "[A → α · B β, a]"
         * 找出所有的 "[B → · γ, b]"，其中 "b ∈ FIRST(βa)"
         */

        Symbol nextSymbol = nextSymbol(item);
        Item nextItem = successor(item);
        Assert.assertNotNull(nextSymbol);
        Assert.assertNotNull(nextItem);

        SymbolString beta = nextSymbolString(nextItem);

        Set<Symbol> lookAHeadsA = item.getLookAHeads();
        Assert.assertFalse(lookAHeadsA.isEmpty());

        Set<Symbol> lookAHeadsB;

        /*
         * 此时展望符包含A，"β -*> ε"
         */
        if (beta == null
                || epsilonInvolvedInFirstsOf(beta)) {
            /*
             * 此时展望符就是 "FIRST(β) + a - ε"
             */
            lookAHeadsB = SetUtils.extract(
                    SetUtils.of(
                            lookAHeadsA,
                            beta == null ? new HashSet<>() : getFirstsOf(beta)
                    )
                    , Symbol.EPSILON
            );
        } else {
            /*
             * 此时展望符就是 "FIRST(β)"
             */
            lookAHeadsB = getFirstsOf(beta);
        }

        Production p = getProductionMap().get(nextSymbol);

        List<Item> result = new ArrayList<>();

        for (PrimaryProduction pp : p.getPrimaryProductions()) {
            if (pp.getRight().getIndexOfDot() == 0
                    || SymbolString.EPSILON_END.equals(pp.getRight())) {
                result.add(new Item(pp, lookAHeadsB));
            }
        }

        return result;
    }

    @Override
    void initAnalysisTableWithReduction(Closure closure, Item item) {
        PrimaryProduction pp = item.getPrimaryProduction();
        PrimaryProduction ppRaw = removeDot(pp);

        if ((Symbol.START.equals(pp.getLeft()))) {
            addOperationToAnalysisTable(
                    closure.getId(),
                    Symbol.DOLLAR,
                    new NodeTransferOperation(
                            -1,
                            ppRaw,
                            NodeTransferOperator.ACCEPT)
            );
        } else {
            for (Symbol terminator : item.getLookAHeads()) {
                addOperationToAnalysisTable(
                        closure.getId(),
                        terminator,
                        new NodeTransferOperation(
                                -1,
                                ppRaw,
                                NodeTransferOperator.REDUCTION)
                );
            }
        }
    }
}
