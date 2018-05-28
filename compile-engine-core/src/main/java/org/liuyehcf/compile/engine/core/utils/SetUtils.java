package org.liuyehcf.compile.engine.core.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Set工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class SetUtils {
    private SetUtils() {
    }

    @SafeVarargs
    public static <T> Set<T> of(T... elements) {
        return new HashSet<>((Arrays.asList(elements)));
    }

    public static <T> Set<T> extract(Set<T> set, T element) {
        Set<T> newSet = new HashSet<>(set);
        newSet.remove(element);
        return newSet;
    }

    public static <T> Set<T> of(Set<T> set1, Set<T> set2) {
        Set<T> newSet = new HashSet<>(set1);

        newSet.addAll(set2);

        return newSet;
    }
}
