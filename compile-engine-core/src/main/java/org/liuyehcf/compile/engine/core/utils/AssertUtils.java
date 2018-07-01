package org.liuyehcf.compile.engine.core.utils;

import java.util.Objects;

/**
 * 断言工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class AssertUtils {

    public static void assertTrue(Boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }

    public static void assertTrue(Boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
    }

    public static void assertFalse(Boolean condition) {
        if (condition) {
            throw new AssertionError();
        }
    }

    public static void assertFalse(Boolean condition, String description) {
        if (condition) {
            throw new AssertionError(description);
        }
    }

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError();
        }
    }

    public static void assertNotNull(Object obj, String description) {
        if (obj == null) {
            throw new AssertionError(description);
        }
    }

    public static void assertNull(Object obj) {
        if (obj != null) {
            throw new AssertionError();
        }
    }

    public static void assertNull(Object obj, String description) {
        if (obj != null) {
            throw new AssertionError(description);
        }
    }

    public static void assertEquals(Object obj1, Object obj2) {
        if (!Objects.equals(obj1, obj2)) {
            throw new AssertionError();
        }
    }

    public static void assertEquals(Object obj1, Object obj2, String description) {
        if (!Objects.equals(obj1, obj2)) {
            throw new AssertionError(description);
        }
    }
}
