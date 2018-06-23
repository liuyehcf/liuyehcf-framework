package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.bytecode.ir._invokestatic;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.compiler.MethodDescription;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.hua.compiler.MethodInfo.buildMethodDescription;

/**
 * 方法调用
 *
 * @author hechenfeng
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
    public void onAction(HuaContext context) {
        String methodName = context.getStack().get(methodNameStackOffset).get(AttrName.METHOD_NAME.name());
        List<Type> argumentTypeList = context.getStack().get(argumentListStackOffset).get(AttrName.ARGUMENT_TYPE_LIST.name());

        MethodDescription methodDescription = buildMethodDescription(methodName, argumentTypeList);

        if (!context.getHuaEngine().getMethodInfoTable().containsMethod(methodDescription)) {
            throw new RuntimeException("方法' " + methodDescription.getDescription() + " '尚未定义");
        }

        MethodInfo methodInfo = context.getHuaEngine().getMethodInfoTable().getMethodByMethodDescription(methodDescription);
        assertNotNull(methodInfo);

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _invokestatic(methodDescription.getDescription()));
        context.getLeftNode().put(AttrName.TYPE.name(), methodInfo.getResultType());
    }
}
