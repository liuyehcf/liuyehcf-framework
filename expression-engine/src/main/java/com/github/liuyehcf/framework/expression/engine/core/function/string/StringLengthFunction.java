package com.github.liuyehcf.framework.expression.engine.core.function.string;

import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringLengthFunction extends Function {
    @Override
    public String getName() {
        return "string.length";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        Object value = arg.getValue();

        if (value == null) {
            return ExpressionValue.valueOf(0);
        }

        if (!(value instanceof String)) {
            throw createTypeIllegalException(1, value);
        }

        return ExpressionValue.valueOf(((String) value).length());
    }
}
