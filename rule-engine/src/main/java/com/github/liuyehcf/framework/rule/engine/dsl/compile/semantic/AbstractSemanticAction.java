package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.SemanticAction;
import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public abstract class AbstractSemanticAction implements SemanticAction<CompilerContext>, Serializable {

    @Override
    public abstract void onAction(CompilerContext context);
}
