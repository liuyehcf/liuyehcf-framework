package org.liuyehcf.compile.engine.expression.core.function.math;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class MathPowFunction extends Function {
    @Override
    public String getName() {
        return "math.pow";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object value1 = arg1.getValue();
        Object value2 = arg2.getValue();

        if (!(value1 instanceof Long || value1 instanceof Double)) {
            throw createTypeIllegalException(1, value1);
        }

        if (!(value2 instanceof Long || value2 instanceof Double)) {
            throw createTypeIllegalException(2, value2);
        }

        return ExpressionValue.valueOf(Math.pow(((Number) value1).doubleValue(), ((Number) value2).doubleValue()));
    }
}