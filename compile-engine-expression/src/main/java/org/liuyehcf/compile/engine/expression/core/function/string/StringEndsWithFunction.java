package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringEndsWithFunction extends Function {
    @Override
    public String getName() {
        return "string.endsWith";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object sequence = arg1.getValue();
        Object suffix = arg2.getValue();

        if (sequence == null || suffix == null) {
            return ExpressionValue.valueOf(false);
        }

        if (!(sequence instanceof String)) {
            throw createTypeIllegalException(1, sequence);
        }

        if (!(suffix instanceof String)) {
            throw createTypeIllegalException(2, suffix);
        }

        return ExpressionValue.valueOf(((String) sequence).endsWith((String) suffix));
    }
}
