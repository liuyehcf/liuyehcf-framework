package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Member;

/**
 * @author chenfeng.hcf
 * @date 2019/9/9
 */
public class Election implements Message {

    /**
     * state of election
     */
    private final ElectionState state;

    /**
     * transaction id
     */
    private final long transactionId;

    /**
     * proposer of this election may not equal with candidate
     * for example A can propose B as candidate
     */
    private final Member proposer;

    /**
     * candidate who run for election
     */
    private final Member candidate;

    public Election(ElectionState state, long transactionId, Member proposer, Member candidate) {
        Assert.assertNotNull(state, "state");
        Assert.assertNotNull(proposer, "proposer");
        Assert.assertNotNull(candidate, "candidate");
        this.state = state;
        this.transactionId = transactionId;
        this.proposer = proposer;
        this.candidate = candidate;
    }

    @Override
    public final MessageType getType() {
        return MessageType.ELECTION;
    }

    public ElectionState getState() {
        return state;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public Member getProposer() {
        return proposer;
    }

    public Member getCandidate() {
        return candidate;
    }

    public Election toSecondState() {
        return new Election(ElectionState.second, transactionId, proposer, candidate);
    }

    @Override
    public String toString() {
        return String.format("Election(state='%s', transactionId='%d', proposer='%s', candidate='%s'",
                state, transactionId, Member.identifierWithId(proposer), Member.identifierWithId(candidate));
    }
}
