package org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.definition.model.Type.TYPE_INT;

/**
 * 检查维度表达式
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class CheckExpressionDimType extends AbstractSemanticAction {

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
    public void onAction(CompilerContext context) {
        Type type = context.getAttr(expressionDimStackOffset, AttrName.TYPE);
        assertTrue(TYPE_INT.equals(type), "[SYNTAX_ERROR] - The array dimension expression type must be an integer");
    }
}
