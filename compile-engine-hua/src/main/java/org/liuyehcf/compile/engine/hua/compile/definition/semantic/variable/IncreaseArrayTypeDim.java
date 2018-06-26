package org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 增加数组维度
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class IncreaseArrayTypeDim extends AbstractSemanticAction implements Serializable {

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
    public void onAction(HuaContext context) {
        Type originType = context.getAttr(typeStackOffset, AttrName.TYPE);
        context.setAttr(typeStackOffset, AttrName.TYPE, originType.toDimIncreasedType());
    }
}
