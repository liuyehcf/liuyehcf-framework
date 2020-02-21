package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/7
 */
public enum MemberStatus {

    /**
     * init
     */
    init,

    /**
     * joining the cluster
     */
    joining,

    /**
     * after join cluster successfully
     */
    active,

    /**
     * after heartbeat timeout
     */
    unreachable,

    /**
     * after ttl timeout
     */
    leaving,

    /**
     * after cleanup
     */
    removed;

    public boolean isInit() {
        return init.equals(this);
    }

    public boolean isJoining() {
        return joining.equals(this);
    }

    public boolean isActive() {
        return active.equals(this);
    }

    public boolean isUnreachable() {
        return unreachable.equals(this);
    }

    public boolean isLeaving() {
        return leaving.equals(this);
    }

    public boolean isRemoved() {
        return removed.equals(this);
    }
}
