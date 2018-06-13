package org.liuyehcf.compile.engine.core.grammar.definition;

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
     * 标记非终结符词素
     */
    MARK(64);

    private final int order;

    MorphemeType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
