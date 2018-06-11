package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class IncreaseArgumentSize extends AbstractSemanticAction {
    public static final int ARGUMENT_SIZE_STACK_OFFSET = -2;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Integer argumentSize = context.getStack().get(IncreaseArgumentSize.ARGUMENT_SIZE_STACK_OFFSET).get(AttrName.ARGUMENT_SIZE.name());
        context.getStack().get(IncreaseArgumentSize.ARGUMENT_SIZE_STACK_OFFSET).put(AttrName.ARGUMENT_SIZE.name(), argumentSize + 1);
    }
}
