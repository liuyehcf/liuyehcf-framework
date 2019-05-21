package com.github.liuyehcf.framework.compile.engine.grammar.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;

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
