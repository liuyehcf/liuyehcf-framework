package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

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

    public Operator getOperator() {
        return operator;
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
