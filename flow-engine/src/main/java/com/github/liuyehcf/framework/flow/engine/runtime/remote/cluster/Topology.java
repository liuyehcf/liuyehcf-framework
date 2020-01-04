package com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public interface Topology extends Identifier, Iterable<Member> {

    /**
     * get leader of this cluster
     */
    Member getLeader();

    /**
     * whether this member is leader of cluster
     */
    boolean isSelfLeader();

    /**
     * whether specified identifier is leader of cluster
     */
    boolean isLeader(Identifier identifier);

    /**
     * whether specified member is self
     */
    boolean isSelf(Identifier identifier);

    /**
     * get self member of cluster
     */
    Member getSelf();

    /**
     * whether cluster contains member with specified identifier
     */
    boolean hasMember(Identifier identifier);

    /**
     * get member of specified identifier
     *
     * @return null if absent
     */
    Member getMember(Identifier identifier);

    /**
     * add member if absent
     */
    void addMemberIfAbsent(Member member);

    /**
     * add or replace member with specified identifier
     */
    void addOrReplaceMember(Member member);

    /**
     * number of members with status active
     * and not unreachable locally
     */
    int activeNum();

    /**
     * get member id cnt
     */
    long getMemberIdCnt();

    /**
     * unique id for new member
     */
    long generateNextMemberId();

    /**
     * transaction id of cluster, used for election
     * it will only be updated after each successful election
     */
    long getTransactionId();

    /**
     * increase transactionId if current value equals with expectedTransactionId
     * may failed if concurrently invoke
     */
    void increaseTransactionId(long expectedTransactionId);

    /**
     * assume leader if current transactionId equals with expectedTransactionId
     * this method will increase transactionId
     * may failed if concurrently invoke
     */
    boolean assumeLeader(long expectedTransactionId);

    /**
     * make self as leader if current transactionId equals with expectedTransactionId
     * this method will set transactionId as {@param updateTransactionId}
     * may failed if concurrently invoke
     */
    boolean assumeLeader(long expectedTransactionId, long updateTransactionId);

    /**
     * sync topology from other topology
     */
    void syncFrom(Topology otherTopology);

    /**
     * identifier of cluster, depending on the number and status of members
     * treat cluster's identifier as md5 or checksum
     */
    @Override
    String getIdentifier();
}
