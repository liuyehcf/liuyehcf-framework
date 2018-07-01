package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp.*;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 添加双目运算
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class PushBinaryComputeByteCode extends AbstractSemanticAction implements Serializable {

    /**
     * 左运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftStackOffset;

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


    public PushBinaryComputeByteCode(int leftStackOffset, int operatorStackOffset, int rightStackOffset) {
        this.leftStackOffset = leftStackOffset;
        this.operatorStackOffset = operatorStackOffset;
        this.rightStackOffset = rightStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type leftType = context.getAttr(leftStackOffset, AttrName.TYPE);
        Type rightType = context.getAttr(rightStackOffset, AttrName.TYPE);
        String operator = context.getValue(operatorStackOffset);

        assertTrue(Type.isCompatible(leftType, rightType) || Type.isCompatible(rightType, leftType),
                "Binary operator '" + operator + "' has inconsistent operator subtypes on both sides");

        Type finalType;
        if (Type.isCompatible(leftType, rightType)) {
            finalType = leftType;
        } else {
            finalType = rightType;
        }

        switch (operator) {
            case NORMAL_BIT_OR:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ior());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lor());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_BIT_XOR:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ixor());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lxor());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_BIT_AND:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _iand());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _land());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_SHL:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ishl());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lshl());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_SHR:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ishr());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lshr());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_USHR:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _iushr());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lushr());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_ADD:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _iadd());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _ladd());
                        break;
                    case NORMAL_FLOAT:
                        context.addByteCodeToCurrentMethod(new _fadd());
                        break;
                    case NORMAL_DOUBLE:
                        context.addByteCodeToCurrentMethod(new _dadd());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_SUB:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _isub());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lsub());
                        break;
                    case NORMAL_FLOAT:
                        context.addByteCodeToCurrentMethod(new _fsub());
                        break;
                    case NORMAL_DOUBLE:
                        context.addByteCodeToCurrentMethod(new _dsub());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_MUL:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _imul());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lmul());
                        break;
                    case NORMAL_FLOAT:
                        context.addByteCodeToCurrentMethod(new _fmul());
                        break;
                    case NORMAL_DOUBLE:
                        context.addByteCodeToCurrentMethod(new _dmul());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_DIV:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _idiv());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _ldiv());
                        break;
                    case NORMAL_FLOAT:
                        context.addByteCodeToCurrentMethod(new _fdiv());
                        break;
                    case NORMAL_DOUBLE:
                        context.addByteCodeToCurrentMethod(new _ddiv());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_REM:

                switch (finalType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _irem());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lrem());
                        break;
                    case NORMAL_FLOAT:
                        context.addByteCodeToCurrentMethod(new _frem());
                        break;
                    case NORMAL_DOUBLE:
                        context.addByteCodeToCurrentMethod(new _drem());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.setAttrToLeftNode(AttrName.TYPE, finalType);
    }
}
