package com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public interface Member extends MemberIdentifier {

    long INVALID_ID = -1L;

    static String identifierWithId(Member member) {
        return String.format("%s/%d", member.getIdentifier(), member.getId());
    }

    /**
     * cluster unique id within cluster, issued by the leader
     * member's id may change during whole lifetime
     * the uniqueness of id in cluster only guarantees the final consistency
     */
    long getId();

    /**
     * get role of this member
     */
    MemberRole getRole();

    /**
     * status of cluster member, it can only be modified by leader
     */
    MemberStatus getStatus();

    /**
     * local status of cluster member, it can be detected and modified locally
     * and this status will not spread to other node
     * <p>
     * if member reachable, then getLocalStatus() is equals with getStatus()
     * if member unreachable, then getLocalStatus() is unreachable
     *
     * @see MemberStatus#unreachable
     */
    MemberStatus getLocalStatus();

    /**
     * set local status based on detect, optional status is unreachable
     *
     * @param status detected status
     */
    void setLocalStatus(MemberStatus status);

    /**
     * clone all fields except status
     *
     * @param id     origin id will be used if id is null
     * @param role   origin role will be used if role is null
     * @param status origin status will be used if status is null
     */
    Member clone(Long id, MemberRole role, MemberStatus status);
}
