package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.bytecode.ik._invokestatic;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 方法调用
 *
 * @author chenlu
 * @date 2018/6/10
 */
public class MethodInvocation extends AbstractSemanticAction {

    /**
     * 方法名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int methodNameStackOffset;

    /**
     * 参数列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argumentListStackOffset;

    public MethodInvocation(int methodNameStackOffset, int argumentListStackOffset) {
        this.methodNameStackOffset = methodNameStackOffset;
        this.argumentListStackOffset = argumentListStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String methodName = context.getStack().get(methodNameStackOffset).get(AttrName.METHOD_NAME.name());
        Integer argumentSize = context.getStack().get(argumentListStackOffset).get(AttrName.ARGUMENT_SIZE.name());

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _invokestatic(methodName, argumentSize));
    }
}
