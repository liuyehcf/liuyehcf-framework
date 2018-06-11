package org.liuyehcf.compile.engine.core.grammar.definition;

/**
 * 语义动作
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface SemanticAction<C> {
    void onAction(C context);
}

