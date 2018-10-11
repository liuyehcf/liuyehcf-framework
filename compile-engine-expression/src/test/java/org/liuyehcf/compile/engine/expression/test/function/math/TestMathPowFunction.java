package org.liuyehcf.compile.engine.expression.test.function.math;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathPowFunction extends TestBase {
    @Test
    public void caseDouble() {
        double result = ExpressionEngine.execute("math.pow(1.0d,3.3f)");

        assertEquals(Math.pow(1.0d, 3.3f), result, 1e-10);
    }

    @Test
    public void caseLong() {
        double result = ExpressionEngine.execute("math.pow(1,2)");

        assertEquals(Math.pow(1L, 3L), result, 1e-10);
    }

    @Test
    public void caseLongDouble() {
        double result = ExpressionEngine.execute("math.pow(1,2.0d)");

        assertEquals(Math.pow(1L, 2.0d), result, 1e-10);

        result = ExpressionEngine.execute("math.pow(1.2f,3L)");

        assertEquals(Math.pow(1.2d, 3L), result, 1e-10);
    }
}
