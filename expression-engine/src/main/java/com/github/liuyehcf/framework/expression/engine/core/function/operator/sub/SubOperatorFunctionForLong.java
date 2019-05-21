package com.github.liuyehcf.framework.expression.engine.core.function.operator.sub;

import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class SubOperatorFunctionForLong extends SubOperatorFunction {
    @Override
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return bothLong(arg1, arg2);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        long value1 = arg1.getValue();
        long value2 = arg2.getValue();

        return ExpressionValue.valueOf(value1 - value2);
    }
}
