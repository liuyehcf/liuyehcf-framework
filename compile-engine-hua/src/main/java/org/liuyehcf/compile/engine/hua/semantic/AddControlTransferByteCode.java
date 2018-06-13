package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifne;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class AddControlTransferByteCode extends AbstractSemanticAction {

    public static final int DEFAULT_STACK_OFFSET = 0;

    private final ControlTransferType controlTransferType;

    public AddControlTransferByteCode(ControlTransferType controlTransferType) {
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        switch (controlTransferType) {
            case IFEQ:
                break;
            case IFLT:
                break;
            case IFLE:
                break;
            case IFNE:
                ControlTransfer code = new _ifne();
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);
                context.getStack().get(DEFAULT_STACK_OFFSET).put(AttrName.TRUE_BYTE_CODE.name(), code);
                break;
            case IFGT:
                break;
            case IFGE:
                break;
            default:
                throw new UnsupportedOperationException();
        }
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
