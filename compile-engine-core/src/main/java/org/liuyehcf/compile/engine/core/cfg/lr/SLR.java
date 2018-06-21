package org.liuyehcf.compile.engine.core.cfg.lr;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

/**
 * SLR文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class SLR<T> extends LR0<T> {

    public SLR(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
        super(originalGrammar, lexicalAnalyzer);
    }

    @Override
    protected void initAnalysisTableWithReduction(Closure closure, Item item) {
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
                if (getFollowsOf(ppRaw.getLeft()).contains(terminator)) {
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
}
