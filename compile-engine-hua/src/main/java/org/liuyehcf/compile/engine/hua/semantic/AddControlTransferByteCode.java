package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class AddControlTransferByteCode extends AbstractSemanticAction {

    private final ControlTransferType controlTransferType;

    public AddControlTransferByteCode(ControlTransferType controlTransferType) {
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
    }

    public enum ControlTransferType {
        IFEQ,
        IFLT,
        IFLE,
        IFNE,
        IFGT,
        IFGE
    }
}
