package org.liuyehcf.compile.engine.expression.test.function.operator.sub;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestSub extends TestBase {
    @Test
    public void caseLong() {
        long result = ExpressionEngine.execute("1-2");
        assertEquals(1L - 2, result);
    }

    @Test
    public void caseDouble() {
        double result = ExpressionEngine.execute("1.0d -1");
        assertEquals(1.0d - 1, result, 1e-10);

        result = ExpressionEngine.execute("1.0f -1");
        assertEquals(1.0f - 1, result, 1e-10);
    }

    @Test
    public void caseWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true - 1L"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 -false"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false-1.0f"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10-true"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false - 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'- true"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 - 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'- 0L"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. - 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'- .0"),
                ExpressionException.class,
                "could not find compatible operator(-) function. type1='java.lang.String', type2='java.lang.Double'");
    }
}
