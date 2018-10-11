package org.liuyehcf.compile.engine.expression.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.model.BackFillType;
import org.liuyehcf.compile.engine.expression.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.expression.core.bytecode.cf.ControlTransfer;

/**
 * 添加跳转指令
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class PushControlTransferByteCode extends AbstractControlTransferByteCode {

    /**
     * 跳转指令类型
     */
    private final ControlTransferType controlTransferType;


    public PushControlTransferByteCode(int backFillStackOffset, ControlTransferType controlTransferType, BackFillType backFillType) {
        super(backFillStackOffset, backFillType);
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(CompilerContext context) {
        ControlTransfer controlTransfer = ControlTransferType.getControlTransferByType(controlTransferType);

        context.addByteCode(controlTransfer);

        doAddCode(context, controlTransfer);
    }
}
