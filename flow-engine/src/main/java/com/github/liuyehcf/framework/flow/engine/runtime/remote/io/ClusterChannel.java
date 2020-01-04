package com.github.liuyehcf.framework.flow.engine.runtime.remote.io;


import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.Identifier;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public interface ClusterChannel {

    /**
     * get cluster member's identifier on the other side
     */
    Identifier getPeerIdentifier();

    /**
     * netty channel
     */
    Channel getChannel();

    /**
     * channel mode
     */
    ChannelMode getChannelMode();

    /**
     * write msg
     */
    ChannelFuture write(Object msg);

    /**
     * close netty async
     */
    void closeAsync();

    /**
     * close netty sync
     */
    void closeSync() throws InterruptedException;
}
