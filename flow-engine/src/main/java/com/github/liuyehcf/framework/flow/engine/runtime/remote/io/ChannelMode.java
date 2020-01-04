package com.github.liuyehcf.framework.flow.engine.runtime.remote.io;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public enum ChannelMode {

    /**
     * client side of channel
     */
    client,

    /**
     * server side of channel
     */
    server;

    public boolean isServerMode() {
        return ChannelMode.server.equals(this);
    }

    public boolean isClientMode() {
        return ChannelMode.client.equals(this);
    }
}
