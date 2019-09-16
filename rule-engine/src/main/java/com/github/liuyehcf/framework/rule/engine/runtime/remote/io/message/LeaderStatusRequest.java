package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

/**
 * @author chenfeng.hcf
 * @date 2019/9/12
 */
public class LeaderStatusRequest implements Message {

    private final long requestId;

    public LeaderStatusRequest(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public final MessageType getType() {
        return MessageType.LEADER_STATUS_REQUEST;
    }

    public long getRequestId() {
        return requestId;
    }
}
