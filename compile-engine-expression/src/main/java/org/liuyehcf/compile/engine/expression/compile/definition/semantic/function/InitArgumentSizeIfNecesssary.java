package org.liuyehcf.compile.engine.expression.compile.definition.semantic.function;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2018/9/25
 */
public class InitArgumentSizeIfNecesssary extends AbstractSemanticAction {
    /**
     * 方法名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argListStackOffset;

    private final int initArgSize;

    public InitArgumentSizeIfNecesssary(int argListStackOffset, int initArgSize) {
        this.argListStackOffset = argListStackOffset;
        this.initArgSize = initArgSize;
    }

    @Override
    public void onAction(CompilerContext context) {
        Integer argSize = context.getAttr(argListStackOffset, AttrName.ARGUMENT_SIZE);
        if (argSize == null) {
            context.setAttr(argListStackOffset, AttrName.ARGUMENT_SIZE, initArgSize);
        }
    }
}
