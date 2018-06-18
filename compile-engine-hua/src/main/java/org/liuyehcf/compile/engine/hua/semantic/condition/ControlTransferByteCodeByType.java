package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifne;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;
import org.liuyehcf.compile.engine.hua.model.ControlTransferType;

/**
 * 按类型添加跳转指令
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class ControlTransferByteCodeByType extends AbstractControlTransferByteCode {

    /**
     * 布尔表达式类型-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    /**
     * 是否相反
     */
    private final boolean isOpposite;

    public ControlTransferByteCodeByType(int backFillStackOffset, int typeStackOffset, BackFillType backFillType, boolean isOpposite) {
        super(backFillStackOffset, backFillType);
        this.typeStackOffset = typeStackOffset;
        this.isOpposite = isOpposite;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransferType type = context.getStack().get(typeStackOffset).get(AttrName.BOOLEAN_EXPRESSION_TYPE.name());

        ControlTransfer code;

        if (type == null) {
            /*
             * 对于仅由boolean变量或者字面值构成的布尔表达式，没有BOOLEAN_EXPRESSION属性
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

        doAddCode(context, code);
    }
}
