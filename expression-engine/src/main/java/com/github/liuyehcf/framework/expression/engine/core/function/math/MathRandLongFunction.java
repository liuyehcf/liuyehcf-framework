package com.github.liuyehcf.framework.expression.engine.core.function.math;

import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

import java.util.Random;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class MathRandLongFunction extends Function {

    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return "math.randomLong";
    }

    @Override
    public ExpressionValue call() {
        return ExpressionValue.valueOf(RANDOM.nextLong());
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        Object value = arg.getValue();

        if (!(value instanceof Long)) {
            throw createTypeIllegalException(1, value);
        }

        return ExpressionValue.valueOf(RANDOM.nextLong() % (long) value);
    }
}
