package org.liuyehcf.compile.engine.core;

import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;

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
