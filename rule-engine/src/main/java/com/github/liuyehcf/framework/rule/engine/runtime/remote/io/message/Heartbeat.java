package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Member;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class Heartbeat implements Message {

    private final Member leader;
    private final long transactionId;
    private final String clusterIdentifier;

    public Heartbeat(Member leader, long transactionId, String clusterIdentifier) {
        this.leader = leader;
        this.transactionId = transactionId;
        this.clusterIdentifier = clusterIdentifier;
    }

    @Override
    public final MessageType getType() {
        return MessageType.HEART_BEAT;
    }

    public Member getLeader() {
        return leader;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public String getClusterIdentifier() {
        return clusterIdentifier;
    }
}
