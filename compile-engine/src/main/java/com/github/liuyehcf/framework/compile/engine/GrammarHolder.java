package com.github.liuyehcf.framework.compile.engine;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;

/**
 * 文法持有器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface GrammarHolder {

    /**
     * 获取Grammar
     *
     * @return 文法
     **/
    Grammar getGrammar();
}
