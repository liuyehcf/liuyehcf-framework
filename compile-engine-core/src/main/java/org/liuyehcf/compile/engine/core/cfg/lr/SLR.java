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
public class SLR extends LR0 {

    protected SLR(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        super(lexicalAnalyzer, originalGrammar);
    }

    public static LRCompiler create(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        SLR compiler = new SLR(lexicalAnalyzer, originalGrammar);

        compiler.init();

        return compiler;
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
                            NodeTransferOperation.OperationCode.ACCEPT)
            );
        } else {
            for (Symbol terminator : getAnalysisTerminators()) {
                if (getFollows().get(ppRaw.getLeft()).contains(terminator)) {
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
}
