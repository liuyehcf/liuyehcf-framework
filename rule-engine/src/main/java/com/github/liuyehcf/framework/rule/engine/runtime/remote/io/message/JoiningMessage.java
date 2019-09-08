package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNode;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class JoiningMessage implements Message {

    private final ClusterNode node;

    public JoiningMessage(ClusterNode node) {
        this.node = node;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOINING;
    }

    public ClusterNode getNode() {
        return node;
    }
}
