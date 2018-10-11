package org.liuyehcf.compile.engine.expression.test.function.operator.add;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.test.TestBase;
import org.liuyehcf.compile.engine.expression.utils.EnvBuilder;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestAddOperator extends TestBase {
    @Test
    public void caseLong() {
        Map<String, Object> env = EnvBuilder.builder().put("a", 1L).put("b", 2L).build();

        long result = ExpressionEngine.execute("1+2");
        assertEquals(1L + 2L, result);

        result = ExpressionEngine.execute("a+2", env);
        assertEquals(1L + 2L, result);

        result = ExpressionEngine.execute("1+b", env);
        assertEquals(1L + 2L, result);

        result = ExpressionEngine.execute("a+b", env);
        assertEquals(1L + 2L, result);
    }

    @Test
    public void caseDouble() {
        //double double
        Map<String, Object> env = EnvBuilder.builder().put("a", 1.0f).put("b", 2.0d).build();
        double result = ExpressionEngine.execute("1.0+2.0");
        assertEquals(1.0 + 2.0, result, 1e-10);

        result = ExpressionEngine.execute("a+2.0", env);
        assertEquals(1.0 + 2.0, result, 1e-10);

        result = ExpressionEngine.execute("1.0+b", env);
        assertEquals(1.0 + 2.0, result, 1e-10);

        result = ExpressionEngine.execute("a + b", env);
        assertEquals(1.0 + 2.0, result, 1e-10);


        // double long
        env = EnvBuilder.builder().put("a", 1.0f).put("b", 2L).build();
        result = ExpressionEngine.execute("1.0+2L");
        assertEquals(1.0 + 2L, result, 1e-10);

        result = ExpressionEngine.execute("a+2L", env);
        assertEquals(1.0 + 2L, result, 1e-10);

        result = ExpressionEngine.execute("1.0+b", env);
        assertEquals(1.0 + 2L, result, 1e-10);

        result = ExpressionEngine.execute("a + b", env);
        assertEquals(1.0 + 2L, result, 1e-10);


        // long double
        env = EnvBuilder.builder().put("a", 1L).put("b", 2.0f).build();
        result = ExpressionEngine.execute("1L+2.0f");
        assertEquals(1L + 2.0d, result, 1e-10);

        result = ExpressionEngine.execute("a+2.0d", env);
        assertEquals(1L + 2.0d, result, 1e-10);

        result = ExpressionEngine.execute("1+b", env);
        assertEquals(1L + 2.0d, result, 1e-10);

        result = ExpressionEngine.execute("a + b", env);
        assertEquals(1L + 2.0d, result, 1e-10);
    }

    @Test
    public void caseString() {
        //string string
        Map<String, Object> env = EnvBuilder.builder().put("a", "hello ").put("b", "liuye").build();
        String result = ExpressionEngine.execute("'hello '+'liuye'");
        assertEquals("hello liuye", result);

        result = ExpressionEngine.execute("a+\"liuye\"", env);
        assertEquals("hello liuye", result);

        result = ExpressionEngine.execute("\"hello \"+b", env);
        assertEquals("hello liuye", result);

        result = ExpressionEngine.execute("a + b", env);
        assertEquals("hello liuye", result);


        //string object
        env = EnvBuilder.builder().put("a", "hello ").put("b", Arrays.asList(1, 2, 3)).build();
        result = ExpressionEngine.execute("'hello '+[1,2,3]");
        assertEquals("hello [1, 2, 3]", result);

        result = ExpressionEngine.execute("a+[1,2,3]", env);
        assertEquals("hello [1, 2, 3]", result);

        result = ExpressionEngine.execute("\"hello \"+b", env);
        assertEquals("hello [1, 2, 3]", result);

        result = ExpressionEngine.execute("a + b", env);
        assertEquals("hello [1, 2, 3]", result);


        //null string
        env = EnvBuilder.builder().put("b", "hehe").build();
        result = ExpressionEngine.execute("null +'hehe'");
        assertEquals("nullhehe", result);

        result = ExpressionEngine.execute("a+\"hehe\"", env);
        assertEquals("nullhehe", result);

        result = ExpressionEngine.execute("null+b", env);
        assertEquals("nullhehe", result);

        result = ExpressionEngine.execute("a + b", env);
        assertEquals("nullhehe", result);
    }

    @Test
    public void caseWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true+1L"),
                ExpressionException.class,
                "could not find compatible operator(+) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1+false"),
                ExpressionException.class,
                "could not find compatible operator(+) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false+1.0f"),
                ExpressionException.class,
                "could not find compatible operator(+) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10+true"),
                ExpressionException.class,
                "could not find compatible operator(+) function. type1='java.lang.Double', type2='java.lang.Boolean'");
    }
}
