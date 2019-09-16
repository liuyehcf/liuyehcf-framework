package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.MemberStatus;

/**
 * @author hechenfeng
 * @date 2019/9/12
 */
public class LeaderStatusResponse implements Message {

    private final long requestId;
    private final MemberStatus status;

    public LeaderStatusResponse(long requestId, MemberStatus status) {
        this.requestId = requestId;
        this.status = status;
    }

    @Override
    public final MessageType getType() {
        return MessageType.LEADER_STATUS_RESPONSE;
    }

    public long getRequestId() {
        return requestId;
    }

    public MemberStatus getStatus() {
        return status;
    }
}
