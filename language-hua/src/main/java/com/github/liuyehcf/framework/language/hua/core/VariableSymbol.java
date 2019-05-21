package com.github.liuyehcf.framework.language.hua.core;

import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;

/**
 * 符号
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class VariableSymbol {

    /**
     * 顺序，即第几个符号
     */
    private final int order;

    /**
     * 命名空间id
     */
    private final int namespaceId;

    /**
     * 符号名字
     */
    private final String name;

    /**
     * 符号类型
     */
    private final Type type;

    public VariableSymbol(int order, int namespaceId, String name, Type type) {
        this.order = order;
        this.namespaceId = namespaceId;
        this.name = name;
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public int getNamespaceId() {
        return namespaceId;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
