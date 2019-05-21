package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.BackFillType;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.ControlTransferType;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.ControlTransfer;

/**
 * 按类型添加跳转指令
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class PushControlTransferByteCodeByType extends AbstractControlTransferByteCode {

    /**
     * 布尔表达式类型(CONTROL_TRANSFER_TYPE)-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    /**
     * 是否相反
     */
    private final boolean isOpposite;

    public PushControlTransferByteCodeByType(int backFillStackOffset, int typeStackOffset, BackFillType backFillType, boolean isOpposite) {
        super(backFillStackOffset, backFillType);
        this.typeStackOffset = typeStackOffset;
        this.isOpposite = isOpposite;
    }

    @Override
    public void onAction(CompilerContext context) {
        ControlTransferType type = context.getAttr(typeStackOffset, AttrName.CONTROL_TRANSFER_TYPE);

        ControlTransfer controlTransfer;

        if (isOpposite) {
            controlTransfer = ControlTransferType.getOppositeControlTransferByType(type);
        } else {
            controlTransfer = ControlTransferType.getControlTransferByType(type);
        }

        context.addByteCode(controlTransfer);

        doAddCode(context, controlTransfer);
    }
}
