package org.liuyehcf.compile.engine.hua.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.model.Type;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.hua.definition.model.Type.TYPE_INT;

/**
 * 检查维度表达式
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class CheckExpressionDimType extends AbstractSemanticAction implements Serializable {

    /**
     * 维度表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionDimStackOffset;

    public CheckExpressionDimType(int expressionDimStackOffset) {
        this.expressionDimStackOffset = expressionDimStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type type = context.getAttr(expressionDimStackOffset, AttrName.TYPE);
        if (!TYPE_INT.equals(type)) {
            throw new RuntimeException("数组维度表达式的类型必须是整型");
        }
    }
}
