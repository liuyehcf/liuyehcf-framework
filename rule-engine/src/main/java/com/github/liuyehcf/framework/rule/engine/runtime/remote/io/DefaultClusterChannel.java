package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public class DefaultClusterChannel implements ClusterChannel {

    private final String peerIdentifier;
    private final Channel channel;
    private final ChannelMode channelMode;

    public DefaultClusterChannel(String peerIdentifier, Channel channel, ChannelMode channelMode) {
        this.peerIdentifier = peerIdentifier;
        this.channel = channel;
        this.channelMode = channelMode;
    }

    @Override
    public final String getPeerIdentifier() {
        return peerIdentifier;
    }

    @Override
    public final Channel getChannel() {
        return channel;
    }

    @Override
    public final ChannelMode getChannelMode() {
        return channelMode;
    }

    @Override
    public final ChannelFuture write(Object msg) {
        return channel.writeAndFlush(msg);
    }

    @Override
    public final void closeAsync() {
        channel.close();
    }

    @Override
    public final void closeSync() throws InterruptedException {
        channel.close().sync();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultClusterChannel that = (DefaultClusterChannel) o;
        return Objects.equals(peerIdentifier, that.peerIdentifier) &&
                Objects.equals(channel, that.channel) &&
                channelMode == that.channelMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(peerIdentifier, channel, channelMode);
    }
}
