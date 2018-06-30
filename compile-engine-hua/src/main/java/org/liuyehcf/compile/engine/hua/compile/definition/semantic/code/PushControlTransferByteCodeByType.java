package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.BackFillType;
import org.liuyehcf.compile.engine.hua.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf._ifne;

import java.io.Serializable;

/**
 * 按类型添加跳转指令
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class PushControlTransferByteCodeByType extends AbstractControlTransferByteCode implements Serializable {

    /**
     * 布尔表达式类型(BOOLEAN_EXPRESSION_TYPE)-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    /**
     * 是否相反
     */
    private final boolean isOpposite;

    public PushControlTransferByteCodeByType(int backFillStackOffset, int typeStackOffset, BackFillType backFillType, boolean isOpposite) {
        super(backFillStackOffset, backFillType);
        this.typeStackOffset = typeStackOffset;
        this.isOpposite = isOpposite;
    }

    @Override
    public void onAction(HuaContext context) {
        ControlTransferType type = context.getAttr(typeStackOffset, AttrName.BOOLEAN_EXPRESSION_TYPE);

        ControlTransfer code;

        if (type == null) {
            /*
             * 对于仅由boolean变量或者字面值构成的布尔表达式，没有BOOLEAN_EXPRESSION属性
             * 该BOOLEAN_EXPRESSION属性是规约 <  <= > >= == != 语句的时候添加的
             * boolean e= a && b || c && d;
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

        context.addByteCodeToCurrentMethod(code);

        doAddCode(context, code);
    }
}
