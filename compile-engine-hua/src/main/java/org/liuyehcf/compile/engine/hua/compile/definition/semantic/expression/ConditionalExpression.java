package org.liuyehcf.compile.engine.hua.compile.definition.semantic.expression;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;
import java.util.Objects;

/**
 * 条件语句
 *
 * @author hechenfeng
 * @date 2018/6/23
 */
public class ConditionalExpression extends AbstractSemanticAction implements Serializable {

    /**
     * true执行的表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int trueExpressionStackOffset;

    /**
     * false执行的表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int falseExpressionStackOffset;

    public ConditionalExpression(int trueExpressionStackOffset, int falseExpressionStackOffset) {
        this.trueExpressionStackOffset = trueExpressionStackOffset;
        this.falseExpressionStackOffset = falseExpressionStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type trueExpressionType = context.getAttr(trueExpressionStackOffset, AttrName.TYPE);
        Type falseExpressionType = context.getAttr(falseExpressionStackOffset, AttrName.TYPE);

        if (!Objects.equals(trueExpressionType, falseExpressionType)) {
            throw new RuntimeException("Conditional expressions vary by branch type");
        }

        context.setAttrToLeftNode(AttrName.TYPE, trueExpressionType);
    }
}
