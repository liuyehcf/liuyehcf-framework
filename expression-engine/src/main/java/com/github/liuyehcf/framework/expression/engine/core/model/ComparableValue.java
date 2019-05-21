package com.github.liuyehcf.framework.expression.engine.core.model;

/**
 * @author hechenfeng
 * @date 2018/10/2
 */
public enum ComparableValue {
    NEGATIVE(-1),
    ZERO(0),
    POSITIVE(1);

    private int value;

    ComparableValue(int value) {
        this.value = value;
    }

    public static ComparableValue valueOf(boolean condition) {
        return valueOf(condition ? 1L : 0L);
    }

    public static ComparableValue valueOf(long value) {
        if (value < 0) {
            return NEGATIVE;
        } else if (value == 0) {
            return ZERO;
        } else {
            return POSITIVE;
        }
    }

    public static ComparableValue valueOf(int value) {
        return valueOf((long) value);
    }

    public int getValue() {
        return value;
    }
}

