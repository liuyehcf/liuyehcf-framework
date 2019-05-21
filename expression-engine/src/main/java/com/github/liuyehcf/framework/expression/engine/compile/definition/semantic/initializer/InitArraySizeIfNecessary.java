package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.initializer;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class InitArraySizeIfNecessary extends AbstractSemanticAction {
    /**
     * 方法名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int arrayItemListStackOffset;

    private final int initArraySize;

    public InitArraySizeIfNecessary(int arrayItemListStackOffset, int initArraySize) {
        this.arrayItemListStackOffset = arrayItemListStackOffset;
        this.initArraySize = initArraySize;
    }

    @Override
    public void onAction(CompilerContext context) {
        Integer initializerSize = context.getAttr(arrayItemListStackOffset, AttrName.ARRAY_SIZE);
        if (initializerSize == null) {
            context.setAttr(arrayItemListStackOffset, AttrName.ARRAY_SIZE, initArraySize);
        }
    }
}
