package org.liuyehcf.compile.engine.core.grammar.definition;

import java.io.Serializable;

/**
 * 词素类型
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public enum MorphemeType implements Serializable {
    /**
     * 普通词素
     */
    NORMAL(1),

    /**
     * 正则表达式词素
     */
    REGEX(8);

    private final int order;

    MorphemeType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
