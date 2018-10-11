package org.liuyehcf.compile.engine.expression.compile.definition.semantic.initializer;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class IncreaseArraySize extends AbstractSemanticAction {
    /**
     * 方法名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int arrayItemListStackOffset;

    public IncreaseArraySize(int arrayItemListStackOffset) {
        this.arrayItemListStackOffset = arrayItemListStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        int argSize = context.getAttr(arrayItemListStackOffset, AttrName.ARRAY_SIZE);
        context.setAttr(arrayItemListStackOffset, AttrName.ARRAY_SIZE, argSize + 1);
    }
}
