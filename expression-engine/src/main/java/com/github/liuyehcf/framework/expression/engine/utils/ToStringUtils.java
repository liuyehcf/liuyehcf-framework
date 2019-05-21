package com.github.liuyehcf.framework.expression.engine.utils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class ToStringUtils {
    public static String toString(Object obj) {
        if (obj == null) {
            return Objects.toString(null);
        } else if (obj.getClass().isArray()) {
            if (obj instanceof boolean[]) {
                return Arrays.toString((boolean[]) obj);
            } else if (obj instanceof byte[]) {
                return Arrays.toString((byte[]) obj);
            } else if (obj instanceof short[]) {
                return Arrays.toString((short[]) obj);
            } else if (obj instanceof char[]) {
                return Arrays.toString((char[]) obj);
            } else if (obj instanceof int[]) {
                return Arrays.toString((int[]) obj);
            } else if (obj instanceof long[]) {
                return Arrays.toString((long[]) obj);
            } else if (obj instanceof float[]) {
                return Arrays.toString((float[]) obj);
            } else if (obj instanceof double[]) {
                return Arrays.toString((double[]) obj);
            } else {
                return Arrays.deepToString((Object[]) obj);
            }
        } else {
            return Objects.toString(obj);
        }
    }
}
