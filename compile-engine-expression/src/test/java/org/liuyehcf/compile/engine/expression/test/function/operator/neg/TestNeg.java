package org.liuyehcf.compile.engine.expression.test.function.operator.neg;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestNeg extends TestBase {
    @Test
    public void caseLong() {
        long result = ExpressionEngine.execute("-1");
        assertEquals(-1L, result);

        result = ExpressionEngine.execute("-(-1)");
        assertEquals(1L, result);
    }

    @Test
    public void caseDouble() {
        double result = ExpressionEngine.execute("-1.0f");
        assertEquals(-1.0d, result, 1e-10);

        result = ExpressionEngine.execute("-(-1.0d)");
        assertEquals(1.0d, result, 1e-10);
    }

    @Test
    public void caseWrong() {
        expectedException(
                () -> ExpressionEngine.execute("-true"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("-false"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("-'hello'"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type='java.lang.String'");
    }
}
