package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;

/**
 * @author chenlu
 * @date 2018/6/3
 */
public class VariableSymbol {
    /**
     * 偏移量
     */
    private final int offset;

    /**
     * 命名空间
     */
    private final Namespace namespace;

    /**
     * 符号名字
     */
    private final String name;

    /**
     * 符号类型
     */
    private final String type;

    /**
     * 符号宽度
     */
    private final int width;

    /**
     * 符号值
     */
    private Object value;

    public VariableSymbol(int offset, Namespace namespace, String name, String type, int width) {
        this.offset = offset;
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.width = width;
    }

    public int getOffset() {
        return offset;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
