package com.github.liuyehcf.framework.expression.engine.core.function.string;

import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringIndexOfFunction extends Function {
    @Override
    public String getName() {
        return "string.indexOf";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object sequence = arg1.getValue();
        Object segment = arg2.getValue();

        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (!(segment instanceof String)) {
            throw createTypeIllegalException(2, segment);
        }

        return ExpressionValue.valueOf(((String) sequence).indexOf((String) segment));
    }
}

