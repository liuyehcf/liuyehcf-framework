package org.liuyehcf.compile.engine.core.grammar.converter;

import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;

/**
 * 文法转换器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface GrammarConverter {
    /**
     * 返回转换后的文法
     *
     * @return 文法
     */
    Grammar getConvertedGrammar();
}
