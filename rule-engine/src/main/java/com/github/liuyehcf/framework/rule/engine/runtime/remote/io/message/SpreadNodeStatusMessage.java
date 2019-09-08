package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNode;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public class SpreadNodeStatusMessage implements Message {

    private final ClusterNode node;

    public SpreadNodeStatusMessage(ClusterNode node) {
        this.node = node;
    }

    @Override
    public MessageType getType() {
        return MessageType.SPREAD_NODE_STATUS;
    }

    public ClusterNode getNode() {
        return node;
    }
}
