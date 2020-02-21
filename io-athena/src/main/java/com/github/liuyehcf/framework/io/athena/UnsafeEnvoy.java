package com.github.liuyehcf.framework.io.athena;

import com.github.liuyehcf.framework.io.athena.event.LeaderStatusQueryResponse;
import com.github.liuyehcf.framework.io.athena.event.Proposal;
import com.github.liuyehcf.framework.io.athena.event.Vote;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public interface UnsafeEnvoy extends Envoy {

    /**
     * get next unique id
     */
    long nextId();

    /**
     * enqueue event to the cluster's event queue
     *
     * @param event event to be appended
     * @param <T>   event's type
     */
    <T> void enqueue(T event);

    /**
     * send event to specified member(no limit in status) of the cluster
     *
     * @param address           address of member
     * @param event             event to be sent
     * @param connectIfNotExist if true, then it will create connection if connection not exist
     * @param <T>               type of event
     */
    <T> boolean whisperIgnoreStatus(Address address, T event, boolean connectIfNotExist);

    /**
     * statistics response of leader status query
     * if the number of unhealthy N >= H then return true
     * if the number of unhealthy N < H then return false
     * H means the majority number of the cluster
     *
     * @param response response of leader status query
     * @return statistics result, true means leader unhealthy
     */
    StatisticsResult<Void> statisticsLeaderStatusQuery(LeaderStatusQueryResponse response);

    /**
     * vote for pre-proposal
     *
     * @param proposal pre-proposal
     * @return pre-vote
     */
    Vote voteForPreProposal(Proposal proposal);

    /**
     * vote for formal-proposal
     *
     * @param proposal formal-proposal
     * @return formal-vote
     */
    Vote voteForFormalProposal(Proposal proposal);

    /**
     * statistics vote of pre-proposal
     * if the number of accept N >= H then return true
     * if the number of accept N < H then return false
     * H means the majority number of the cluster
     *
     * @param vote pre-vote
     * @return statistics result, true means (accept, candidate)
     */
    StatisticsResult<Member> statisticsPreProposal(Vote vote);

    /**
     * statistics vote of formal-proposal
     * if the number of accept N >= H then return true
     * if the number of accept N < H then return false
     * H means the majority number of the cluster
     *
     * @param vote formal-vote
     * @return statistics result, true means (accept, leader)
     */
    StatisticsResult<Member> statisticsFormalProposal(Vote vote);

    /**
     * accept the specified member as new leader
     *
     * @param member  specified member
     * @param version cluster version
     */
    void acceptLeader(Member member, long version);

    /**
     * record the current timestamp as the timestamp of follower's heartbeat
     *
     * @param member the specified member
     */
    void receiveLeaderKeepAlive(Member member);

    /**
     * return the latest timestamp of receiving LeaderKeepAlive
     *
     * @param member the specified member
     */
    long getLatestLeaderKeepAliveTimestamp(Member member);

    /**
     * record the current timestamp as the timestamp of leader's reply to heartbeat ACK
     */
    void receiveLeaderKeepAliveAck();

    /**
     * return the latest timestamp of receiving LeaderKeepAliveAck
     *
     * @return timestamp
     */
    long getLatestLeaderKeepAliveAckTimestamp();
}
