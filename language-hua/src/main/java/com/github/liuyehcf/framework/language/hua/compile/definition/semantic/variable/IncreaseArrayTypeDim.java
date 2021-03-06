package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.variable;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 增加数组维度
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class IncreaseArrayTypeDim extends AbstractSemanticAction {

    /**
     * 类型-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    public IncreaseArrayTypeDim(int typeStackOffset) {
        this.typeStackOffset = typeStackOffset;
    }


    @Override
    public void onAction(CompilerContext context) {
        Type originType = context.getAttr(typeStackOffset, AttrName.TYPE);
        context.setAttr(typeStackOffset, AttrName.TYPE, originType.toDimIncreasedType());
    }
}
