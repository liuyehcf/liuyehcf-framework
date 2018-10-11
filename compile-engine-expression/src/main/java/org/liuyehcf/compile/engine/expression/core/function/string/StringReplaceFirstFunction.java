package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringReplaceFirstFunction extends Function {
    @Override
    public String getName() {
        return "string.replaceFirst";
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

        return ExpressionValue.valueOf(((String) sequence).replaceFirst((String) regex, (String) replacement));
    }
}