package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.io.Serializable;

/**
 * SLR文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class SLR<T> extends LR0<T> implements Serializable {

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
