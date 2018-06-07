package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class Assignment extends AbstractSemanticAction {
    private final int fromStackOffset;

    private final int toStackOffset;

    private final int operatorOffset;

    public Assignment(int fromStackOffset, int toStackOffset, int operatorOffset) {
        this.fromStackOffset = fromStackOffset;
        this.toStackOffset = toStackOffset;
        this.operatorOffset = operatorOffset;
    }

    public int getFromStackOffset() {
        return fromStackOffset;
    }

    public int getToStackOffset() {
        return toStackOffset;
    }

    public int getOperatorOffset() {
        return operatorOffset;
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
