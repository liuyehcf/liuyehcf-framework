package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.function;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ir._invokestatic;
import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.utils.FunctionUtils;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;

/**
 * 方法调用
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class FunctionInvocation extends AbstractSemanticAction {

    /**
     * 方法名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int functionNameStackOffset;

    /**
     * 参数列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argumentListStackOffset;

    public FunctionInvocation(int functionNameStackOffset, int argumentListStackOffset) {
        this.functionNameStackOffset = functionNameStackOffset;
        this.argumentListStackOffset = argumentListStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        String methodName = context.getAttr(functionNameStackOffset, AttrName.METHOD_NAME);
        int argSize = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_SIZE);

        Function function = FunctionUtils.getFunctionByName(methodName);

        assertNotNull(function, "[SYNTAX_ERROR] - Function '" + methodName + "' undefined");

        context.addByteCode(new _invokestatic(methodName, argSize));
    }
}
