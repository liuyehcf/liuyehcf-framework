package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * LR0文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class LR0<T> extends AbstractLRCompiler<T> implements Serializable {

    public LR0(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
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

        return new Item(ppStart, null);
    }

    @Override
    List<Item> findEqualItems(Item item) {

        /*
         * 对于每个 "A → α · B β"
         * 找出所有的 "B → · γ"
         */

        Symbol nextSymbol = nextSymbol(item);
        Assert.assertNotNull(nextSymbol);

        List<Item> result = new ArrayList<>();

        Production p = getProductionMap().get(nextSymbol);

        for (PrimaryProduction pp : p.getPrimaryProductions()) {
            if (pp.getRight().getIndexOfDot() == 0
                    || SymbolString.EPSILON_END.equals(pp.getRight())) {
                result.add(new Item(pp, null));
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
            for (Symbol terminator : getAnalysisTerminators()) {
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
