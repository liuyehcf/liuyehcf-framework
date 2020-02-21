package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class Vote implements SystemEvent {

    /**
     * proposal id
     */
    private final long id;

    /**
     * state of election
     */
    private final Proposal.Stage stage;

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
     * the next cluster version if someone win the election
     */
    private final long version;

    /**
     * candidate who run for election
     */
    private final Member candidate;

    public Vote(long id, Proposal.Stage stage, boolean accept, Member proposer, long version, Member candidate) {
        Assert.assertNotNull(stage, "stage");
        Assert.assertNotNull(proposer, "proposer");
        this.id = id;
        this.stage = stage;
        this.accept = accept;
        this.proposer = proposer.copy();
        this.version = version;
        if (candidate == null) {
            this.candidate = null;
        } else {
            this.candidate = candidate.copy();
        }
    }

    public long getId() {
        return id;
    }

    public Proposal.Stage getStage() {
        return stage;
    }

    public boolean isAccept() {
        return accept;
    }

    public Member getProposer() {
        return proposer;
    }

    public long getVersion() {
        return version;
    }

    public Member getCandidate() {
        return candidate;
    }
}
