package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.cp.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;
import static org.liuyehcf.compile.engine.hua.semantic.operator.BinaryOperator.Operator.*;

/**
 * 双目运算
 *
 * @author chenlu
 * @date 2018/6/7
 */
public class BinaryOperator extends AbstractSemanticAction {

    /**
     * 左运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftStackOffset;

    /**
     * 右运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int rightStackOffset;

    /**
     * 操作符
     */
    private final Operator operator;

    public BinaryOperator(int leftStackOffset, int rightStackOffset, Operator operator) {
        this.leftStackOffset = leftStackOffset;
        this.rightStackOffset = rightStackOffset;
        this.operator = operator;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Type leftType = context.getStack().get(leftStackOffset).get(AttrName.TYPE.name());
        Type rightType = context.getStack().get(rightStackOffset).get(AttrName.TYPE.name());

        switch (operator) {
            case BIT_OR:
                checkEqualType(leftType, rightType, BIT_OR);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ior());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + BIT_OR.getSign() + "\' 运算");
                }

                break;
            case BIT_XOR:
                checkEqualType(leftType, rightType, BIT_XOR);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ixor());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + BIT_XOR.getSign() + "\' 运算");
                }

                break;
            case BIT_AND:
                checkEqualType(leftType, rightType, BIT_AND);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iand());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + BIT_AND.getSign() + "\' 运算");
                }

                break;
            case SHIFT_LEFT:
                checkIntegralType(rightType, SHIFT_LEFT);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishl());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + SHIFT_LEFT.getSign() + "\' 运算");
                }

                break;
            case SHIFT_RIGHT:
                checkIntegralType(rightType, SHIFT_RIGHT);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishr());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + SHIFT_RIGHT.getSign() + "\' 运算");
                }

                break;
            case UNSIGNED_SHIFT_RIGHT:
                checkIntegralType(rightType, UNSIGNED_SHIFT_RIGHT);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iushr());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + UNSIGNED_SHIFT_RIGHT.getSign() + "\' 运算");
                }

                break;
            case ADDITION:
                checkEqualType(leftType, rightType, ADDITION);


                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iadd());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + ADDITION.getSign() + "\' 运算");
                }

                break;
            case SUBTRACTION:
                checkEqualType(leftType, rightType, SUBTRACTION);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _isub());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + SUBTRACTION.getSign() + "\' 运算");
                }

                break;
            case MULTIPLICATION:
                checkEqualType(leftType, rightType, MULTIPLICATION);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _imul());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + MULTIPLICATION.getSign() + "\' 运算");
                }

                break;
            case DIVISION:
                checkEqualType(leftType, rightType, DIVISION);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _idiv());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + DIVISION.getSign() + "\' 运算");
                }

                break;
            case REMAINDER:
                checkEqualType(leftType, rightType, REMAINDER);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _irem());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + REMAINDER.getSign() + "\' 运算");
                }

                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void checkEqualType(Type type1, Type type2, Operator operator) {
        if (!type1.equals(type2)) {
            throw new RuntimeException(" \'" + operator.getSign() + "\' 运算符两侧运算子类型不一致");
        }
    }

    private void checkIntegralType(Type type, Operator operator) {
        if (!NORMAL_INT.equals(type.getTypeName())) {
            throw new RuntimeException(" \'" + operator.getSign() + "\' 运算符右侧必须是整型");
        }
    }

    public enum Operator {
        BIT_OR("|"),
        BIT_XOR("^"),
        BIT_AND("&"),
        SHIFT_LEFT("<<"),
        SHIFT_RIGHT(">>"),
        UNSIGNED_SHIFT_RIGHT(">>>"),
        ADDITION("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        REMAINDER("%");

        private final String sign;

        Operator(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }
    }
}
