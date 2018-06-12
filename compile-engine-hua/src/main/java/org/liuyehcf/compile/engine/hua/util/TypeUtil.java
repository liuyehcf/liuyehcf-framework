package org.liuyehcf.compile.engine.hua.util;

/**
 * @author chenlu
 * @date 2018/6/12
 */
public class TypeUtil {
    public static boolean isArrayType(String type) {
        return type.length() >= 2
                && type.charAt(type.length() - 1) == ']'
                && type.charAt(type.length() - 2) == '[';
    }
}
