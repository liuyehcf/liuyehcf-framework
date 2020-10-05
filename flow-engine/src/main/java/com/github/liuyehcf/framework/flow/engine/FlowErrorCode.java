package com.github.liuyehcf.framework.flow.engine;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public enum FlowErrorCode {

    COMPILE("001", "COMPILE"),

    PROMISE("011", "PROMISE"),

    SERIALIZE("021", "SERIALIZE"),

    THREAD_POOL("031", "THREAD_POOL"),

    ACTION("041", "ACTION"),
    CONDITION("042", "CONDITION"),
    EXCLUSIVE_GATEWAY("043", "EXCLUSIVE_GATEWAY"),
    JOIN_GATEWAY("044", "JOIN_GATEWAY"),
    SUB_FLOW("045", "SUB_FLOW"),
    LISTENER("051", "LISTENER"),

    DELEGATE_FIELD("061", "DELEGATE_FIELD"),
    PROPERTY("062", "PROPERTY"),
    PLACE_HOLDER("063", "PLACE_HOLDER"),

    LINK_TERMINATE("071", "LINK_TERMINATE"),
    INSTANCE_TERMINATE("072", "INSTANCE_TERMINATE"),

    FLOW_ALREADY_FINISHED("081", "FLOW_ALREADY_FINISHED"),
    ;

    private final String code;
    private final String scope;

    FlowErrorCode(String code, String scope) {
        this.code = code;
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }

    public String getScope() {
        return scope;
    }

    public String getReason() {
        return String.format("[ %s, %s ]", code, scope);
    }
}
