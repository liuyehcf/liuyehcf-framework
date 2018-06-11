package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

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
        switch (operator) {
            case LOGICAL_OR:
                throw new UnsupportedOperationException();
            case LOGICAL_AND:
                throw new UnsupportedOperationException();
            case BIT_OR:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ior());
                break;
            case BIT_XOR:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ixor());
                break;
            case BIT_AND:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iand());
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
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishl());
                break;
            case SHIFT_RIGHT:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishr());
                break;
            case UNSIGNED_SHIFT_RIGHT:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iushr());
                break;
            case ADDITION:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iadd());
                break;
            case SUBTRACTION:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _isub());
                break;
            case MULTIPLICATION:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _imul());
                break;
            case DIVISION:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _idiv());
                break;
            case REMAINDER:
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
