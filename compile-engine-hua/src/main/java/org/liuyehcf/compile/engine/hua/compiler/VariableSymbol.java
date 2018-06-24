package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.hua.model.Type;

/**
 * 符号
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class VariableSymbol {
    /**
     * 偏移量
     */
    private final int offset;

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

    /**
     * 符号值
     */
    private Object value;

    public VariableSymbol(int offset, int namespaceId, String name, Type type) {
        this.offset = offset;
        this.namespaceId = namespaceId;
        this.name = name;
        this.type = type;
    }

    public int getOffset() {
        return offset;
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

    public Object getValue() {
        return value;
    }
}
