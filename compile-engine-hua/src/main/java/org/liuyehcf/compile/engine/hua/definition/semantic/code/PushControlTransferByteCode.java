package org.liuyehcf.compile.engine.hua.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.BackFillType;
import org.liuyehcf.compile.engine.hua.definition.model.ControlTransferType;

import java.io.Serializable;

/**
 * 跳转跳转指令
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public class PushControlTransferByteCode extends AbstractControlTransferByteCode implements Serializable {

    /**
     * 跳转指令类型
     */
    private final ControlTransferType controlTransferType;


    public PushControlTransferByteCode(int backFillStackOffset, ControlTransferType controlTransferType, BackFillType backFillType) {
        super(backFillStackOffset, backFillType);
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(HuaContext context) {
        ControlTransfer code = ControlTransferType.getControlTransferByType(controlTransferType);

        context.addByteCodeToCurrentMethod(code);

        doAddCode(context, code);
    }
}
