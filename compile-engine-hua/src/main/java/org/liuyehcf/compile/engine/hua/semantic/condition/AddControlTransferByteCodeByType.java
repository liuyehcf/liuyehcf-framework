package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifne;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;
import org.liuyehcf.compile.engine.hua.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/16
 */
public class AddControlTransferByteCodeByType extends AbstractSemanticAction {
    /**
     * 待回填字节码-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 布尔表达式类型-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    /**
     * 是否相反
     */
    private final boolean isOpposite;

    public AddControlTransferByteCodeByType(int backFillStackOffset, int typeStackOffset, BackFillType backFillType, boolean isOpposite) {
        this.backFillStackOffset = backFillStackOffset;
        this.typeStackOffset = typeStackOffset;
        this.backFillType = backFillType;
        this.isOpposite = isOpposite;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransferType type = context.getStack().get(typeStackOffset).get(AttrName.BOOLEAN_EXPRESSION_TYPE.name());

        ControlTransfer code;

        if (type == null) {
            /*
             * 对于单个的boolean变量或者字面值，没有设置过BOOLEAN_EXPRESSION属性
             */
            if (isOpposite) {
                code = new _ifne();
            } else {
                code = new _ifeq();
            }
        } else {
            if (isOpposite) {
                code = ControlTransferType.getOppositeControlTransferByType(type);
            } else {
                code = ControlTransferType.getControlTransferByType(type);
            }
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
        List<ControlTransfer> codes = context.getStack().get(backFillStackOffset).get(attrName);

        if (codes == null) {
            codes = new ArrayList<>();
            context.getStack().get(backFillStackOffset).put(attrName, codes);
        }

        codes.add(code);
    }
}
