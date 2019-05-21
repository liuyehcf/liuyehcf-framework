package com.github.liuyehcf.framework.compile.engine.grammar.definition;

/**
 * 语义动作
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface SemanticAction<C> {
    void onAction(C context);
}

