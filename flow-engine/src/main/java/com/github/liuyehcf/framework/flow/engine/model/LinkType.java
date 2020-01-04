package com.github.liuyehcf.framework.flow.engine.model;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public enum LinkType {
    NORMAL,
    TRUE,
    FALSE,
    ;

    public boolean isNormal() {
        return NORMAL.equals(this);
    }

    public boolean isTrue() {
        return TRUE.equals(this);
    }

    public boolean isFalse() {
        return FALSE.equals(this);
    }
}
