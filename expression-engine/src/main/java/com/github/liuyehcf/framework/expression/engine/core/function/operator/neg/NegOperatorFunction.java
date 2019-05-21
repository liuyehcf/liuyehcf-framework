package com.github.liuyehcf.framework.expression.engine.core.function.operator.neg;

import com.github.liuyehcf.framework.expression.engine.core.function.OperatorFunction;
import com.github.liuyehcf.framework.expression.engine.core.model.OperatorType;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public abstract class NegOperatorFunction extends OperatorFunction {
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE >> 1;
    }

    @Override
    public final OperatorType getType() {
        return OperatorType.NEG;
    }

    @Override
    public final boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return super.accept(arg1, arg2);
    }

    @Override
    public final ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        return super.call(arg1, arg2);
    }
}
