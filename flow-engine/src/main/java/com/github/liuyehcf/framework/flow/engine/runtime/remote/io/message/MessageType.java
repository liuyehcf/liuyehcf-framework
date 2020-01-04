package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.message;

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
     * cluster member joining cluster through seed member
     */
    JOINING(1),

    /**
     * spread member status to other members in the cluster
     */
    SYNC_MEMBER(2),

    /**
     * cluster topology
     */
    SYNC_TOPOLOGY(3),

    /**
     * elect as a leader
     */
    ELECTION(4),

    /**
     * vote for leader
     */
    VOTE(7),

    /**
     * leader of cluster has been selected
     */
    ELECTION_RESULT(8),

    /**
     * ask leader to send topology
     */
    SYNC_TOPOLOGY_REQUEST(9),

    /**
     * ask whether leader is reachable
     */
    LEADER_STATUS_REQUEST(10),

    /**
     * response whether leader is reachable
     */
    LEADER_STATUS_RESPONSE(11),

    /**
     * one leader leave office and direct recommend another leader
     */
    LEADER_RECOMMENDATION(12),

    /**
     * if one leader is lose in leadership collide, then the lose side will sent compensate member to the peer
     */
    COMPENSATE_MEMBERS(13),
    ;

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
