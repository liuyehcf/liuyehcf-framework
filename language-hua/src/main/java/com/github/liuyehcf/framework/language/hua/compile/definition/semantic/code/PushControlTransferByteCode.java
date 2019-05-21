package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.code;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.BackFillType;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.ControlTransferType;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cf.ControlTransfer;

/**
 * 跳转跳转指令
 *
 * @author hechenfeng
 * @date 2018/6/13
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
        ControlTransfer code = ControlTransferType.getControlTransferByType(controlTransferType);

        context.addByteCodeToCurrentMethod(code);

        doAddCode(context, code);
    }
}
