package org.liuyehcf.compile.engine.hua.semantic.variable;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 数组类型降维
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class ArrayTypeDimDecrease extends AbstractSemanticAction {

    /**
     * 表达式名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionNameStackOffset;

    public ArrayTypeDimDecrease(int expressionNameStackOffset) {
        this.expressionNameStackOffset = expressionNameStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Type type = context.getStack().get(expressionNameStackOffset).get(AttrName.TYPE.name());

        if (!type.isArrayType()) {
            throw new RuntimeException("数组维度不足");
        }

        context.getLeftNode().put(AttrName.TYPE.name(), type.toDimDecreasedType());
    }
}
