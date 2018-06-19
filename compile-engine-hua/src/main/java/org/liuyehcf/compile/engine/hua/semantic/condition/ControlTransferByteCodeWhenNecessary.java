package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;

/**
 * @author chenlu
 * @date 2018/6/19
 */
public class ControlTransferByteCodeWhenNecessary extends ControlTransferByteCodeByType {

    /**
     * 包含 expression是否为空 的语法树节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    public ControlTransferByteCodeWhenNecessary(int backFillStackOffset, int typeStackOffset, BackFillType backFillType, boolean isOpposite, int expressionStackOffset) {
        super(backFillStackOffset, typeStackOffset, backFillType, isOpposite);
        this.expressionStackOffset = expressionStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Object conditionObject = context.getStack().get(expressionStackOffset).get(AttrName.IS_EMPTY_CONDITION_EXPRESSION.name());

        if (conditionObject != null) {
            return;
        }

        super.onAction(context);
    }
}
