package com.github.liuyehcf.framework.expression.engine.core.function.math;

import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;

import java.util.Random;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class MathRandDoubleFunction extends Function {

    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return "math.randomDouble";
    }

    @Override
    public ExpressionValue call() {
        return ExpressionValue.valueOf(RANDOM.nextDouble());
    }
}
