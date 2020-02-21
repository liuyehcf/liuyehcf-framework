package com.github.liuyehcf.framework.common.tools.asserts;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 断言工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class Assert {

    public static void assertTrue(boolean condition, Callable<String> loader) {
        if (!condition) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertTrue(boolean condition, String description) {
        assertTrue(condition, () -> description);
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, () -> null);
    }

    public static void assertFalse(boolean condition, Callable<String> loader) {
        if (condition) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertFalse(boolean condition, String description) {
        assertFalse(condition, () -> description);
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, () -> null);
    }

    public static void assertNotNull(Object obj, Callable<String> loader) {
        if (obj == null) {
            throw new NullPointerException(lazyLoad(loader));
        }
    }

    public static void assertNotNull(Object obj, String description) {
        assertNotNull(obj, () -> description);
    }

    public static void assertNotNull(Object obj) {
        assertNotNull(obj, () -> null);
    }

    public static void assertNull(Object obj, Callable<String> loader) {
        if (obj != null) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertNull(Object obj, String description) {
        assertNull(obj, () -> description);
    }

    public static void assertNull(Object obj) {
        assertNull(obj, () -> null);
    }

    public static void assertNotBlank(String s, Callable<String> loader) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertNotBlank(String s, String description) {
        assertNotBlank(s, () -> description);
    }

    public static void assertNotBlank(String s) {
        assertNotBlank(s, () -> null);
    }

    public static void assertBlank(String s, Callable<String> loader) {
        if (StringUtils.isNotBlank(s)) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertBlank(String s, String description) {
        assertBlank(s, () -> description);
    }

    public static void assertBlank(String s) {
        assertBlank(s, () -> null);
    }

    public static void assertEmpty(Collection<?> collection, Callable<String> loader) {
        if (collection != null && !collection.isEmpty()) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertEmpty(Collection<?> collection, String description) {
        assertEmpty(collection, () -> description);
    }

    public static void assertEmpty(Collection<?> collection) {
        assertEmpty(collection, () -> null);
    }

    public static void assertNotEmpty(Collection<?> collection, Callable<String> loader) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertNotEmpty(Collection<?> collection, String description) {
        assertNotEmpty(collection, () -> description);
    }

    public static void assertNotEmpty(Collection<?> collection) {
        assertNotEmpty(collection, () -> null);
    }

    public static void assertEquals(Object obj1, Object obj2, Callable<String> loader) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertEquals(Object obj1, Object obj2, String description) {
        assertEquals(obj1, obj2, () -> description);
    }

    public static void assertEquals(Object obj1, Object obj2) {
        assertEquals(obj1, obj2, () -> null);
    }

    public static void assertNotEquals(Object obj1, Object obj2, Callable<String> loader) {
        if (Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(lazyLoad(loader));
        }
    }

    public static void assertNotEquals(Object obj1, Object obj2, String description) {
        assertNotEquals(obj1, obj2, () -> description);
    }

    public static void assertNotEquals(Object obj1, Object obj2) {
        assertNotEquals(obj1, obj2, () -> null);
    }

    private static String lazyLoad(Callable<String> callable) {
        try {
            return callable.call();
        } catch (Throwable e) {
            throw new IllegalArgumentException("load description throw an unexpected exception", e);
        }
    }
}
