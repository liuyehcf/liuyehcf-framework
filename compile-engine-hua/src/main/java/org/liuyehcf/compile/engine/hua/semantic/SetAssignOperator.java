package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class SetAssignOperator extends AbstractSemanticAction {

    private final int stackOffset;

    private final Operator operator;

    public SetAssignOperator(int stackOffset, Operator operator) {
        this.stackOffset = stackOffset;
        this.operator = operator;
    }

    public int getStackOffset() {
        return stackOffset;
    }

    public Operator getOperator() {
        return operator;
    }

    public enum Operator {
        NORMAL_ASSIGN("=");

        private final String sign;

        Operator(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }
    }
}
