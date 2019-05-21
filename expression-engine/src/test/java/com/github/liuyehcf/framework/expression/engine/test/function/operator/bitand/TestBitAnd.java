package com.github.liuyehcf.framework.expression.engine.test.function.operator.bitand;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestBitAnd extends TestBase {
    @Test
    public void caseLong() {
        long result = ExpressionEngine.execute("2 & 3");

        assertEquals(2L & 3L, result);
    }

    @Test
    public void caseWrong() {
        expectedException(
                () -> ExpressionEngine.execute("1.0&2.1f"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Double', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1.0 & 2L"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Double', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("0&-3e-2"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Long', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true & 1L"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 &false"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false&1.0f"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10&true"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false & 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'& true"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 & 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'& 0L"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. & 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'& .0"),
                ExpressionException.class,
                "could not find compatible operator(&) function. type1='java.lang.String', type2='java.lang.Double'");
    }
}
