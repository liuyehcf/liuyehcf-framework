package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.join;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public class PushJoinScope extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.pushJoinScope();
    }
}
