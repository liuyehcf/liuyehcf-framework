package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterTopology;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public class ClusterTopologyMessage implements Message {

    private final ClusterTopology topology;

    public ClusterTopologyMessage(ClusterTopology topology) {
        this.topology = topology;
    }

    @Override
    public MessageType getType() {
        return MessageType.CLUSTER_TOPOLOGY;
    }

    public ClusterTopology getTopology() {
        return topology;
    }
}
