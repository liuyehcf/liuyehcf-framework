package org.liuyehcf.compile.engine.expression.test.function.math;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathSinFunction extends TestBase {
    @Test
    public void case1() {
        double result = ExpressionEngine.execute("math.sin(1.0d)");

        assertEquals(Math.sin(1.0), result, 1e-10);
    }
}