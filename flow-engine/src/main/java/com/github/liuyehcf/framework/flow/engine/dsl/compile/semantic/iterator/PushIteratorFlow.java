package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.iterator;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
public class PushIteratorFlow extends AbstractSemanticAction {

    private boolean isSubFlow;

    public PushIteratorFlow(boolean isSubFlow) {
        this.isSubFlow = isSubFlow;
    }

    @Override
    public void onAction(CompilerContext context) {
        context.pushFlow(isSubFlow);
    }
}
