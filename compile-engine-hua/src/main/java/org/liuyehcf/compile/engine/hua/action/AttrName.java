package org.liuyehcf.compile.engine.hua.action;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public enum AttrName {
    TYPE("type"),
    WIDTH("width"),
    IGNORE_NEXT_ENTER_NAMESPACE("ignore_next_enter_namespace");

    private final String name;

    AttrName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
