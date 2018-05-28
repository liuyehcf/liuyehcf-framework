package org.liuyehcf.compile.engine.core.grammar.converter;

import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;

/**
 * 文法转换器抽象基类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class AbstractGrammarConverter implements GrammarConverter {
    /**
     * 待转换的文法
     */
    protected final Grammar originalGrammar;

    /**
     * 转换后的文法
     */
    private Grammar convertedGrammar;

    AbstractGrammarConverter(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    @Override
    public final Grammar getConvertedGrammar() {
        if (convertedGrammar == null) {
            convertedGrammar = doConvert();
        }
        return convertedGrammar;
    }

    /**
     * 文法转换的具体操作，交由子类实现
     *
     * @return 转换后的文法
     */
    protected abstract Grammar doConvert();
}
