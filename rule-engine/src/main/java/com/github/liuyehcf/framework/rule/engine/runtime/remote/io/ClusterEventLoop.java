package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.PromiseListener;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNode;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNodeIdentifier;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterTopology;

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
    ClusterTopology getTopology();

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
    boolean hasChannel(String peerIdentifier);

    /**
     * get cluster channel
     */
    ClusterChannel getChannel(String peerIdentifier);

    /**
     * get all channels
     */
    Collection<ClusterChannel> getChannels();

    /**
     * create client mode cluster channel
     */
    void createChannel(ClusterNodeIdentifier peerIdentifier) throws InterruptedException;

    /**
     * update node status of topology, and spread it to other connected nodes
     */
    void updateAndSpreadNodeStatus(ClusterNode node);

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
