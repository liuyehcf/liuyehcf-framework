package org.liuyehcf.compile.engine.hua.compile.definition.semantic.operator;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp.*;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 双目运算
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class BinaryOperation extends AbstractSemanticAction implements Serializable {

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


    public BinaryOperation(int leftStackOffset, int operatorStackOffset, int rightStackOffset) {
        this.leftStackOffset = leftStackOffset;
        this.operatorStackOffset = operatorStackOffset;
        this.rightStackOffset = rightStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type leftType = context.getAttr(leftStackOffset, AttrName.TYPE);
        Type rightType = context.getAttr(rightStackOffset, AttrName.TYPE);
        String operator = context.getValue(operatorStackOffset);

        switch (operator) {
            case NORMAL_BIT_OR:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
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
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
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
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
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
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
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
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
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
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
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
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _iadd());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _ladd());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_SUB:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _isub());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lsub());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_MUL:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _imul());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lmul());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_DIV:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _idiv());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _ldiv());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            case NORMAL_REM:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _irem());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lrem());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.setAttrToLeftNode(AttrName.TYPE, leftType);
    }

    private void checkEqualType(Type type1, Type type2, String operator) {
        if (!type1.equals(type2)) {
            throw new RuntimeException("'" + operator + "' has inconsistent operator subtypes on both sides");
        }
    }
}
