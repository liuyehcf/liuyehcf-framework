package com.github.liuyehcf.framework.rpc.maple.util;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author hechenfeng
 * @date 2019/3/21
 */
public abstract class Assert {

    public static void assertTrue(Boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    public static void assertTrue(Boolean condition, String description) {
        if (!condition) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertFalse(Boolean condition) {
        if (condition) {
            throw new IllegalArgumentException();
        }
    }

    public static void assertFalse(Boolean condition, String description) {
        if (condition) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void assertNotNull(Object obj, String description) {
        if (obj == null) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertNull(Object obj) {
        if (obj != null) {
            throw new IllegalArgumentException();
        }
    }

    public static void assertNull(Object obj, String description) {
        if (obj != null) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertNotBlank(String s) {
        assertNotBlank(s, null);
    }

    public static void assertNotBlank(String s, String description) {
        if (s == null || s.trim().length() == 0) {
            throw new IllegalArgumentException(description);
        }
    }

    public static void assertEquals(Object obj1, Object obj2) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException();
        }
    }

    public static void assertEquals(Object obj1, Object obj2, String description) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(description);
        }
    }

    public static <T> T assertSuccess(Callable<T> callable, String description) {
        try {
            return callable.call();
        } catch (Throwable e) {
            throw new IllegalArgumentException(description, e);
        }
    }

    public static <T> T assertSuccess(Callable<T> callable) {
        return assertSuccess(callable, null);
    }
}
