package com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp;

import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2021/7/16
 */
public class CmpOperatorFunctionForNull extends CmpOperatorFunction {
    @Override
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return arg1.getValue() == null || arg2.getValue() == null;
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        if (arg1.getValue() == null && arg2.getValue() == null) {
            return ExpressionValue.valueOf(0);
        } else if (arg1.getValue() == null) {
            return ExpressionValue.valueOf(-1);
        } else {
            return ExpressionValue.valueOf(1);
        }
    }
}

