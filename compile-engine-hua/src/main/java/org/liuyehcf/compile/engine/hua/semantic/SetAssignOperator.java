package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class SetAssignOperator extends AbstractSemanticAction {

    private final Operator operator;

    public SetAssignOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getStack().get(0).put(AttrName.ASSIGN_OPERATOR.name(), operator);
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
