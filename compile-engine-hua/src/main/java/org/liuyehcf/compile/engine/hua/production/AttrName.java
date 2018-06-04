package org.liuyehcf.compile.engine.hua.production;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public enum AttrName {
    TYPE("type"),
    WIDTH("width"),
    ADDRESS("address"),
    CODES("codes");

    private final String name;

    AttrName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
