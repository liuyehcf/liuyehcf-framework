package org.liuyehcf.compile.engine.expression.compile.definition.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.expression.compile.CompilerContext;

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
