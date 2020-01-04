package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.Member;

/**
 * @author hechenfeng
 * @date 2019/9/9
 */
public class Vote implements Message {

    /**
     * state of election
     */
    private final ElectionState state;

    /**
     * transaction id
     */
    private final long transactionId;

    /**
     * whether accept
     */
    private final boolean accept;

    /**
     * proposer of this election may not equal with candidate
     * for example A can propose B as candidate
     */
    private final Member proposer;

    /**
     * candidate who run for election
     */
    private final Member candidate;

    public Vote(ElectionState state, long transactionId, boolean accept, Member proposer, Member candidate) {
        Assert.assertNotNull(state, "state");
        Assert.assertNotNull(proposer, "proposer");
        this.state = state;
        this.transactionId = transactionId;
        this.accept = accept;
        this.proposer = proposer;
        this.candidate = candidate;
    }

    @Override
    public final MessageType getType() {
        return MessageType.VOTE;
    }

    public ElectionState getState() {
        return state;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public boolean isAccept() {
        return accept;
    }

    public boolean isReject() {
        return !isAccept();
    }

    public Member getProposer() {
        return proposer;
    }

    public Member getCandidate() {
        return candidate;
    }

    @Override
    public String toString() {
        return String.format("Vote(state='%s', transactionId='%d', accept='%b', proposer='%s', candidate='%s')",
                state, transactionId, accept, Member.identifierWithId(proposer), Member.identifierWithId(candidate));
    }
}
