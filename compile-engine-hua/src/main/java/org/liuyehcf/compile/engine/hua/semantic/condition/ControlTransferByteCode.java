package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.*;
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
    private final ControlTransferType controlTransferType;


    public ControlTransferByteCode(int backFillStackOffset, ControlTransferType controlTransferType, BackFillType backFillType) {
        super(backFillStackOffset, backFillType);
        this.controlTransferType = controlTransferType;
    }

    @Override
    public void onAction(HuaContext context) {
        ControlTransfer code;
        switch (controlTransferType) {
            case IFEQ:
                code = new _ifeq();
                break;
            case IFNE:
                code = new _ifne();
                break;
            case GOTO:
                code = new _goto();
                break;
            case IF_ICMPNE:
                code = new _if_icmpne();
                break;
            case IF_ICMPEQ:
                code = new _if_icmpeq();
                break;
            case IF_ICMPGE:
                code = new _if_icmpge();
                break;
            case IF_ICMPLE:
                code = new _if_icmple();
                break;
            case IF_ICMPGT:
                code = new _if_icmpgt();
                break;
            case IF_ICMPLT:
                code = new _if_icmplt();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);

        doAddCode(context, code);
    }
}
