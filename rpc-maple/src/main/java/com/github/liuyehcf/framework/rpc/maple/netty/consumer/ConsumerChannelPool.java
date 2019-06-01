package com.github.liuyehcf.framework.rpc.maple.netty.consumer;

import com.github.liuyehcf.framework.rpc.maple.MapleException;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceAddress;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author chenlu
 * @date 2019/3/27
 */
public abstract class ConsumerChannelPool {

    private static final Map<ServiceAddress, Channel> channelPool = Maps.newConcurrentMap();

    public static void addChannel(final ServiceAddress serviceAddress, final Callable<Channel> callable) {
        if (!channelPool.containsKey(serviceAddress)) {
            synchronized (ConsumerChannelPool.class) {
                if (!channelPool.containsKey(serviceAddress)) {
                    final Channel channel;
                    try {
                        channel = callable.call();
                    } catch (Throwable e) {
                        throw new MapleException(MapleException.Code.IO, e);
                    }
                    channelPool.put(serviceAddress, channel);
                    channel.closeFuture().addListener((ChannelFuture future) -> channelPool.remove(serviceAddress));
                }
            }
        }
    }

    public static Channel getChannel(final ServiceAddress serviceAddress) {
        return channelPool.get(serviceAddress);
    }

}
