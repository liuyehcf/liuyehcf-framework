package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2021/3/27
 */
public class BindSubFlow extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        // when adding sub flow, each node has not been initialized and cannot execute init method
        // the bind operation can only be performed after the sub flow is parsed (the init operation will be performed on the sub flow)
        context.bindSubFlow();
    }
}
