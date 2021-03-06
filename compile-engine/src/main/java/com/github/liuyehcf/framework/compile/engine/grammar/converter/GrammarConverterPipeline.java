package com.github.liuyehcf.framework.compile.engine.grammar.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;

/**
 * 文法转换器pipeline
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface GrammarConverterPipeline {

    /**
     * 转换给定文法
     *
     * @param grammar 待转换文法
     * @return 转换后的文法
     */
    Grammar convert(Grammar grammar);
}
