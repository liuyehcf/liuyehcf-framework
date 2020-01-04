package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.Member;

/**
 * @author hechenfeng
 * @date 2019/9/10
 */
public class ElectionResult implements Message {

    private final Member leader;
    private final long transactionId;

    public ElectionResult(Member leader, long transactionId) {
        this.leader = leader;
        this.transactionId = transactionId;
    }

    @Override
    public final MessageType getType() {
        return MessageType.ELECTION_RESULT;
    }

    public Member getLeader() {
        return leader;
    }

    public long getTransactionId() {
        return transactionId;
    }
}
