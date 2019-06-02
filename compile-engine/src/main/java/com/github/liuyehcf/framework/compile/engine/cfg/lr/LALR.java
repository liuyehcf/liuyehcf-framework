package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;

import java.io.Serializable;

/**
 * LALR文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class LALR<T> extends LR1<T> implements Serializable {

    public LALR(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
        super(originalGrammar, lexicalAnalyzer, true);
    }
}
