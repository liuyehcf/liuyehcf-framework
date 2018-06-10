package org.liuyehcf.compile.engine.core.cfg.lr;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;

/**
 * LALR文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class LALR extends LR1 {
    public LALR(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
        super(originalGrammar, lexicalAnalyzer, true);
    }
}
