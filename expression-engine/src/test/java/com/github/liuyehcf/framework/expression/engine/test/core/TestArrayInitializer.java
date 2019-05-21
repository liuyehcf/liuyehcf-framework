package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestArrayInitializer extends TestBase {

    @Test
    public void caseBoolean() {
        Object[] result = ExpressionEngine.execute("[true,false,true]");

        assertEquals(3, result.length);
        assertEquals(true, result[0]);
        assertEquals(false, result[1]);
        assertEquals(true, result[2]);
    }

    @Test
    public void caseLong() {
        Object[] result = ExpressionEngine.execute("[1,2L,0Xf,07]");

        assertEquals(4, result.length);
        assertEquals(1L, result[0]);
        assertEquals(2L, result[1]);
        assertEquals(15L, result[2]);
        assertEquals(7L, result[3]);
    }

    @Test
    public void caseDouble() {
        Object[] result = ExpressionEngine.execute("[1.0,2.1d,3.3f,1.3e-10,10.,.9]");

        assertEquals(6, result.length);
        assertEquals(1.0, (double) result[0], 1e-10);
        assertEquals(2.1d, (double) result[1], 1e-10);
        assertEquals(3.3d, (double) result[2], 1e-10);
        assertEquals(1.3e-10, (double) result[3], 1e-10);
        assertEquals(10., (double) result[4], 1e-10);
        assertEquals(.9, (double) result[5], 1e-10);
    }

    @Test
    public void caseString() {
        Object[] result = ExpressionEngine.execute("['a',\"\0\n\",'\"',\"'\",\"a\",'\t\0','c']");

        assertEquals(7, result.length);
        assertEquals("a", result[0]);
        assertEquals("\0\n", result[1]);
        assertEquals("\"", result[2]);
        assertEquals("'", result[3]);
        assertEquals("a", result[4]);
        assertEquals("\t\0", result[5]);
        assertEquals("c", result[6]);
    }

    @Test
    public void caseMixedLiteral() {
        Object[] result = ExpressionEngine.execute("[1L,1D,true,'hello']");

        assertEquals(4, result.length);
        assertEquals(1L, result[0]);
        assertEquals(1D, result[1]);
        assertEquals(true, result[2]);
        assertEquals("hello", result[3]);
    }

    @Test
    public void caseMixed() {
        Map<String, Object> env = EnvBuilder.builder().put("a", 1L).put("b.c", 100.0D).put("d.e.f", "liuye").build();
        Object[] result = ExpressionEngine.execute("[1L,1D,true,'hello',a,b.c,d.e.f, string.join(['a','b','c'],\"#\"),string.length('a')]", env);

        assertEquals(9, result.length);
        assertEquals(1L, result[0]);
        assertEquals(1D, result[1]);
        assertEquals(true, result[2]);
        assertEquals("hello", result[3]);
        assertEquals(1L, result[4]);
        assertEquals(100D, result[5]);
        assertEquals("liuye", result[6]);
        assertEquals("a#b#c", result[7]);
        assertEquals(1L, result[8]);
    }
}
