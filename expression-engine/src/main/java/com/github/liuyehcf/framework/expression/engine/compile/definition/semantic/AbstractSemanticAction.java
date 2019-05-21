package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.SemanticAction;
import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;

import java.io.Serializable;

/**
 * 语义动作抽象基类
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public abstract class AbstractSemanticAction implements SemanticAction<CompilerContext>, Serializable {

    public static final String NOT_NULL = "NOT_NULL";

    @Override
    public void onAction(CompilerContext context) {

    }
}
