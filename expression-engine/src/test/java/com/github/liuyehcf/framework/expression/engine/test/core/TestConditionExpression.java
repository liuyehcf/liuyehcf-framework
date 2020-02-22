package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestConditionExpression extends TestBase {
    @Test
    public void caseBooleanLiteral() {
        Object result = ExpressionEngine.execute("true?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("false?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("true?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("false?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("true?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("false?true:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseLess() {
        Object result = ExpressionEngine.execute("1 <3 ?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("1 <-3 ?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("1 <3 ?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("1 <-3 ?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("1 <3 ?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("1 <-3 ?true:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseLessEqual() {
        Object result = ExpressionEngine.execute("3 <=3 ?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("1 <= -100 ?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("3 <=3 ?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("1 <= -100 ?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("3 <=3 ?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("1 <= -100  ?true:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseLarge() {
        Object result = ExpressionEngine.execute("1 > 0 ?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("1 > 1 ?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("1 > 0 ?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("1 > 1 ?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("1 > 0 ?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("1 > 1 ?false:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseLargeEqual() {
        Object result = ExpressionEngine.execute("1 >= 1  ?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("1 >= 2 ?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("1 >= 1  ?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("1 >= 2 ?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("1 >= 1  ?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("1 >= 2 ?false:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseEqual() {
        Object result = ExpressionEngine.execute("1== 1  ?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("1 == 2 ?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("1== 1?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("1 == 2?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("1== 1?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("1 == 2?false:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseNotEqual() {
        Object result = ExpressionEngine.execute("1 != 2  ?true:false");
        assertEquals(true, result);
        result = ExpressionEngine.execute("2 != 2 ?true:false");
        assertEquals(false, result);

        result = ExpressionEngine.execute("1 != 2?1L:2D");
        assertEquals(1L, result);
        result = ExpressionEngine.execute("2 != 2?1L:2D");
        assertEquals(2D, (double) result, 1e-10);

        result = ExpressionEngine.execute("1 != 2?null:'liuye'");
        assertNull(result);
        result = ExpressionEngine.execute("2 != 2?false:'liuye'");
        assertEquals("liuye", result);
    }

    @Test
    public void caseProperty() {
        Map<String, Object> env = EnvBuilder.builder().put("a", true).put("b", "TRUE").put("c", "FALSE").build();
        String result = ExpressionEngine.execute("a?b:c", env);

        assertEquals("TRUE", result);
    }

    @Test
    public void caseNested() {
        Object result = ExpressionEngine.execute("1 != 2  ?(3 != 4? 'TRUE':1l):false");
        assertEquals("TRUE", result);

        result = ExpressionEngine.execute("1 != 2  ?(3 == 4? 1.d:\"FALSE\"):false");
        assertEquals("FALSE", result);

        result = ExpressionEngine.execute("1 == 2  ?(3 == 4? 1.d:\"FALSE\"):false");
        assertEquals(false, result);

        Map<String, Object> env = EnvBuilder.builder().put("a", true).put("b", "True").put("c", "False").build();
        result = ExpressionEngine.execute("1 == 2  ?(3 == 4? 1.d:\"FALSE\"):(a?b:c)", env);
        assertEquals("True", result);

        env = EnvBuilder.builder().put("a", false).put("b", "True").put("c", "False").build();
        result = ExpressionEngine.execute("1 == 2  ?(3 == 4? 1.d:\"FALSE\"):(a?b:c)", env);
        assertEquals("False", result);
    }

    @Test
    public void caseBooleanIncompatible() {
        Object result = ExpressionEngine.execute("1<2?true:1L");
        assertEquals(true, result);

        result = ExpressionEngine.execute("1<2?(3>4): 1L || 3<4");
        assertEquals(false, result);

        result = ExpressionEngine.execute("(1<2?(3>4):1L) || 3<4");
        assertEquals(true, result);

        expectedException(
                () -> ExpressionEngine.execute("(1>=2?(3>4):1L) || 3>4"),
                ExpressionException.class,
                "boolean expression is incompatible with type='java.lang.Long'");


        expectedException(
                () -> ExpressionEngine.execute("(1<2?'hello':1L) || 3<4"),
                ExpressionException.class,
                "boolean expression is incompatible with type='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("(1>=2?(3>4):'hello') || 3>4"),
                ExpressionException.class,
                "boolean expression is incompatible with type='java.lang.String'");
    }
}
