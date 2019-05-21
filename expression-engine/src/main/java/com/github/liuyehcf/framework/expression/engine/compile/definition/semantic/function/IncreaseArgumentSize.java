package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.function;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2018/9/25
 */
public class IncreaseArgumentSize extends AbstractSemanticAction {
    /**
     * 方法名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argListStackOffset;

    public IncreaseArgumentSize(int argListStackOffset) {
        this.argListStackOffset = argListStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        int argSize = context.getAttr(argListStackOffset, AttrName.ARGUMENT_SIZE);
        context.setAttr(argListStackOffset, AttrName.ARGUMENT_SIZE, argSize + 1);
    }
}
