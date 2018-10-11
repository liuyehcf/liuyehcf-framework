package org.liuyehcf.compile.engine.expression.core.function.math;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

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
