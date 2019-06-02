package com.github.liuyehcf.framework.compile.engine.grammar.definition;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;

/**
 * 语义动作
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface SemanticAction<C extends Context> {

    /**
     * 执行语义动作的入口
     *
     * @param context 编译上下文
     */
    void onAction(C context);
}

