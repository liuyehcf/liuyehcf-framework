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
        String methodName = context.getAttr(methodNameStackOffset, AttrName.METHOD_NAME);
        List<Type> argumentTypeList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_TYPE_LIST);

        MethodDescription methodDescription = buildMethodDescription(methodName, argumentTypeList);

        if (!context.containsMethod(methodDescription)) {
            throw new RuntimeException("方法' " + methodDescription.getDescription() + " '尚未定义");
        }

        MethodInfo methodInfo = context.getMethodByMethodDescription(methodDescription);
        assertNotNull(methodInfo);

        context.addByteCodeToCurrentMethod(new _invokestatic(methodDescription.getDescription()));
        context.setAttrToLeftNode(AttrName.TYPE, methodInfo.getResultType());
    }
}
