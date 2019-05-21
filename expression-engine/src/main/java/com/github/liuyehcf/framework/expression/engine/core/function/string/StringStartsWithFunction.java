package com.github.liuyehcf.framework.expression.engine.core.function.string;

import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringStartsWithFunction extends Function {
    @Override
    public String getName() {
        return "string.startsWith";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object sequence = arg1.getValue();
        Object prefix = arg2.getValue();

        if (sequence == null || prefix == null) {
            return ExpressionValue.valueOf(false);
        }

        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (!(prefix instanceof String)) {
            throw createTypeIllegalException(2, prefix);
        }

        return ExpressionValue.valueOf(((String) sequence).startsWith((String) prefix));
    }
}
