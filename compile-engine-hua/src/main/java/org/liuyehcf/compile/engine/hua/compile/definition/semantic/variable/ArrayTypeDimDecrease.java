package org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 数组类型降维
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class ArrayTypeDimDecrease extends AbstractSemanticAction implements Serializable {

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
    public void onAction(HuaContext context) {
        Type type = context.getAttr(expressionNameStackOffset, AttrName.TYPE);

        if (!type.isArrayType()) {
            throw new RuntimeException("Non-array can not be reduced dimension operation\n");
        }

        context.setAttrToLeftNode(AttrName.TYPE, type.toDimDecreasedType());
    }
}
