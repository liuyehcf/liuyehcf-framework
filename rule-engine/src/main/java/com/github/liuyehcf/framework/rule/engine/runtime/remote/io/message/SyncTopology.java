package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Topology;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public class SyncTopology implements Message {

    private final Topology topology;

    public SyncTopology(Topology topology) {
        this.topology = topology;
    }

    @Override
    public final MessageType getType() {
        return MessageType.SYNC_TOPOLOGY;
    }

    public Topology getTopology() {
        return topology;
    }
}
