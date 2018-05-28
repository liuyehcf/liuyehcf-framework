package org.liuyehcf.compile.engine.core.cfg.lr;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.core.utils.SetUtils;

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
public class LR1 extends AbstractLRCompiler {

    protected LR1(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar, boolean needMerge) {
        super(lexicalAnalyzer, originalGrammar, needMerge);
    }

    public static LRCompiler create(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        LR1 compiler = new LR1(lexicalAnalyzer, originalGrammar, false);

        compiler.init();

        return compiler;
    }

    @Override
    Item createFirstItem() {
        PrimaryProduction ppStart; // origin production

        AssertUtils.assertTrue(getProductionMap().get(Symbol.START).getPrimaryProductions().size() == 2);

        // 第一个子产生式
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
        AssertUtils.assertNotNull(nextSymbol);
        AssertUtils.assertNotNull(nextItem);

        Symbol secondNextSymbol = nextSymbol(nextItem);

        Set<Symbol> lookAHeadsA = item.getLookAHeads();
        AssertUtils.assertFalse(lookAHeadsA.isEmpty());

        Set<Symbol> lookAHeadsB;

        // 此时展望符包含A，"β -*> ε"
        if (secondNextSymbol == null
                || getFirsts().get(secondNextSymbol).contains(Symbol.EPSILON)) {
            // 此时展望符就是 "FIRST(β) + a - ε"
            lookAHeadsB = SetUtils.extract(
                    SetUtils.of(
                            lookAHeadsA,
                            secondNextSymbol == null ? new HashSet<>() : getFirsts().get(secondNextSymbol)
                    )
                    , Symbol.EPSILON
            );
        } else {
            // 此时展望符就是 "FIRST(β)"
            lookAHeadsB = new HashSet<>(getFirsts().get(secondNextSymbol));
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
                            NodeTransferOperation.OperationCode.ACCEPT)
            );
        } else {
            for (Symbol terminator : item.getLookAHeads()) {
                addOperationToAnalysisTable(
                        closure.getId(),
                        terminator,
                        new NodeTransferOperation(
                                -1,
                                ppRaw,
                                NodeTransferOperation.OperationCode.REDUCTION)
                );
            }
        }
    }
}
