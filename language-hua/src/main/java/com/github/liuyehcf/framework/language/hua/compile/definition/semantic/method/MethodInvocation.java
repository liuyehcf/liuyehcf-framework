package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.method;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.BasicMethodInfo;
import com.github.liuyehcf.framework.language.hua.core.MethodSignature;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ir._invokestatic;

import java.util.List;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertNotNull;
import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.core.MethodInfo.buildMethodSignature;

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
    public void onAction(CompilerContext context) {
        String methodName = context.getAttr(methodNameStackOffset, AttrName.METHOD_NAME);
        List<Type> argumentTypeList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_TYPE_LIST);

        MethodSignature methodSignature = buildMethodSignature(methodName, argumentTypeList);

        assertTrue(context.containsMethod(methodSignature), "[SYNTAX_ERROR] - Method' " + methodSignature.getSignature() + " 'undefined");

        BasicMethodInfo basicMethodInfo = context.getBasicMethodByMethodSignature(methodSignature);
        assertNotNull(basicMethodInfo, "[SYNTAX_ERROR] - Cannot find definition of method '" + methodSignature.getSignature() + "'");

        int constantOffset = context.getConstantOffset(methodSignature.getSignature());
        assertTrue(constantOffset >= 0, "[SYSTEM_ERROR] - Constant offset is illegal");
        context.addByteCodeToCurrentMethod(new _invokestatic(constantOffset));
        context.setAttrToLeftNode(AttrName.TYPE, basicMethodInfo.getResultType());
    }
}
