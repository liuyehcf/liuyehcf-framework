package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._ineg;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._ixor;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._lneg;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._lxor;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._lconst;

import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 添加一元运算指令
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class PushUnaryComputeByteCode extends AbstractSemanticAction {
    /**
     * 操作符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int operatorStackOffset;

    /**
     * 右运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int rightStackOffset;

    public PushUnaryComputeByteCode(int operatorStackOffset, int rightStackOffset) {
        this.operatorStackOffset = operatorStackOffset;
        this.rightStackOffset = rightStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type rightType = context.getAttr(rightStackOffset, AttrName.TYPE);
        String operator = context.getValue(operatorStackOffset);

        String typeName = rightType.getTypeName();
        switch (operator) {
            case NORMAL_SUB:
                switch (typeName) {
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ineg());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lneg());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_ADD:
                // do nothing
                break;
            case NORMAL_BIT_REVERSED:
                switch (typeName) {
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _iconst(-1));
                        context.addByteCodeToCurrentMethod(new _ixor());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lconst(-1));
                        context.addByteCodeToCurrentMethod(new _lxor());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_LOGICAL_NOT:
                assertEquals(rightType, Type.TYPE_BOOLEAN);
                /*
                 * 转换跳转指令的类型
                 */
                ControlTransferType controlTransferType = context.getAttr(rightStackOffset, AttrName.CONTROL_TRANSFER_TYPE);
                ControlTransferType oppositeType = controlTransferType.getOppositeType();
                context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, oppositeType);

                /*
                 * 交换TRUE回填字节码与FALSE回填字节码
                 */
                List<ByteCode> trueByteCodes = context.getAttr(rightStackOffset, AttrName.TRUE_BYTE_CODE);
                List<ByteCode> falseByteCodes = context.getAttr(rightStackOffset, AttrName.FALSE_BYTE_CODE);
                context.setAttrToLeftNode(AttrName.TRUE_BYTE_CODE, falseByteCodes);
                context.setAttrToLeftNode(AttrName.FALSE_BYTE_CODE, trueByteCodes);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.setAttrToLeftNode(AttrName.TYPE, rightType);
    }
}
