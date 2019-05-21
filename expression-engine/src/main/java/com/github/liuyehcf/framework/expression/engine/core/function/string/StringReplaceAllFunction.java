package com.github.liuyehcf.framework.expression.engine.core.function.string;

import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringReplaceAllFunction extends Function {
    @Override
    public String getName() {
        return "string.replaceAll";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3) {
        Object sequence = arg1.getValue();
        Object regex = arg2.getValue();
        Object replacement = arg3.getValue();

        if (sequence == null) {
            return ExpressionValue.valueOf(null);
        }
        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (regex == null || replacement == null) {
            return ExpressionValue.valueOf(sequence);
        }
        if (!(regex instanceof String)) {
            throw createTypeIllegalException(2, regex);
        }
        if (!(replacement instanceof String)) {
            throw createTypeIllegalException(3, replacement);
        }

        return ExpressionValue.valueOf(((String) sequence).replaceAll((String) regex, (String) replacement));
    }
}
