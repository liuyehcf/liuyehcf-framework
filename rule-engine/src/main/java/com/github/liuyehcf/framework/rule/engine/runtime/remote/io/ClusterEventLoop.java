package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.PromiseListener;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Identifier;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Member;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.MemberIdentifier;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Topology;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.Election;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.LeaderStatusResponse;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.Vote;

import java.util.Collection;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public interface ClusterEventLoop {

    /**
     * get rule engine instance
     */
    RuleEngine getEngine();

    /**
     * get topology of cluster
     */
    Topology getTopology();

    /**
     * add cluster channel if absent
     * if exists, the cluster channel will be closed
     *
     * @return {@code true} if previous not exists, otherwise false
     */
    boolean addChannelIfAbsent(ClusterChannel clusterChannel);

    /**
     * whether has channel
     */
    boolean hasChannel(Identifier peerIdentifier);

    /**
     * get cluster channel
     */
    ClusterChannel getChannel(Identifier peerIdentifier);

    /**
     * get all channels
     */
    Collection<ClusterChannel> getChannels();

    /**
     * create client mode cluster channel
     */
    void createChannel(MemberIdentifier peerIdentifier) throws InterruptedException;

    /**
     * send message to specified member
     *
     * @param peerIdentifier identifier of peer member
     * @param message        to be delivered
     * @return whether success
     */
    boolean unicast(Identifier peerIdentifier, Object message);

    /**
     * send message to all connected members
     *
     * @param message message to be delivered
     */
    void broadcast(Object message);

    /**
     * statistics for leader status
     */
    void statisticsForLeaderStatus(LeaderStatusResponse message);

    /**
     * update member status of topology, and spread it to other connected members
     */
    void updateAndBroadcastMemberStatus(Member member);

    /**
     * vote for first state election
     *
     * @see com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.ElectionState#first
     */
    void voteForFirstStateElection(Election election);

    /**
     * vote for election
     *
     * @see com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.ElectionState#second
     */
    void voteForSecondStateElection(Election election);

    /**
     * vote for first state election
     *
     * @see com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.ElectionState#first
     */
    void statisticsForFirstStateElection(Vote vote);

    /**
     * vote for second state election
     *
     * @see com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.ElectionState#second
     */
    void statisticsForSecondStateElection(Vote vote);

    /**
     * clear election status
     */
    void clearElectionStatus();

    /**
     * add close promise listener
     * when event loop is shutdown, this listener will be and only be execute once
     */
    Promise<Void> addCloseListener(PromiseListener<Void> listener);

    /**
     * shutdown
     */
    void shutdown();
}
