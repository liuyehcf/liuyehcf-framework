package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

/**
 * @author hechenfeng
 * @date 2019/9/10
 */
public class SyncTopologyRequest implements Message {

    @Override
    public final MessageType getType() {
        return MessageType.SYNC_TOPOLOGY_REQUEST;
    }
}
