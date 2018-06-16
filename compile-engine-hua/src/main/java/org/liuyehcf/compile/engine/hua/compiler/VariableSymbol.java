package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import org.liuyehcf.compile.engine.hua.model.Type;

/**
 * @author hechenfeng
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
    private final Type type;

    /**
     * 符号值
     */
    private Object value;

    public VariableSymbol(int offset, Namespace namespace, String name, Type type) {
        this.offset = offset;
        this.namespace = namespace;
        this.name = name;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
