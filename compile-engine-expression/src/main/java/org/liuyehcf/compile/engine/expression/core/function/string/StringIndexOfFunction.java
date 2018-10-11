package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

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

