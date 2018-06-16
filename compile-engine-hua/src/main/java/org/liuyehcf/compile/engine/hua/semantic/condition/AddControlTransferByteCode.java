package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;
import org.liuyehcf.compile.engine.hua.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加跳转指令
 *
 * @author chenlu
 * @date 2018/6/13
 */
public class AddControlTransferByteCode extends AbstractSemanticAction {

    /**
     * 待回填字节码的栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推 todo 改名字
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

    public AddControlTransferByteCode(int firstStackOffset, ControlTransferType controlTransferType, BackFillType backFillType) {
        this.firstStackOffset = firstStackOffset;
        this.controlTransferType = controlTransferType;
        this.backFillType = backFillType;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
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
                addCode(context, AttrName.TRUE_BYTE_CODE.name(), code);
                break;
            case FALSE:
                addCode(context, AttrName.FALSE_BYTE_CODE.name(), code);
                break;
            case NEXT:
                addCode(context, AttrName.NEXT_BYTE_CODE.name(), code);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void addCode(HuaCompiler.HuaContext context, String attrName, ControlTransfer code) {
        List<ControlTransfer> codes = context.getStack().get(firstStackOffset).get(attrName);

        if (codes == null) {
            codes = new ArrayList<>();
            context.getStack().get(firstStackOffset).put(attrName, codes);
        }

        codes.add(code);
    }
}
