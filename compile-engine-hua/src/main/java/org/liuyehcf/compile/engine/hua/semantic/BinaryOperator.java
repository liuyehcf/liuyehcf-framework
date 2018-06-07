package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 双目运算
 *
 * @author chenlu
 * @date 2018/6/7
 */
public class BinaryOperator extends AbstractSemanticAction {

    private final int leftStackOffset;

    private final int rightStackOffset;

    private final Operator operator;

    public BinaryOperator(int leftStackOffset, int rightStackOffset, Operator operator) {
        this.leftStackOffset = leftStackOffset;
        this.rightStackOffset = rightStackOffset;
        this.operator = operator;
    }

    public int getLeftStackOffset() {
        return leftStackOffset;
    }

    public int getRightStackOffset() {
        return rightStackOffset;
    }

    public Operator getOperator() {
        return operator;
    }

    public enum Operator {
        ADDITION("+"),
        SUBTRACTION("-");

        private final String sign;

        Operator(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }
    }
}
