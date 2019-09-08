package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public enum MessageType {

    /**
     * heart beat
     */
    HEART_BEAT(128),

    /**
     * greet which must be the first message of the client of communication pair
     */
    GREET(0),

    /**
     * cluster node joining cluster through seed node
     */
    JOINING(1),

    /**
     * spread node status to other nodes in the cluster
     */
    SPREAD_NODE_STATUS(2),

    /**
     * cluster topology
     */
    CLUSTER_TOPOLOGY(3);

    private final int type;

    MessageType(int type) {
        this.type = type;
    }

    public static MessageType typeOf(int type) {
        for (MessageType messageType : values()) {
            if (messageType.getType() == type) {
                return messageType;
            }
        }
        throw new UnsupportedOperationException(String.format("Unsupported message type='%s'", type));
    }

    public int getType() {
        return type;
    }
}
