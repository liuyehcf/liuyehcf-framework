package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_INT;
import static org.liuyehcf.compile.engine.hua.semantic.BinaryOperator.Operator.*;

/**
 * 双目运算
 *
 * @author chenlu
 * @date 2018/6/7
 */
public class BinaryOperator extends AbstractSemanticAction {

    /**
     * 左运算子的偏移量
     */
    public static final int LEFT_STACK_OFFSET = -2;

    /**
     * 右运算子的偏移量
     */
    public static final int RIGHT_STACK_OFFSET = 0;

    private final Operator operator;

    public BinaryOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String leftType;
        String rightType;

        switch (operator) {
            case LOGICAL_OR:
                throw new UnsupportedOperationException();
            case LOGICAL_AND:
                throw new UnsupportedOperationException();
            case BIT_OR:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + BIT_OR.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ior());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + BIT_OR.getSign() + "\' 运算");
                }

                break;
            case BIT_XOR:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + BIT_XOR.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ixor());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + BIT_XOR.getSign() + "\' 运算");
                }

                break;
            case BIT_AND:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + BIT_AND.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iand());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + BIT_AND.getSign() + "\' 运算");
                }

                break;
            case EQUAL:
                throw new UnsupportedOperationException();
            case NOT_EQUAL:
                throw new UnsupportedOperationException();
            case LESS:
                throw new UnsupportedOperationException();
            case LARGE:
                throw new UnsupportedOperationException();
            case LESS_EQUAL:
                throw new UnsupportedOperationException();
            case LARGE_EQUAL:
                throw new UnsupportedOperationException();
            case SHIFT_LEFT:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!NORMAL_INT.equals(rightType)) {
                    throw new RuntimeException(" \'" + SHIFT_LEFT.getSign() + "\' 运算符右侧必须是整型");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishl());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + SHIFT_LEFT.getSign() + "\' 运算");
                }

                break;
            case SHIFT_RIGHT:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!NORMAL_INT.equals(rightType)) {
                    throw new RuntimeException(" \'" + SHIFT_RIGHT.getSign() + "\' 运算符右侧必须是整型");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishr());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + SHIFT_RIGHT.getSign() + "\' 运算");
                }

                break;
            case UNSIGNED_SHIFT_RIGHT:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!NORMAL_INT.equals(rightType)) {
                    throw new RuntimeException(" \'" + UNSIGNED_SHIFT_RIGHT.getSign() + "\' 运算符右侧必须是整型");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iushr());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + UNSIGNED_SHIFT_RIGHT.getSign() + "\' 运算");
                }

                break;
            case ADDITION:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + ADDITION.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iadd());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + ADDITION.getSign() + "\' 运算");
                }

                break;
            case SUBTRACTION:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + SUBTRACTION.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _isub());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + SUBTRACTION.getSign() + "\' 运算");
                }

                break;
            case MULTIPLICATION:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + MULTIPLICATION.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _imul());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + MULTIPLICATION.getSign() + "\' 运算");
                }

                break;
            case DIVISION:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + DIVISION.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _idiv());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + DIVISION.getSign() + "\' 运算");
                }

                break;
            case REMAINDER:
                leftType = context.getStack().get(LEFT_STACK_OFFSET).get(AttrName.TYPE.name());
                rightType = context.getStack().get(RIGHT_STACK_OFFSET).get(AttrName.TYPE.name());

                if (!leftType.equals(rightType)) {
                    throw new RuntimeException(" \'" + REMAINDER.getSign() + "\' 运算符两侧运算子类型不一致");
                }

                switch (leftType) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _idiv());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + REMAINDER.getSign() + "\' 运算");
                }

                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _irem());
                break;
        }
    }

    public enum Operator {
        LOGICAL_OR("||"),
        LOGICAL_AND("&&"),
        BIT_OR("|"),
        BIT_XOR("^"),
        BIT_AND("&"),
        EQUAL("=="),
        NOT_EQUAL("!="),
        LESS("<"),
        LARGE(">"),
        LESS_EQUAL("<="),
        LARGE_EQUAL(">="),
        SHIFT_LEFT("<<"),
        SHIFT_RIGHT(">>"),
        UNSIGNED_SHIFT_RIGHT(">>>"),
        ADDITION("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        REMAINDER("%"),;

        private final String sign;

        Operator(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }
    }
}
