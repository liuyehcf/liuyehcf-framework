package org.liuyehcf.compile.engine.hua.compile.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.BasicMethodInfo;
import org.liuyehcf.compile.engine.hua.core.MethodSignature;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._invokestatic;

import java.io.Serializable;
import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.core.MethodInfo.buildMethodSignature;

/**
 * 方法调用
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class MethodInvocation extends AbstractSemanticAction implements Serializable {

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
    public void onAction(CompilerContext context) {
        String methodName = context.getAttr(methodNameStackOffset, AttrName.METHOD_NAME);
        List<Type> argumentTypeList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_TYPE_LIST);

        MethodSignature methodSignature = buildMethodSignature(methodName, argumentTypeList);

        if (!context.containsMethod(methodSignature)) {
            throw new RuntimeException("Method' " + methodSignature.getSignature() + " 'undefined");
        }

        BasicMethodInfo basicMethodInfo = context.getBasicMethodByMethodSignature(methodSignature);
        assertNotNull(basicMethodInfo);

        int constantOffset = context.getConstantOffset(methodSignature.getSignature());
        assertTrue(constantOffset >= 0, "[SYSTEM_ERROR] - Constant offset is illegal");
        context.addByteCodeToCurrentMethod(new _invokestatic(constantOffset));
        context.setAttrToLeftNode(AttrName.TYPE, basicMethodInfo.getResultType());
    }
}
