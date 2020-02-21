package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class Proposal implements SystemEvent {

    /**
     * proposal id
     */
    private final long id;

    /**
     * stage of proposal
     */
    private final Stage stage;

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

    public Proposal(long id, Stage stage, Member proposer, long version, Member candidate) {
        Assert.assertNotNull(stage, "stage");
        Assert.assertNotNull(proposer, "proposer");
        Assert.assertNotNull(candidate, "candidate");
        this.id = id;
        this.stage = stage;
        this.proposer = proposer.copy();
        this.version = version;
        this.candidate = candidate.copy();
    }

    public long getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
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

    public enum Stage {

        /**
         * pre proposal
         */
        pre,

        /**
         * formal proposal
         */
        formal;

        public boolean isPre() {
            return pre.equals(this);
        }

        public boolean isFormal() {
            return formal.equals(this);
        }
    }
}
