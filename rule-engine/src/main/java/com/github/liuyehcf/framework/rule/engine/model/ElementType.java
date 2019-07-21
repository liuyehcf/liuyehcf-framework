package com.github.liuyehcf.framework.rule.engine.model;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public enum ElementType {

    START("start"),
    CONDITION("condition"),
    ACTION("action"),
    JOIN_GATEWAY("joinGateway"),
    EXCLUSIVE_GATEWAY("exclusiveGateway"),
    SUB_RULE("subRule"),
    LISTENER("listener"),
    EVENT("event");

    private String type;

    ElementType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
