package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.util.BeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author hechenfeng
 * @date 2019/10/18
 */
@SuppressWarnings("all")
public class TestBeanCloneUtils {

    private static final Random RANDOM = new Random();

    @Test
    public void testBeanBoolean() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.booleanValue1 = true;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                true, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.booleanValue2 = false;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, false, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 3
        origin = new TestBean();
        origin.booleanValue3 = true;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, true, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.booleanValue4 = true;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, true,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanByte() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.byteValue1 = 1;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 1, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.byteValue2 = 2;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, (byte) 2, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 3
        origin = new TestBean();
        origin.byteValue3 = 3;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 3, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.byteValue4 = 4;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 4,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanChar() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.charValue1 = 'a';
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                'a', null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.charValue2 = 'b';
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, 'b', (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");


        // 3
        origin = new TestBean();
        origin.charValue3 = 'c';
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, 'c', (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.charValue4 = 'd';
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, 'd',
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanShort() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.shortValue1 = 1;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 1, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.shortValue2 = 2;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, (short) 2, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");


        // 3
        origin = new TestBean();
        origin.shortValue3 = 3;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 3, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.shortValue4 = 4;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 4,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanInt() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.intValue1 = 1;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                1, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.intValue2 = 2;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, 2, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");


        // 3
        origin = new TestBean();
        origin.intValue3 = 3;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 3, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.intValue4 = 4;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 4,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanLong() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.longValue1 = 1;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                1, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.longValue2 = 2L;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, 2L, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");


        // 3
        origin = new TestBean();
        origin.longValue3 = 3;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 3, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.longValue4 = 4L;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 4L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanFloat() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.floatValue1 = 1f;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                1f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.floatValue2 = 2f;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, 2f, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");


        // 3
        origin = new TestBean();
        origin.floatValue3 = 3f;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 3f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.floatValue4 = 4f;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 4f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testBeanDouble() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.doubleValue1 = 1D;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                1D, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.doubleValue2 = 2D;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, 2D, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");


        // 3
        origin = new TestBean();
        origin.doubleValue3 = 3D;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 3D, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");

        // 4
        origin = new TestBean();
        origin.doubleValue4 = 4D;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0d, 4d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test
    public void testString() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.string = "string";
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                "string",
                null, false, null,
                "string");
    }

    @Test
    public void testBigInteger() {
        BigInteger origin = new BigInteger("123123123123123123123123123123123");
        BigInteger clone = BeanUtils.clone(origin);
        Assert.assertEquals(origin, clone);
    }

    @Test
    public void testBigDecimal() {
        BigDecimal origin = new BigDecimal("123123123123123123123123123123123.123123123123");
        BigDecimal clone = BeanUtils.clone(origin);
        Assert.assertEquals(origin, clone);
    }

    @Test
    public void testNameLength1() {
        TestBean origin;
        TestBean clone;

        // 1
        origin = new TestBean();
        origin.n = "string";
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                "string", false, null,
                "string");

        // 2
        origin = new TestBean();
        origin.b = true;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, true, null,
                "string");

        // 3
        origin = new TestBean();
        origin.B = false;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, false,
                "string");
    }

    @Test
    public void testDefaultNotNull() {
        TestBean origin;
        TestBean clone;

        origin = new TestBean();
        origin.defaultNotNull = null;
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                null);
    }

    @Test
    public void testWrong() {
        TestBean origin;
        TestBean clone;

        origin = new TestBean();
        origin.get = "string";
        origin.noGetMethod = "string";
        origin.getMethodWithParams = "string";
        origin.getMethodWithVoidReturnType1 = "string";
        origin.getMethodWithVoidReturnType2 = "string";
        origin.trueGetWithNoSet = "string";
        origin.trueGetWithNonVoidReturnSetMethod = "string";
        clone = BeanUtils.clone(origin);
        assertTestBean(
                clone,
                false, null, false, false,
                (byte) 0, null, (byte) 0, (byte) 0,
                (char) 0, null, (char) 0, (char) 0,
                (short) 0, null, (short) 0, (short) 0,
                0, null, 0, 0,
                0, null, 0, 0L,
                0f, null, 0f, 0f,
                0, null, 0, 0d,
                null,
                null,
                null,
                null, false, null,
                "string");
    }

    @Test(expected = RuleException.class)
    public void testWithNonNoArgsConstructor() {
        TestBeanWithNonNoArgsConstrcutor origin = new TestBeanWithNonNoArgsConstrcutor(1);

        BeanUtils.clone(origin);
    }

    @Test
    public void testNormalBean() {
        for (int i = 0; i < 1000; i++) {
            NormalBean bean = new NormalBean();

            fillNormalBean(bean);

            NormalBean clone = BeanUtils.clone(bean);
            Assert.assertEquals(bean, clone);
        }
    }

    @Test
    public void testPrimitiveArray() {
        boolean[] booleanArray1 = new boolean[]{true, false};
        Boolean[] booleanArray2 = new Boolean[]{true, false, null};
        boolean[] cloneBooleanArray1 = BeanUtils.clone(booleanArray1);
        Boolean[] cloneBooleanArray2 = BeanUtils.clone(booleanArray2);
        Arrays.equals(booleanArray1, cloneBooleanArray1);
        Arrays.equals(booleanArray2, cloneBooleanArray2);

        byte[] byteArray1 = new byte[]{1, 2};
        Byte[] byteArray2 = new Byte[]{2, 3, null};
        byte[] cloneByteArray1 = BeanUtils.clone(byteArray1);
        Byte[] cloneByteArray2 = BeanUtils.clone(byteArray2);
        Arrays.equals(byteArray1, cloneByteArray1);
        Arrays.equals(byteArray2, cloneByteArray2);

        char[] charArray1 = new char[]{1, 2};
        Character[] charArray2 = new Character[]{2, 3, null};
        char[] cloneCharArray1 = BeanUtils.clone(charArray1);
        Character[] cloneCharArray2 = BeanUtils.clone(charArray2);
        Arrays.equals(charArray1, cloneCharArray1);
        Arrays.equals(charArray2, cloneCharArray2);

        short[] shortArray1 = new short[]{1, 2};
        Short[] shortArray2 = new Short[]{2, 3, null};
        short[] cloneShortArray1 = BeanUtils.clone(shortArray1);
        Short[] cloneShortArray2 = BeanUtils.clone(shortArray2);
        Arrays.equals(shortArray1, cloneShortArray1);
        Arrays.equals(shortArray2, cloneShortArray2);

        int[] intArray1 = new int[]{1, 2};
        Integer[] intArray2 = new Integer[]{2, 3, null};
        int[] cloneIntArray1 = BeanUtils.clone(intArray1);
        Integer[] cloneIntArray2 = BeanUtils.clone(intArray2);
        Arrays.equals(intArray1, cloneIntArray1);
        Arrays.equals(intArray2, cloneIntArray2);

        long[] longArray1 = new long[]{1, 2};
        Long[] longArray2 = new Long[]{2L, 3L, null};
        long[] cloneLongArray1 = BeanUtils.clone(longArray1);
        Long[] cloneLongArray2 = BeanUtils.clone(longArray2);
        Arrays.equals(longArray1, cloneLongArray1);
        Arrays.equals(longArray2, cloneLongArray2);

        float[] floatArray1 = new float[]{1, 2};
        Float[] floatArray2 = new Float[]{2f, 3f, null};
        float[] cloneFloatArray1 = BeanUtils.clone(floatArray1);
        Float[] cloneFloatArray2 = BeanUtils.clone(floatArray2);
        Arrays.equals(floatArray1, cloneFloatArray1);
        Arrays.equals(floatArray2, cloneFloatArray2);

        double[] doubleArray1 = new double[]{1, 2};
        Double[] doubleArray2 = new Double[]{2d, 3d, null};
        double[] cloneDoubleArray1 = BeanUtils.clone(doubleArray1);
        Double[] cloneDoubleArray2 = BeanUtils.clone(doubleArray2);
        Arrays.equals(doubleArray1, cloneDoubleArray1);
        Arrays.equals(doubleArray2, cloneDoubleArray2);
    }

    @Test
    public void testSingleArray() {
        Pair<Object[], Integer> pair = createArray(0);
        Object[] array = pair.getKey();
        int index = pair.getValue();

        Object[] clone = BeanUtils.clone(array);
        Arrays.deepEquals(array, clone);
    }

    @Test
    public void testNestedArray1() {
        Pair<Object[], Integer> innerPair = createArray(0);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> middlePair = createArray(1);
        Object[] middleArray = middlePair.getKey();
        int middleIndex = middlePair.getValue();
        middleArray[middleIndex++] = innerArray;

        Pair<Object[], Integer> outerPair = createArray(1);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();
        outerArray[outerIndex++] = middleArray;

        Object[] clone = BeanUtils.clone(outerArray);
        Arrays.deepEquals(outerArray, clone);
    }

    @Test
    public void testSharedArray1() {
        Pair<Object[], Integer> innerPair = createArray(0);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> middlePair1 = createArray(1);
        Object[] middleArray1 = middlePair1.getKey();
        int middleIndex1 = middlePair1.getValue();
        middleArray1[middleIndex1++] = innerArray;

        Pair<Object[], Integer> middlePair2 = createArray(1);
        Object[] middleArray2 = middlePair2.getKey();
        int middleIndex2 = middlePair2.getValue();
        middleArray2[middleIndex2++] = innerArray;

        Pair<Object[], Integer> outerPair = createArray(2);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();
        outerArray[outerIndex++] = middleArray1;
        outerArray[outerIndex++] = middleArray2;

        Object[] clone = BeanUtils.clone(outerArray);
        Arrays.deepEquals(outerArray, clone);
    }

    @Test
    public void testSharedArray2() {
        Pair<Object[], Integer> innerPair = createArray(0);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> middlePair = createArray(1);
        Object[] middleArray = middlePair.getKey();
        int middleIndex = middlePair.getValue();
        middleArray[middleIndex++] = innerArray;

        Pair<Object[], Integer> outerPair = createArray(1);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();
        outerArray[outerIndex++] = innerArray;

        Object[] clone = BeanUtils.clone(outerArray);
        Arrays.deepEquals(outerArray, clone);
    }

    @Test
    public void testSharedArray3() {
        Pair<Object[], Integer> innerPair = createArray(0);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> middlePair = createArray(1);
        Object[] middleArray = middlePair.getKey();
        int middleIndex = middlePair.getValue();
        middleArray[middleIndex++] = innerArray;

        Pair<Object[], Integer> outerPair = createArray(2);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();
        outerArray[outerIndex++] = innerArray;
        outerArray[outerIndex++] = middleArray;

        Object[] clone = BeanUtils.clone(outerArray);
        Arrays.deepEquals(outerArray, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceArray1() {
        Pair<Object[], Integer> innerPair = createArray(1);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> outerPair = createArray(1);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();
        outerArray[outerIndex++] = innerArray;
        innerArray[innerIndex++] = outerArray;

        BeanUtils.clone(outerArray);
    }

    @Test(expected = RuleException.class)
    public void testReferenceArray2() {
        Pair<Object[], Integer> innerPair = createArray(1);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> middlePair = createArray(1);
        Object[] middleArray = middlePair.getKey();
        int middleIndex = middlePair.getValue();

        Pair<Object[], Integer> outerPair = createArray(1);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();

        outerArray[outerIndex++] = middleArray;
        middleArray[middleIndex++] = innerArray;
        innerArray[innerIndex++] = middleArray;

        BeanUtils.clone(outerArray);
    }

    @Test(expected = RuleException.class)
    public void testReferenceArray3() {
        Pair<Object[], Integer> innerPair = createArray(1);
        Object[] innerArray = innerPair.getKey();
        int innerIndex = innerPair.getValue();

        Pair<Object[], Integer> middlePair = createArray(1);
        Object[] middleArray = middlePair.getKey();
        int middleIndex = middlePair.getValue();

        Pair<Object[], Integer> outerPair = createArray(1);
        Object[] outerArray = outerPair.getKey();
        int outerIndex = outerPair.getValue();

        outerArray[outerIndex++] = middleArray;
        middleArray[middleIndex++] = innerArray;
        innerArray[innerIndex++] = outerArray;

        BeanUtils.clone(outerArray);
    }

    @Test
    public void testSingleMap() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test
    public void testMapWithBean() {
        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        map.put("bean", bean);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test
    public void testNestedMap() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> middleMap = Maps.newHashMap();
        fillMap(middleMap);
        middleMap.put("map", innerMap);

        Map<String, Object> outerMapper = Maps.newHashMap();
        fillMap(outerMapper);
        outerMapper.put("map", middleMap);

        Map<String, Object> clone = BeanUtils.clone(outerMapper);
        Assert.assertEquals(outerMapper, clone);
    }

    @Test
    public void testSharedMap1() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> middleMap1 = Maps.newHashMap();
        fillMap(middleMap1);
        middleMap1.put("map", innerMap);

        Map<String, Object> middleMap2 = Maps.newHashMap();
        fillMap(middleMap2);
        middleMap2.put("map", innerMap);

        Map<String, Object> outerMapper = Maps.newHashMap();
        fillMap(outerMapper);
        outerMapper.put("map1", middleMap1);
        outerMapper.put("map2", middleMap2);

        Map<String, Object> clone = BeanUtils.clone(outerMapper);
        Assert.assertEquals(outerMapper, clone);

        Map<String, Object> clonedMiddleMap1 = (Map<String, Object>) clone.get("map1");
        Map<String, Object> clonedMiddleMap2 = (Map<String, Object>) clone.get("map2");

        Assert.assertTrue(middleMap1.get("map") == middleMap2.get("map"));
        Assert.assertFalse(clonedMiddleMap1.get("map") == clonedMiddleMap2.get("map"));
    }

    @Test
    public void testSharedMap2() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> middleMap = Maps.newHashMap();
        fillMap(middleMap);
        middleMap.put("map", innerMap);

        Map<String, Object> outerMapper = Maps.newHashMap();
        fillMap(outerMapper);
        outerMapper.put("map", innerMap);

        Map<String, Object> clone = BeanUtils.clone(outerMapper);
        Assert.assertEquals(outerMapper, clone);

        Map<String, Object> clonedMiddleMap = (Map<String, Object>) clone.get("map");

        Assert.assertTrue(middleMap.get("map") == outerMapper.get("map"));
        Assert.assertFalse(clonedMiddleMap.get("map") == clone.get("map"));
    }

    @Test
    public void testSharedMap3() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> middleMap = Maps.newHashMap();
        fillMap(middleMap);
        middleMap.put("map", innerMap);

        Map<String, Object> outerMapper = Maps.newHashMap();
        fillMap(outerMapper);
        outerMapper.put("map", innerMap);
        outerMapper.put("map1", middleMap);

        Map<String, Object> clone = BeanUtils.clone(outerMapper);
        Assert.assertEquals(outerMapper, clone);

        Map<String, Object> clonedMiddleMap = (Map<String, Object>) clone.get("map");

        Assert.assertTrue(middleMap.get("map") == outerMapper.get("map"));
        Assert.assertFalse(clonedMiddleMap.get("map") == clone.get("map"));
    }

    @Test(expected = RuleException.class)
    public void testReferenceMap1() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> outerMap = Maps.newHashMap();
        fillMap(outerMap);

        outerMap.put("map", innerMap);
        innerMap.put("map", outerMap);

        BeanUtils.clone(outerMap);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMap2() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> middleMap = Maps.newHashMap();
        fillMap(middleMap);

        Map<String, Object> outerMap = Maps.newHashMap();
        fillMap(outerMap);

        outerMap.put("map", middleMap);
        middleMap.put("map", innerMap);
        innerMap.put("map", middleMap);

        BeanUtils.clone(outerMap);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMap3() {
        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        Map<String, Object> middleMap = Maps.newHashMap();
        fillMap(middleMap);

        Map<String, Object> outerMap = Maps.newHashMap();
        fillMap(outerMap);

        outerMap.put("map", middleMap);
        middleMap.put("map", innerMap);
        innerMap.put("map", outerMap);

        BeanUtils.clone(outerMap);
    }

    @Test
    public void testSingleCollection() {
        List<Object> list = Lists.newArrayList();
        fillList(list);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test
    public void testNestedCollection1() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> middleList = Lists.newArrayList();
        fillList(middleList);
        middleList.add(innterList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);
        outerList.add(middleList);

        List<Object> clone = BeanUtils.clone(outerList);
        Assert.assertEquals(outerList, clone);
    }

    @Test
    public void testSharedCollection1() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> middleList1 = Lists.newArrayList();
        fillList(middleList1);
        middleList1.add(innterList);

        List<Object> middleList2 = Lists.newArrayList();
        fillList(middleList2);
        middleList2.add(innterList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);
        outerList.add(middleList1);
        outerList.add(middleList2);

        List<Object> clone = BeanUtils.clone(outerList);
        Assert.assertEquals(outerList, clone);
    }

    @Test
    public void testSharedCollection2() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> middleList = Lists.newArrayList();
        fillList(middleList);
        middleList.add(innterList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);
        outerList.add(innterList);

        List<Object> clone = BeanUtils.clone(outerList);
        Assert.assertEquals(outerList, clone);
    }

    @Test
    public void testSharedCollection3() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> middleList = Lists.newArrayList();
        fillList(middleList);
        middleList.add(innterList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);
        outerList.add(innterList);
        outerList.add(middleList);

        List<Object> clone = BeanUtils.clone(outerList);
        Assert.assertEquals(outerList, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceCollection1() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);
        outerList.add(innterList);
        innterList.add(outerList);

        BeanUtils.clone(outerList);
    }

    @Test(expected = RuleException.class)
    public void testReferenceCollection2() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> middleList = Lists.newArrayList();
        fillList(middleList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);

        outerList.add(middleList);
        middleList.add(innterList);
        innterList.add(middleList);

        BeanUtils.clone(outerList);
    }

    @Test(expected = RuleException.class)
    public void testReferenceCollection3() {
        List<Object> innterList = Lists.newArrayList();
        fillList(innterList);

        List<Object> middleList = Lists.newArrayList();
        fillList(middleList);

        List<Object> outerList = Lists.newArrayList();
        fillList(outerList);

        outerList.add(middleList);
        middleList.add(innterList);
        innterList.add(outerList);

        BeanUtils.clone(outerList);
    }

    @Test
    public void testSharedMapCollectionBean1() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();

        bean.setMap(map);
        bean.setCollection(list);

        map.put("list", list);

        NormalBean clone = BeanUtils.clone(bean);
        Assert.assertEquals(bean, clone);
    }

    @Test
    public void testSharedMapCollectionBean2() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        NormalBean innerBean = new NormalBean();
        fillNormalBean(innerBean);

        bean.setMap(map);
        bean.setCollection(list);

        map.put("bean", innerBean);
        list.add(innerBean);

        NormalBean clone = BeanUtils.clone(bean);
        Assert.assertEquals(bean, clone);
    }

    @Test
    public void testSharedMapCollectionBean3() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        list.add(bean);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test
    public void testSharedMapCollectionBean4() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        bean.setMap(innerMap);
        list.add(innerMap);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test
    public void testSharedMapCollectionBean5() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("bean", bean);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test
    public void testSharedMapCollectionBean6() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        List<Object> innerList = Lists.newArrayList();
        fillList(innerList);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("list", innerList);
        bean.setCollection(innerList);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean1() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();

        bean.setMap(map);
        bean.setCollection(list);

        map.put("list", list);
        list.add(map);

        BeanUtils.clone(bean);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean2() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();

        bean.setMap(map);
        bean.setCollection(list);

        map.put("bean", bean);

        BeanUtils.clone(bean);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean3() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();

        bean.setMap(map);
        bean.setCollection(list);

        list.add(bean);

        BeanUtils.clone(bean);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean4() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        NormalBean innerBean = new NormalBean();
        fillNormalBean(innerBean);

        bean.setMap(map);
        bean.setCollection(list);

        map.put("bean", innerBean);
        list.add(innerBean);

        innerBean.setMap(map);

        NormalBean clone = BeanUtils.clone(bean);
        Assert.assertEquals(bean, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean5() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        NormalBean innerBean = new NormalBean();
        fillNormalBean(innerBean);

        bean.setMap(map);
        bean.setCollection(list);

        map.put("bean", innerBean);
        list.add(innerBean);

        innerBean.setCollection(list);

        NormalBean clone = BeanUtils.clone(bean);
        Assert.assertEquals(bean, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean6() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        NormalBean innerBean = new NormalBean();
        fillNormalBean(innerBean);

        bean.setMap(map);
        bean.setCollection(list);

        map.put("bean", innerBean);
        list.add(innerBean);

        innerBean.setBean(bean);

        NormalBean clone = BeanUtils.clone(bean);
        Assert.assertEquals(bean, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean7() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        list.add(bean);
        bean.setCollection(list);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean8() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        list.add(bean);
        bean.setMap(map);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean9() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        list.add(bean);
        list.add(map);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean10() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        bean.setMap(innerMap);
        list.add(innerMap);
        innerMap.put("bean", bean);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean11() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        bean.setMap(innerMap);
        list.add(innerMap);
        innerMap.put("list", list);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean12() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        Map<String, Object> innerMap = Maps.newHashMap();
        fillMap(innerMap);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        map.put("bean", bean);
        map.put("list", list);
        bean.setMap(innerMap);
        list.add(innerMap);
        innerMap.put("map", map);

        Map<String, Object> clone = BeanUtils.clone(map);
        Assert.assertEquals(map, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean13() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("bean", bean);
        bean.setMap(map);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean14() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("bean", bean);
        bean.setCollection(list);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean15() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("bean", bean);
        map.put("list", list);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean16() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        List<Object> innerList = Lists.newArrayList();
        fillList(innerList);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("list", innerList);
        bean.setCollection(innerList);

        innerList.add(map);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean17() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        List<Object> innerList = Lists.newArrayList();
        fillList(innerList);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("list", innerList);
        bean.setCollection(innerList);

        innerList.add(bean);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test(expected = RuleException.class)
    public void testReferenceMapCollectionBean18() {
        Map<String, Object> map = Maps.newHashMap();
        fillMap(map);

        List<Object> list = Lists.newArrayList();
        fillList(list);

        List<Object> innerList = Lists.newArrayList();
        fillList(innerList);

        NormalBean bean = new NormalBean();
        fillNormalBean(bean);

        list.add(map);
        list.add(bean);
        map.put("list", innerList);
        bean.setCollection(innerList);

        innerList.add(list);

        List<Object> clone = BeanUtils.clone(list);
        Assert.assertEquals(list, clone);
    }

    @Test
    public void testGenericBean() {
        GenericBean<String> innerBean = new GenericBean<>();
        innerBean.setName("inner");
        innerBean.setData("data");

        GenericBean<GenericBean<String>> middleBean = new GenericBean<>();
        middleBean.setName("middle");
        middleBean.setData(innerBean);

        GenericBean<GenericBean<GenericBean<String>>> outerBean = new GenericBean<>();
        outerBean.setName("outer");
        outerBean.setData(middleBean);

        Assert.assertEquals(outerBean, BeanUtils.clone(outerBean));
        Assert.assertEquals(middleBean, BeanUtils.clone(middleBean));
        Assert.assertEquals(innerBean, BeanUtils.clone(innerBean));
    }

    private Pair<Object[], Integer> createArray(int extraNum) {
        Object[] array = new Object[14 + extraNum];
        array[0] = RANDOM.nextBoolean();
        array[1] = (byte) RANDOM.nextInt();
        array[2] = (char) RANDOM.nextInt();
        array[3] = (short) RANDOM.nextInt();
        array[4] = RANDOM.nextInt();
        array[5] = RANDOM.nextLong();
        array[6] = RANDOM.nextFloat();
        array[7] = RANDOM.nextDouble();
        array[8] = UUID.randomUUID().toString();
        array[9] = getRandomBigInteger();
        array[10] = getRandomBigDecimal();
        array[11] = new Date();
        array[12] = getRandomEnum();
        array[13] = null;

        return new ImmutablePair<>(array, 14);
    }

    private void fillMap(Map<String, Object> map) {
        map.put("bean", RANDOM.nextBoolean());
        map.put("byte", (byte) RANDOM.nextInt());
        map.put("char", (char) RANDOM.nextInt());
        map.put("short", (short) RANDOM.nextInt());
        map.put("int", RANDOM.nextInt());
        map.put("long", RANDOM.nextLong());
        map.put("float", RANDOM.nextFloat());
        map.put("double", RANDOM.nextDouble());
        map.put("string", UUID.randomUUID().toString());
        map.put("bigInteger", getRandomBigInteger());
        map.put("bigDecimal", getRandomBigDecimal());
        map.put("date", new Date());
        map.put(null, "null");
        map.put("null", null);
        map.put("enum", getRandomEnum());
    }

    private void fillList(List<Object> list) {
        list.add(RANDOM.nextBoolean());
        list.add((byte) RANDOM.nextInt());
        list.add((char) RANDOM.nextInt());
        list.add((short) RANDOM.nextInt());
        list.add(RANDOM.nextInt());
        list.add(RANDOM.nextLong());
        list.add(RANDOM.nextFloat());
        list.add(RANDOM.nextDouble());
        list.add(UUID.randomUUID().toString());
        list.add(getRandomBigInteger());
        list.add(getRandomBigDecimal());
        list.add(new Date());
        list.add(null);
        list.add(getRandomEnum());
    }

    private void fillNormalBean(NormalBean bean) {
        if (RANDOM.nextBoolean()) {
            bean.setBooleanValue1(RANDOM.nextBoolean());
        }
        if (RANDOM.nextBoolean()) {
            bean.setBooleanValue2(RANDOM.nextBoolean());
        }

        if (RANDOM.nextBoolean()) {
            bean.setByteValue1((byte) RANDOM.nextInt());
        }
        if (RANDOM.nextBoolean()) {
            bean.setByteValue2((byte) RANDOM.nextInt());
        }

        if (RANDOM.nextBoolean()) {
            bean.setCharValue1((char) RANDOM.nextInt());
        }
        if (RANDOM.nextBoolean()) {
            bean.setCharValue2((char) RANDOM.nextInt());
        }

        if (RANDOM.nextBoolean()) {
            bean.setShortValue1((short) RANDOM.nextInt());
        }
        if (RANDOM.nextBoolean()) {
            bean.setShortValue2((short) RANDOM.nextInt());
        }

        if (RANDOM.nextBoolean()) {
            bean.setIntValue1(RANDOM.nextInt());
        }
        if (RANDOM.nextBoolean()) {
            bean.setIntValue2(RANDOM.nextInt());
        }

        if (RANDOM.nextBoolean()) {
            bean.setLongValue1(RANDOM.nextLong());
        }
        if (RANDOM.nextBoolean()) {
            bean.setLongValue2(RANDOM.nextLong());
        }

        if (RANDOM.nextBoolean()) {
            bean.setFloatValue1(RANDOM.nextFloat());
        }
        if (RANDOM.nextBoolean()) {
            bean.setFloatValue2(RANDOM.nextFloat());
        }

        if (RANDOM.nextBoolean()) {
            bean.setDoubleValue1(RANDOM.nextDouble());
        }
        if (RANDOM.nextBoolean()) {
            bean.setDoubleValue2(RANDOM.nextDouble());
        }

        if (RANDOM.nextBoolean()) {
            bean.setString(UUID.randomUUID().toString());
        }

        if (RANDOM.nextBoolean()) {
            bean.setBigInteger(getRandomBigInteger());
        }

        if (RANDOM.nextBoolean()) {
            bean.setBigDecimal(getRandomBigDecimal());
        }

        if (RANDOM.nextBoolean()) {
            bean.setDate(new Date());
        }

        if (RANDOM.nextBoolean()) {
            bean.setTestEnum(getRandomEnum());
        }
    }

    private BigInteger getRandomBigInteger() {
        return new BigInteger(Long.toString(RANDOM.nextLong())).multiply(new BigInteger(Long.toString(RANDOM.nextLong())));
    }

    private BigDecimal getRandomBigDecimal() {
        return new BigDecimal(Double.toString(RANDOM.nextDouble())).multiply(new BigDecimal(Double.toString(RANDOM.nextDouble())));
    }

    private TestEnum getRandomEnum() {
        if (RANDOM.nextBoolean()) {
            return TestEnum._1;
        } else {
            return TestEnum._2;
        }
    }

    private void assertTestBean(
            TestBean testBean,
            boolean booleanValue1, Boolean booleanValue2, boolean booleanValue3, Boolean booleanValue4,
            byte byteValue1, Byte byteValue2, byte byteValue3, Byte byteValue4,
            char charValue1, Character charValue2, char charValue3, Character charValue4,
            short shortValue1, Short shortValue2, short shortValue3, Short shortValue4,
            int intValue1, Integer intValue2, int intValue3, Integer intValue4,
            long longValue1, Long longValue2, long longValue3, Long longValue4,
            float floatValue1, Float floatValue2, float floatValue3, Float floatValue4,
            double doubleValue1, Double doubleValue2, double doubleValue3, Double doubleValue4,
            Map<String, Object> map,
            List<Object> list,
            String string,
            String n, boolean b, Boolean B,
            String defaultNotNull) {
        Assert.assertEquals(booleanValue1, testBean.booleanValue1);
        Assert.assertEquals(booleanValue2, testBean.booleanValue2);
        Assert.assertEquals(booleanValue3, testBean.booleanValue3);
        Assert.assertEquals(booleanValue4, testBean.booleanValue4);

        Assert.assertEquals(byteValue1, testBean.byteValue1);
        Assert.assertEquals(byteValue2, testBean.byteValue2);
        Assert.assertEquals(byteValue3, testBean.byteValue3);
        Assert.assertEquals(byteValue4, testBean.byteValue4);

        Assert.assertEquals(charValue1, testBean.charValue1);
        Assert.assertEquals(charValue2, testBean.charValue2);
        Assert.assertEquals(charValue3, testBean.charValue3);
        Assert.assertEquals(charValue4, testBean.charValue4);

        Assert.assertEquals(shortValue1, testBean.shortValue1);
        Assert.assertEquals(shortValue2, testBean.shortValue2);
        Assert.assertEquals(shortValue3, testBean.shortValue3);
        Assert.assertEquals(shortValue4, testBean.shortValue4);

        Assert.assertEquals(intValue1, testBean.intValue1);
        Assert.assertEquals(intValue2, testBean.intValue2);
        Assert.assertEquals(intValue3, testBean.intValue3);
        Assert.assertEquals(intValue4, testBean.intValue4);

        Assert.assertEquals(longValue1, testBean.longValue1);
        Assert.assertEquals(longValue2, testBean.longValue2);
        Assert.assertEquals(longValue3, testBean.longValue3);
        Assert.assertEquals(longValue4, testBean.longValue4);

        Assert.assertEquals(floatValue1, testBean.floatValue1, 1e-10);
        Assert.assertEquals(floatValue2, testBean.floatValue2);
        Assert.assertEquals(floatValue3, testBean.floatValue3, 1e-10);
        Assert.assertEquals(floatValue4, testBean.floatValue4);

        Assert.assertEquals(doubleValue1, testBean.doubleValue1, 1e-10);
        Assert.assertEquals(doubleValue2, testBean.doubleValue2);
        Assert.assertEquals(doubleValue3, testBean.doubleValue3, 1e-10);
        Assert.assertEquals(doubleValue4, testBean.doubleValue4);

        Assert.assertEquals(
                JSON.toJSONString(map, SerializerFeature.SortField, SerializerFeature.MapSortField),
                JSON.toJSONString(testBean.map, SerializerFeature.SortField, SerializerFeature.MapSortField)
        );
        Assert.assertEquals(
                JSON.toJSONString(list, SerializerFeature.SortField, SerializerFeature.MapSortField),
                JSON.toJSONString(testBean.list, SerializerFeature.SortField, SerializerFeature.MapSortField)
        );
        Assert.assertEquals(string, testBean.string);
        Assert.assertEquals(n, testBean.n);
        Assert.assertEquals(b, testBean.b);
        Assert.assertEquals(B, testBean.B);
        Assert.assertEquals(defaultNotNull, testBean.defaultNotNull);
        Assert.assertEquals(null, testBean.get);
        Assert.assertEquals(null, testBean.noGetMethod);
        Assert.assertEquals(null, testBean.getMethodWithParams);
        Assert.assertEquals(null, testBean.getMethodWithVoidReturnType1);
        Assert.assertEquals(null, testBean.getMethodWithVoidReturnType2);
        Assert.assertEquals(null, testBean.trueGetWithNoSet);
        Assert.assertEquals(null, testBean.trueGetWithNonVoidReturnSetMethod);
    }

    public enum TestEnum {
        _1(1),
        _2;

        private int i;

        TestEnum() {
        }

        TestEnum(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    public static final class TestBean {
        private boolean booleanValue1;
        private Boolean booleanValue2;
        private boolean booleanValue3;
        private Boolean booleanValue4;

        private byte byteValue1;
        private Byte byteValue2;
        private byte byteValue3;
        private Byte byteValue4;

        private char charValue1;
        private Character charValue2;
        private char charValue3;
        private Character charValue4;

        private short shortValue1;
        private Short shortValue2;
        private short shortValue3;
        private Short shortValue4;

        private int intValue1;
        private Integer intValue2;
        private int intValue3;
        private Integer intValue4;

        private long longValue1;
        private Long longValue2;
        private long longValue3;
        private Long longValue4;

        private float floatValue1;
        private Float floatValue2;
        private float floatValue3;
        private Float floatValue4;

        private double doubleValue1;
        private Double doubleValue2;
        private double doubleValue3;
        private Double doubleValue4;

        private Map<String, Object> map;
        private List<Object> list;

        private String string;

        private String n;
        private boolean b;
        private Boolean B;

        private String defaultNotNull = "string";

        private String get;

        private String noGetMethod;

        private String getMethodWithParams;

        private String getMethodWithVoidReturnType1;

        private String getMethodWithVoidReturnType2;

        private String trueGetWithNoSet;

        private String trueGetWithNonVoidReturnSetMethod;

        public boolean isBooleanValue1() {
            return booleanValue1;
        }

        public void setBooleanValue1(boolean booleanValue1) {
            this.booleanValue1 = booleanValue1;
        }

        public Boolean getBooleanValue2() {
            return booleanValue2;
        }

        public void setBooleanValue2(Boolean booleanValue2) {
            this.booleanValue2 = booleanValue2;
        }

        public Boolean isBooleanValue3() {
            return booleanValue3;
        }

        public void setBooleanValue3(Boolean booleanValue3) {
            if (booleanValue3 != null) {
                this.booleanValue3 = booleanValue3;
            }
        }

        public boolean getBooleanValue4() {
            return booleanValue4 == null ? false : booleanValue4;
        }

        public void setBooleanValue4(boolean booleanValue4) {
            this.booleanValue4 = booleanValue4;
        }

        public byte getByteValue1() {
            return byteValue1;
        }

        public void setByteValue1(byte byteValue1) {
            this.byteValue1 = byteValue1;
        }

        public Byte getByteValue2() {
            return byteValue2;
        }

        public void setByteValue2(Byte byteValue2) {
            this.byteValue2 = byteValue2;
        }

        public Byte getByteValue3() {
            return byteValue3;
        }

        public void setByteValue3(Byte byteValue3) {
            if (byteValue3 != null) {
                this.byteValue3 = byteValue3;
            }
        }

        public byte getByteValue4() {
            return byteValue4 == null ? (byte) 0 : byteValue4;
        }

        public void setByteValue4(byte byteValue4) {
            this.byteValue4 = byteValue4;
        }

        public char getCharValue1() {
            return charValue1;
        }

        public void setCharValue1(char charValue1) {
            this.charValue1 = charValue1;
        }

        public Character getCharValue2() {
            return charValue2;
        }

        public void setCharValue2(Character charValue2) {
            this.charValue2 = charValue2;
        }

        public Character getCharValue3() {
            return charValue3;
        }

        public void setCharValue3(Character charValue3) {
            if (charValue3 != null) {
                this.charValue3 = charValue3;
            }
        }

        public char getCharValue4() {
            return charValue4 == null ? (char) 0 : charValue4;
        }

        public void setCharValue4(char charValue4) {
            this.charValue4 = charValue4;
        }

        public short getShortValue1() {
            return shortValue1;
        }

        public void setShortValue1(short shortValue1) {
            this.shortValue1 = shortValue1;
        }

        public Short getShortValue2() {
            return shortValue2;
        }

        public void setShortValue2(Short shortValue2) {
            this.shortValue2 = shortValue2;
        }

        public Short getShortValue3() {
            return shortValue3;
        }

        public void setShortValue3(Short shortValue3) {
            if (shortValue3 != null) {
                this.shortValue3 = shortValue3;
            }
        }

        public short getShortValue4() {
            return shortValue4 == null ? (short) 0 : shortValue4;
        }

        public void setShortValue4(short shortValue4) {
            this.shortValue4 = shortValue4;
        }

        public int getIntValue1() {
            return intValue1;
        }

        public void setIntValue1(int intValue1) {
            this.intValue1 = intValue1;
        }

        public Integer getIntValue2() {
            return intValue2;
        }

        public void setIntValue2(Integer intValue2) {
            this.intValue2 = intValue2;
        }

        public Integer getIntValue3() {
            return intValue3;
        }

        public void setIntValue3(Integer intValue3) {
            if (intValue3 != null) {
                this.intValue3 = intValue3;
            }
        }

        public int getIntValue4() {
            return intValue4 == null ? 0 : intValue4;
        }

        public void setIntValue4(int intValue4) {
            this.intValue4 = intValue4;
        }

        public long getLongValue1() {
            return longValue1;
        }

        public void setLongValue1(long longValue1) {
            this.longValue1 = longValue1;
        }

        public Long getLongValue2() {
            return longValue2;
        }

        public void setLongValue2(Long longValue2) {
            this.longValue2 = longValue2;
        }

        public Long getLongValue3() {
            return longValue3;
        }

        public void setLongValue3(Long longValue3) {
            if (longValue3 != null) {
                this.longValue3 = longValue3;
            }
        }

        public long getLongValue4() {
            return longValue4 == null ? 0L : longValue4;
        }

        public void setLongValue4(long longValue4) {
            this.longValue4 = longValue4;
        }

        public float getFloatValue1() {
            return floatValue1;
        }

        public void setFloatValue1(float floatValue1) {
            this.floatValue1 = floatValue1;
        }

        public Float getFloatValue2() {
            return floatValue2;
        }

        public void setFloatValue2(Float floatValue2) {
            this.floatValue2 = floatValue2;
        }

        public Float getFloatValue3() {
            return floatValue3;
        }

        public void setFloatValue3(Float floatValue3) {
            if (floatValue3 != null) {
                this.floatValue3 = floatValue3;
            }
        }

        public float getFloatValue4() {
            return floatValue4 == null ? 0.0f : floatValue4;
        }

        public void setFloatValue4(float floatValue4) {
            this.floatValue4 = floatValue4;
        }

        public double getDoubleValue1() {
            return doubleValue1;
        }

        public void setDoubleValue1(double doubleValue1) {
            this.doubleValue1 = doubleValue1;
        }

        public Double getDoubleValue2() {
            return doubleValue2;
        }

        public void setDoubleValue2(Double doubleValue2) {
            this.doubleValue2 = doubleValue2;
        }

        public Double getDoubleValue3() {
            return doubleValue3;
        }

        public void setDoubleValue3(Double doubleValue3) {
            if (doubleValue3 != null) {
                this.doubleValue3 = doubleValue3;
            }
        }

        public double getDoubleValue4() {
            return doubleValue4 == null ? 0 : doubleValue4;
        }

        public void setDoubleValue4(double doubleValue4) {
            this.doubleValue4 = doubleValue4;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }

        public List<Object> getList() {
            return list;
        }

        public void setList(List<Object> list) {
            this.list = list;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public boolean isB() {
            return b;
        }

        public void setB(boolean b) {
            this.b = b;
        }

        public Boolean getB() {
            return B;
        }

        public void setB(Boolean b) {
            B = b;
        }

        public String getDefaultNotNull() {
            return defaultNotNull;
        }

        public void setDefaultNotNull(String defaultNotNull) {
            this.defaultNotNull = defaultNotNull;
        }

        public void setNoGetMethod(String noGetMethod) {
            this.noGetMethod = noGetMethod;
        }

        public String getGetMethodWithParams(int param) {
            return getMethodWithParams;
        }

        public void setGetMethodWithParams(String getMethodWithParams) {
            this.getMethodWithParams = getMethodWithParams;
        }

        public void getGetMethodWithVoidReturnType1() {

        }

        public void setGetMethodWithVoidReturnType1(String getMethodWithVoidReturnType1) {
            this.getMethodWithVoidReturnType1 = getMethodWithVoidReturnType1;
        }

        public Void getGetMethodWithVoidReturnType2() {
            return null;
        }

        public void setGetMethodWithVoidReturnType2(String getMethodWithVoidReturnType2) {
            this.getMethodWithVoidReturnType2 = getMethodWithVoidReturnType2;
        }

        public String getTrueGetWithNoSet() {
            return trueGetWithNoSet;
        }

        public String getTrueGetWithNonVoidReturnSetMethod() {
            return trueGetWithNonVoidReturnSetMethod;
        }

        public String setTrueGetWithNonVoidReturnSetMethod(String trueGetWithNonVoidReturnSetMethod) {
            this.trueGetWithNonVoidReturnSetMethod = trueGetWithNonVoidReturnSetMethod;
            return null;
        }

        public String get() {
            return get;
        }

        public void setGet(String get) {
            this.get = get;
        }
    }

    public static final class TestBeanWithNonNoArgsConstrcutor {

        private int age;

        public TestBeanWithNonNoArgsConstrcutor(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static final class NormalBean {
        private boolean booleanValue1;
        private Boolean booleanValue2;

        private byte byteValue1;
        private Byte byteValue2;

        private char charValue1;
        private Character charValue2;

        private short shortValue1;
        private Short shortValue2;

        private int intValue1;
        private Integer intValue2;

        private long longValue1;
        private Long longValue2;

        private float floatValue1;
        private Float floatValue2;

        private double doubleValue1;
        private Double doubleValue2;

        private String string;

        private BigInteger bigInteger;
        private BigDecimal bigDecimal;
        private Date date;
        private TestEnum testEnum;

        private Map<String, Object> map;

        private Collection<Object> collection;

        private NormalBean bean;

        public boolean isBooleanValue1() {
            return booleanValue1;
        }

        public void setBooleanValue1(boolean booleanValue1) {
            this.booleanValue1 = booleanValue1;
        }

        public Boolean getBooleanValue2() {
            return booleanValue2;
        }

        public void setBooleanValue2(Boolean booleanValue2) {
            this.booleanValue2 = booleanValue2;
        }

        public byte getByteValue1() {
            return byteValue1;
        }

        public void setByteValue1(byte byteValue1) {
            this.byteValue1 = byteValue1;
        }

        public Byte getByteValue2() {
            return byteValue2;
        }

        public void setByteValue2(Byte byteValue2) {
            this.byteValue2 = byteValue2;
        }

        public char getCharValue1() {
            return charValue1;
        }

        public void setCharValue1(char charValue1) {
            this.charValue1 = charValue1;
        }

        public Character getCharValue2() {
            return charValue2;
        }

        public void setCharValue2(Character charValue2) {
            this.charValue2 = charValue2;
        }

        public short getShortValue1() {
            return shortValue1;
        }

        public void setShortValue1(short shortValue1) {
            this.shortValue1 = shortValue1;
        }

        public Short getShortValue2() {
            return shortValue2;
        }

        public void setShortValue2(Short shortValue2) {
            this.shortValue2 = shortValue2;
        }

        public int getIntValue1() {
            return intValue1;
        }

        public void setIntValue1(int intValue1) {
            this.intValue1 = intValue1;
        }

        public Integer getIntValue2() {
            return intValue2;
        }

        public void setIntValue2(Integer intValue2) {
            this.intValue2 = intValue2;
        }

        public long getLongValue1() {
            return longValue1;
        }

        public void setLongValue1(long longValue1) {
            this.longValue1 = longValue1;
        }

        public Long getLongValue2() {
            return longValue2;
        }

        public void setLongValue2(Long longValue2) {
            this.longValue2 = longValue2;
        }

        public float getFloatValue1() {
            return floatValue1;
        }

        public void setFloatValue1(float floatValue1) {
            this.floatValue1 = floatValue1;
        }

        public Float getFloatValue2() {
            return floatValue2;
        }

        public void setFloatValue2(Float floatValue2) {
            this.floatValue2 = floatValue2;
        }

        public double getDoubleValue1() {
            return doubleValue1;
        }

        public void setDoubleValue1(double doubleValue1) {
            this.doubleValue1 = doubleValue1;
        }

        public Double getDoubleValue2() {
            return doubleValue2;
        }

        public void setDoubleValue2(Double doubleValue2) {
            this.doubleValue2 = doubleValue2;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public BigInteger getBigInteger() {
            return bigInteger;
        }

        public void setBigInteger(BigInteger bigInteger) {
            this.bigInteger = bigInteger;
        }

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public void setBigDecimal(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public TestEnum getTestEnum() {
            return testEnum;
        }

        public void setTestEnum(TestEnum testEnum) {
            this.testEnum = testEnum;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }

        public Collection<Object> getCollection() {
            return collection;
        }

        public void setCollection(Collection<Object> collection) {
            this.collection = collection;
        }

        public NormalBean getBean() {
            return bean;
        }

        public void setBean(NormalBean bean) {
            this.bean = bean;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NormalBean that = (NormalBean) o;
            return booleanValue1 == that.booleanValue1 &&
                    byteValue1 == that.byteValue1 &&
                    charValue1 == that.charValue1 &&
                    shortValue1 == that.shortValue1 &&
                    intValue1 == that.intValue1 &&
                    longValue1 == that.longValue1 &&
                    Float.compare(that.floatValue1, floatValue1) == 0 &&
                    Double.compare(that.doubleValue1, doubleValue1) == 0 &&
                    Objects.equals(booleanValue2, that.booleanValue2) &&
                    Objects.equals(byteValue2, that.byteValue2) &&
                    Objects.equals(charValue2, that.charValue2) &&
                    Objects.equals(shortValue2, that.shortValue2) &&
                    Objects.equals(intValue2, that.intValue2) &&
                    Objects.equals(longValue2, that.longValue2) &&
                    Objects.equals(floatValue2, that.floatValue2) &&
                    Objects.equals(doubleValue2, that.doubleValue2) &&
                    Objects.equals(string, that.string) &&
                    Objects.equals(bigInteger, that.bigInteger) &&
                    Objects.equals(bigDecimal, that.bigDecimal) &&
                    Objects.equals(date, that.date) &&
                    Objects.equals(map, that.map) &&
                    Objects.equals(collection, that.collection) &&
                    Objects.equals(bean, that.bean);
        }

        @Override
        public int hashCode() {
            return Objects.hash(booleanValue1, booleanValue2, byteValue1, byteValue2, charValue1, charValue2, shortValue1, shortValue2, intValue1, intValue2, longValue1, longValue2, floatValue1, floatValue2, doubleValue1, doubleValue2, string, bigInteger, bigDecimal, date, map, collection, bean);
        }
    }

    public static final class GenericBean<T> {
        private String name;
        private T data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GenericBean<?> that = (GenericBean<?>) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(data, that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, data);
        }
    }
}
