package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class AddControlTransferByteCode extends AbstractSemanticAction {

    /**
     * 是否必须
     * 对于例如 boolean c=true; 是不需要转移指令的
     */
    private final boolean isNeed;

    /**
     * 待回填字节码的栈偏移量
     */
    private final int firstStackOffset;

    /**
     * 转移字节码类型
     */
    private final ControlTransferType controlTransferType;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    public AddControlTransferByteCode(boolean isNeed, int firstStackOffset, ControlTransferType controlTransferType, BackFillType backFillType) {
        this.isNeed = isNeed;
        this.firstStackOffset = firstStackOffset;
        this.controlTransferType = controlTransferType;
        this.backFillType = backFillType;
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
            case GOTO:
                code = new _goto();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);

        switch (backFillType) {
            case TRUE:
                context.getStack().get(firstStackOffset).put(AttrName.TRUE_BYTE_CODE.name(), code);
                break;
            case FALSE:
                context.getStack().get(firstStackOffset).put(AttrName.FALSE_BYTE_CODE.name(), code);
                break;
            case NEXT:
                context.getStack().get(firstStackOffset).put(AttrName.NEXT_BYTE_CODE.name(), code);
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
        IFGE,
        GOTO
    }

    public enum BackFillType {
        TRUE,
        FALSE,
        NEXT
    }
}
