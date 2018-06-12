package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._invokestatic;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class MethodInvocation extends AbstractSemanticAction {
    private static final int METHOD_NAME_STACK_OFFSET = -3;
    private static final int ARGUMENT_SIZE_STACK_OFFSET = -1;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String methodName = context.getStack().get(MethodInvocation.METHOD_NAME_STACK_OFFSET).get(AttrName.METHOD_NAME.name());
        Integer argumentSize = context.getStack().get(MethodInvocation.ARGUMENT_SIZE_STACK_OFFSET).get(AttrName.ARGUMENT_SIZE.name());

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _invokestatic(methodName, argumentSize));
    }
}
