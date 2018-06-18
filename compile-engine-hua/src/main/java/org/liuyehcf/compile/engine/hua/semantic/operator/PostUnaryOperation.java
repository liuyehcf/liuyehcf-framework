package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.model.UnaryOperator;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 后置一元运算
 *
 * @author chenlu
 * @date 2018/6/18
 */
public class PostUnaryOperation extends AbstractSemanticAction {
    /**
     * 运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    /**
     * 操作符
     */
    private final UnaryOperator operator;

    public PostUnaryOperation(int stackOffset, UnaryOperator operator) {
        this.stackOffset = stackOffset;
        this.operator = operator;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        super.onAction(context);
    }
}
