package com.github.liuyehcf.framework.expression.engine.test.function.math;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathLog10Function extends TestBase {
    @Test
    public void case1() {
        double result = ExpressionEngine.execute("math.log10(1.0d)");

        assertEquals(Math.log10(1.0), result, 1e-10);
    }
}
