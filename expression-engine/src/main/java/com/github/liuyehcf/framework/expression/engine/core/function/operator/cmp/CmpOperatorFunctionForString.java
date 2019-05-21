package com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp;

import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class CmpOperatorFunctionForString extends CmpOperatorFunction {
    @Override
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return bothString(arg1, arg2);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        String value1 = arg1.getValue();
        String value2 = arg2.getValue();

        if (value1 == null && value2 == null) {
            return ExpressionValue.valueOf(0L);
        } else if (value1 == null) {
            return ExpressionValue.valueOf(-1L);
        } else if (value2 == null) {
            return ExpressionValue.valueOf(1L);
        } else {
            return ExpressionValue.valueOf(value1.compareTo(value2));
        }
    }
}
