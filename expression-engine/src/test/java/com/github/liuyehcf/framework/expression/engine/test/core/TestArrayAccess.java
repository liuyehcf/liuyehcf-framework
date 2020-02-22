package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestArrayAccess extends TestBase {
    // todo 列表初始化后接数组访问

    @Test
    public void caseList() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", Arrays.asList(true, false)).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] || a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseMap() {
        Map<String, Object> map = EnvBuilder.builder().put("firstName", "liu").put("lastName", "ye").build();
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", map).build();
        String result = ExpressionEngine.execute("a.b.c['firstName'] + a.b.c['lastName']", env);

        assertEquals("liuye", result);
    }

    @Test
    public void caseBooleanArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new boolean[]{true, false}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] && a.b.c[1]", env);

        assertFalse(result);
    }

    @Test
    public void caseByteArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new byte[]{1, 2}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseCharArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new char[]{'a', 'b'}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseShortArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new short[]{1, 2}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseIntArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new int[]{1, 10}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseLongArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new long[]{1, 10}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseFloatArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new float[]{1.0f, 10.0f}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }

    @Test
    public void caseDoubleArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a.b.c", new double[]{1.0d, 10.0d}).build();
        boolean result = ExpressionEngine.execute("a.b.c[0] < a.b.c[1]", env);

        assertTrue(result);
    }
}
