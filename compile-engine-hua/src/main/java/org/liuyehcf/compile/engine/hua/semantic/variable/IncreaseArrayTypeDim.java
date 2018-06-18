package org.liuyehcf.compile.engine.hua.semantic.variable;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

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
    public void onAction(HuaCompiler.HuaContext context) {
        Type originType = context.getStack().get(typeStackOffset).get(AttrName.TYPE.name());
        context.getStack().get(typeStackOffset).put(AttrName.TYPE.name(), originType.toDimIncreasedType());
    }
}
