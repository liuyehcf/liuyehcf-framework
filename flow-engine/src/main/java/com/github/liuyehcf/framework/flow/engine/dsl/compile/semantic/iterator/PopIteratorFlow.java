package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.iterator;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/7/7
 */
public class PopIteratorFlow extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.popFlow();
    }
}
