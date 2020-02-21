package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/7
 */
public enum MemberRole {

    /**
     * follower in the cluster
     * who can participate in vote
     */
    follower,

    /**
     * follower in the cluster
     * who can manage the cluster
     */
    leader;

    public boolean isLeader() {
        return leader.equals(this);
    }

    public boolean isFollower() {
        return follower.equals(this);
    }
}
