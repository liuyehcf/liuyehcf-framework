package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class InitArgumentSize extends AbstractSemanticAction {
    private static final int ARGUMENT_SIZE_STACK_OFFSET = 0;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getStack().get(InitArgumentSize.ARGUMENT_SIZE_STACK_OFFSET).put(AttrName.ARGUMENT_SIZE.name(), 1);
    }
}
