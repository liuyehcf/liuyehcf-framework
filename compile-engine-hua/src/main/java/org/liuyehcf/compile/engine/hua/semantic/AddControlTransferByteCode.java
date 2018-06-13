package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class AddControlTransferByteCode extends AbstractSemanticAction {

    private final boolean isNeed;

    private final int firstStackOffset;

    private final ControlTransferType controlTransferType;

    public AddControlTransferByteCode(boolean isNeed, int firstStackOffset, ControlTransferType controlTransferType) {
        this.isNeed = isNeed;
        this.firstStackOffset = firstStackOffset;
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        if (!isNeed
                && context.getStack().get(firstStackOffset).get(AttrName.IS_CONDITION_EXPRESSION.name()) == null) {
            return;
        }

        ControlTransfer code;
        switch (controlTransferType) {
            case IFEQ:
                code = new _ifeq();
                break;
            case IFLT:
                code = new _iflt();
                break;
            case IFLE:
                code = new _ifle();
                break;
            case IFNE:
                code = new _ifne();
                break;
            case IFGT:
                code = new _ifgt();
                break;
            case IFGE:
                code = new _ifge();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);
        context.getStack().get(firstStackOffset).put(AttrName.FALSE_BYTE_CODE.name(), code);
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
