package org.liuyehcf.compile.engine.core.utils;

/**
 * 字符串工具类
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
public class StringUtils {
    public static boolean isBlank(String s) {
        return s == null || "".equals(s);
    }
}
