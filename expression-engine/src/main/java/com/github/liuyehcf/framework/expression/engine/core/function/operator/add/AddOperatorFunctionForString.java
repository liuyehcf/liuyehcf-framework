package com.github.liuyehcf.framework.expression.engine.core.function.operator.add;

import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.utils.ToStringUtils;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class AddOperatorFunctionForString extends AddOperatorFunction {
    @Override
    public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
        return anyString(arg1, arg2);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object value1 = arg1.getValue();
        Object value2 = arg2.getValue();

        return ExpressionValue.valueOf(ToStringUtils.toString(value1) + ToStringUtils.toString(value2));
    }
}
