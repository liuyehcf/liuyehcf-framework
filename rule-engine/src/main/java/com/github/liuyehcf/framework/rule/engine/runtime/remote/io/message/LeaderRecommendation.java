package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

/**
 * @author chenfeng.hcf
 * @date 2019/9/13
 */
public class LeaderRecommendation implements Message {

    private final long expectedTransactionId;

    public LeaderRecommendation(long expectedTransactionId) {
        this.expectedTransactionId = expectedTransactionId;
    }

    @Override
    public final MessageType getType() {
        return MessageType.LEADER_RECOMMENDATION;
    }

    public long getExpectedTransactionId() {
        return expectedTransactionId;
    }
}
