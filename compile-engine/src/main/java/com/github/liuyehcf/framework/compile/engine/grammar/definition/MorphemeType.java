package com.github.liuyehcf.framework.compile.engine.grammar.definition;

/**
 * 词素类型
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public enum MorphemeType {
    /**
     * 普通词素
     */
    NORMAL(1),

    /**
     * 正则表达式词素
     */
    REGEX(8),

    /**
     * 自定义处理过程词素
     * 仅用于词法分析器
     */
    OPERATOR(16);

    private final int order;

    MorphemeType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
