package org.liuyehcf.compile.engine.core.utils;

/**
 * 断言工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class AssertUtils {
    private AssertUtils() {
    }

    public static void assertTrue(Boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }

    public static void assertFalse(Boolean condition) {
        if (condition) {
            throw new AssertionError();
        }
    }

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError();
        }
    }

    public static void assertNull(Object obj) {
        if (obj != null) {
            throw new AssertionError();
        }
    }
}
