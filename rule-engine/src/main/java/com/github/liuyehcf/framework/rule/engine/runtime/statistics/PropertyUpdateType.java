package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public enum PropertyUpdateType {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private String type;

    PropertyUpdateType(String type) {
        this.type = type;
    }

    public static PropertyUpdateType typeOf(String type) {
        for (PropertyUpdateType propertyUpdateType : values()) {
            if (Objects.equals(propertyUpdateType.getType(), type)) {
                return propertyUpdateType;
            }
        }

        throw new UnsupportedOperationException();
    }

    public String getType() {
        return type;
    }
}
