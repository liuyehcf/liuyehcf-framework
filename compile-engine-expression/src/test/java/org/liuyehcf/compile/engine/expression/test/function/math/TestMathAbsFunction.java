package org.liuyehcf.compile.engine.expression.test.function.math;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathAbsFunction extends TestBase {
    @Test
    public void case1() {
        long result = ExpressionEngine.execute("math.abs(0)");

        assertEquals(0L, result);
    }

    @Test
    public void case2() {
        long result = ExpressionEngine.execute("math.abs(100)");

        assertEquals(100L, result);
    }

    @Test
    public void case3() {
        long result = ExpressionEngine.execute("math.abs(-1100L)");

        assertEquals(1100L, result);
    }

    @Test
    public void case4() {
        double result = ExpressionEngine.execute("math.abs(100.123f)");

        assertEquals(100.123, result, 1e-10);
    }

    @Test
    public void case5() {
        double result = ExpressionEngine.execute("math.abs(-100.123d)");

        assertEquals(100.123, result, 1e-10);
    }
}
