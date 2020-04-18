package com.github.liuyehcf.framework.common.tools.bean;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public abstract class OptionalUtils {

    public static <T> T getOrDefault(T param, T defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        return param;
    }
}
