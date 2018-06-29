package org.liuyehcf.compile.engine.core.utils;

/**
 * 字符工具类
 *
 * @author chenlu
 * @date 2018/6/29
 */
public abstract class CharacterUtil {
    public static boolean isBlankChar(char c) {
        return 9 <= c && c <= 13 || c == 32;
    }
}
