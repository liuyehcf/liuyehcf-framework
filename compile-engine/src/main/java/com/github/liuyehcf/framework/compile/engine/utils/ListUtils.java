package com.github.liuyehcf.framework.compile.engine.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * List工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class ListUtils {

    public static <T> List<T> subListExceptFirstElement(List<T> list) {
        return list.subList(1, list.size());
    }

    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        return new ArrayList<>((Arrays.asList(elements)));
    }

    public static <T> List<T> of(T element, List<T> list) {
        List<T> newList = new ArrayList<>();
        newList.add(element);
        newList.addAll(list);
        return newList;
    }

    public static <T> List<T> of(List<T> list, T element) {
        List<T> newList = new ArrayList<>(list);
        newList.add(element);
        return newList;
    }

    @SafeVarargs
    public static <T> List<T> of(List<T> list, T... elements) {
        List<T> newList = new ArrayList<>(list);
        newList.addAll(Arrays.asList(elements));
        return newList;
    }

    public static <T> List<T> of(List<T> list1, List<T> list2) {
        List<T> newList = new ArrayList<>(list1);
        newList.addAll(list2);
        return newList;
    }

    public static <T extends Comparable<T>> List<T> sort(List<T> list) {
        Collections.sort(list);
        return list;
    }
}
