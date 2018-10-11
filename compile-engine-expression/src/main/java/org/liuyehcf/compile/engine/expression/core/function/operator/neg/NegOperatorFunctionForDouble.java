package org.liuyehcf.compile.engine.expression.core.function.operator.neg;

import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class NegOperatorFunctionForDouble extends NegOperatorFunction {
    @Override
    public boolean accept(ExpressionValue arg) {
        return isDouble(arg);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        double value = arg.getValue();

        return ExpressionValue.valueOf(-value);
    }
}