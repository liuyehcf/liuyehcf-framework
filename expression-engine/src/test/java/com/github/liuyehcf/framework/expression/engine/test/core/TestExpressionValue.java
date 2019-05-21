package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public class TestExpressionValue extends TestBase {

    @Test
    public void caseAllType() {
        assertTrue(ExpressionValue.valueOf(true).getValue() instanceof Boolean);
        assertTrue(ExpressionValue.valueOf(false).getValue() instanceof Boolean);
        assertTrue(ExpressionValue.valueOf((byte) 1).getValue() instanceof Long);
        assertTrue(ExpressionValue.valueOf((short) 1).getValue() instanceof Long);
        assertTrue(ExpressionValue.valueOf(1).getValue() instanceof Long);
        assertTrue(ExpressionValue.valueOf(1L).getValue() instanceof Long);
        assertTrue(ExpressionValue.valueOf(1.0f).getValue() instanceof Double);
        assertTrue(ExpressionValue.valueOf(1.0d).getValue() instanceof Double);
        assertTrue(ExpressionValue.valueOf('1').getValue() instanceof String);
        assertTrue(ExpressionValue.valueOf("a").getValue() instanceof String);
        assertTrue(ExpressionValue.valueOf(new boolean[0]).getValue() instanceof boolean[]);
        assertTrue(ExpressionValue.valueOf(new byte[0]).getValue() instanceof byte[]);
        assertTrue(ExpressionValue.valueOf(new short[0]).getValue() instanceof short[]);
        assertTrue(ExpressionValue.valueOf(new int[0]).getValue() instanceof int[]);
        assertTrue(ExpressionValue.valueOf(new long[0]).getValue() instanceof long[]);
        assertTrue(ExpressionValue.valueOf(new float[0]).getValue() instanceof float[]);
        assertTrue(ExpressionValue.valueOf(new double[0]).getValue() instanceof double[]);
        assertTrue(ExpressionValue.valueOf(new char[0]).getValue() instanceof char[]);
    }

    @Test
    public void caseExecutionBoolean() {
        Map<String, Object> env = EnvBuilder.builder().put("a", true).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void caseExecutionByte() {
        Map<String, Object> env = EnvBuilder.builder().put("a", (byte) 1).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Long);
    }

    @Test
    public void caseExecutionShort() {
        Map<String, Object> env = EnvBuilder.builder().put("a", (short) 1).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Long);
    }

    @Test
    public void caseExecutionInt() {
        Map<String, Object> env = EnvBuilder.builder().put("a", 1).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Long);
    }

    @Test
    public void caseExecutionLong() {
        Map<String, Object> env = EnvBuilder.builder().put("a", 1L).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Long);
    }

    @Test
    public void caseExecutionFloat() {
        Map<String, Object> env = EnvBuilder.builder().put("a", 1.1f).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Double);
    }

    @Test
    public void caseExecutionDouble() {
        Map<String, Object> env = EnvBuilder.builder().put("a", 1.1d).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof Double);
    }

    @Test
    public void caseExecutionChar() {
        Map<String, Object> env = EnvBuilder.builder().put("a", '1').build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof String);
    }

    @Test
    public void caseExecutionString() {
        Map<String, Object> env = EnvBuilder.builder().put("a", "a").build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof String);
    }

    @Test
    public void caseExecutionArray() {
        Map<String, Object> env = EnvBuilder.builder().put("a", new boolean[0]).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof boolean[]);
    }

    @Test
    public void caseExecutionList() {
        Map<String, Object> env = EnvBuilder.builder().put("a", Lists.newArrayList()).build();
        Object result = ExpressionEngine.execute("a", env);
        assertTrue(result instanceof List);
    }

    @Test
    public void caseExecutionObject() {
        Map<String, Object> env = EnvBuilder.builder().put("a", new Object()).build();
        Object result = ExpressionEngine.execute("a", env);
        assertEquals(Object.class, result.getClass());
    }
}
