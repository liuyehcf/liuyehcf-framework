package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.iterator;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class PopIteratorNodeAndType extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.popNode();
        context.popLinkType();
    }
}
