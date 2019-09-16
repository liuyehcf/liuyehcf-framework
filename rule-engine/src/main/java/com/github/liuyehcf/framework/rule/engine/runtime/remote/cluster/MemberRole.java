package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/9
 */
public enum MemberRole {

    /**
     * leader of cluster, member state changes can only be accomplished through leader
     */
    leader,

    /**
     * normal member of cluster, can vote for election
     */
    follower,

    ;

    public boolean isLeader() {
        return leader.equals(this);
    }
}
