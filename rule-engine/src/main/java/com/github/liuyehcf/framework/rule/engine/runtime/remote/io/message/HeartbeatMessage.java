package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class HeartbeatMessage implements Message {

    private final String nodeIdentifier;
    private final String clusterIdentifier;

    public HeartbeatMessage(String nodeIdentifier, String clusterIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
        this.clusterIdentifier = clusterIdentifier;
    }

    @Override
    public MessageType getType() {
        return MessageType.HEART_BEAT;
    }

    public String getNodeIdentifier() {
        return nodeIdentifier;
    }

    public String getClusterIdentifier() {
        return clusterIdentifier;
    }
}
