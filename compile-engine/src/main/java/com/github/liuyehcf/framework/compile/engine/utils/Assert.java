package com.github.liuyehcf.framework.compile.engine.utils;

import java.util.Collection;
import java.util.Objects;

/**
 * 断言工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class Assert {

    public static void assertTrue(Boolean condition, String description) {
        if (!condition) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertTrue(Boolean condition) {
        assertTrue(condition, null);
    }

    public static void assertFalse(Boolean condition, String description) {
        if (condition) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertFalse(Boolean condition) {
        assertFalse(condition, null);
    }

    public static void assertNotNull(Object obj, String description) {
        if (obj == null) {
            throw new NullPointerException(description);
        }
    }

    public static void assertNotNull(Object obj) {
        assertNotNull(obj, null);
    }

    public static void assertNull(Object obj, String description) {
        if (obj != null) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertNull(Object obj) {
        assertNull(obj, null);
    }

    public static void assertEmpty(Collection<?> collection, String description) {
        if (collection != null && !collection.isEmpty()) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertEmpty(Collection<?> collection) {
        assertEmpty(collection, null);
    }

    public static void assertNotEmpty(Collection<?> collection, String description) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertNotEmpty(Collection<?> collection) {
        assertNotEmpty(collection, null);
    }

    public static void assertEquals(Object obj1, Object obj2, String description) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertEquals(Object obj1, Object obj2) {
        assertEquals(obj1, obj2, null);
    }

    public static void assertNotEquals(Object obj1, Object obj2, String description) {
        if (Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertNotEquals(Object obj1, Object obj2) {
        assertNotEquals(obj1, obj2, null);
    }
}
