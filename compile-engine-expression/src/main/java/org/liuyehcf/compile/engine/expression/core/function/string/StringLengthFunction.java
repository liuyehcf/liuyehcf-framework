package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

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
