package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.BackFillType;
import org.liuyehcf.compile.engine.hua.model.ControlTransferType;

/**
 * 跳转跳转指令
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public class ControlTransferByteCode extends AbstractControlTransferByteCode {

    /**
     * 跳转指令类型
     */
    final ControlTransferType controlTransferType;


    public ControlTransferByteCode(int backFillStackOffset, ControlTransferType controlTransferType, BackFillType backFillType) {
        super(backFillStackOffset, backFillType);
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(HuaContext context) {
        ControlTransfer code = ControlTransferType.getControlTransferByType(controlTransferType);

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);

        doAddCode(context, code);
    }
}
