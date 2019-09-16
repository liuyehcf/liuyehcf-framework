package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
public enum MemberStatus {

    /**
     * before new member of cluster adding to the cluster
     */
    init,

    /**
     * new member of cluster is adding to the cluster
     * during which some pre hooks(checks for example) will be executed
     */
    joining,

    /**
     * member of cluster is active
     * in this status, member can communicate to other active members
     */
    active,

    /**
     * member of cluster is removing from the cluster
     * during which some post hooks will be executed
     */
    leaving,

    /**
     * member of cluster detected inactive
     */
    inactive,

    /**
     * detect unreachable
     */
    unreachable,

    ;

    public boolean isInit() {
        return init.equals(this);
    }

    public boolean isUnreachable() {
        return unreachable.equals(this);
    }

    public boolean isActive() {
        return active.equals(this);
    }

    public boolean isLeaving() {
        return leaving.equals(this);
    }

    public boolean isInactive() {
        return inactive.equals(this);
    }
}
