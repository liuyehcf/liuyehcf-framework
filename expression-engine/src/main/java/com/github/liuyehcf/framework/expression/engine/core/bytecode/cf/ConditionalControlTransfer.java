package com.github.liuyehcf.framework.expression.engine.core.bytecode.cf;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;

/**
 * 条件跳转指令的基类
 *
 * @author hechenfeng
 * @date 2018/10/2
 */
public abstract class ConditionalControlTransfer extends ControlTransfer {

    ConditionalControlTransfer() {
    }

    ConditionalControlTransfer(int codeOffset) {
        super(codeOffset);
    }

    @Override
    public final void operate(RuntimeContext context) {
        Object value = context.pop().getValue();

        ComparableValue comparableValue;
        if (value instanceof Boolean) {
            comparableValue = ComparableValue.valueOf((boolean) value);
        } else if (value instanceof ComparableValue) {
            comparableValue = (ComparableValue) value;
        } else {
            throw new ExpressionException("boolean expression is incompatible with type='" + (value == null ? null : value.getClass().getName()) + "'");
        }

        if (accept(comparableValue)) {
            context.setCodeOffset(getCodeOffset());
        } else {
            context.increaseCodeOffset();
        }
    }

    abstract boolean accept(ComparableValue comparableValue);
}
