package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class JoinClusterResponse implements SystemEvent {

    private final boolean isAccept;
    private final Member leader;
    private final long version;
    private final String reason;

    public JoinClusterResponse(boolean isAccept, Member leader, long version, String reason) {
        this.isAccept = isAccept;
        if (leader != null) {
            this.leader = leader.copy();
        } else {
            this.leader = null;
        }
        this.version = version;
        this.reason = reason;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public Member getLeader() {
        return leader;
    }

    public long getVersion() {
        return version;
    }

    public String getReason() {
        return reason;
    }
}
