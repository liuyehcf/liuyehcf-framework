package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * 语法树节点
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class SyntaxNode {

    private final Map<String, Object> attrs = new HashMap<>(16);
    private Symbol id;
    private String value;

    SyntaxNode(Symbol id, String value) {
        this.id = id;
        this.value = value;
    }

    SyntaxNode() {
    }

    public Object putIfAbsent(String key, Object attr) {
        return attrs.putIfAbsent(key, attr);
    }

    @SuppressWarnings("unchecked")
    public <T> T put(String key, T attr) {
        return (T) attrs.put(key, attr);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) attrs.get(key);
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public Symbol getId() {
        return id;
    }

    void setId(Symbol id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }
}
