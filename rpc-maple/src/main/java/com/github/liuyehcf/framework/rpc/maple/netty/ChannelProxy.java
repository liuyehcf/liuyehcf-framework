package com.github.liuyehcf.framework.rpc.maple.netty;

import com.github.liuyehcf.framework.rpc.maple.register.ServiceInstance;
import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author hechenfeng
 * @date 2019/3/25
 */
@Data
public class ChannelProxy {

    private volatile Channel channel;

    private volatile ServiceInstance serviceInstance;

    public void bind(final ServiceInstance serviceInstance, final Channel channel) {
        ResponseQueue.createChannelQueue(channel);
        this.serviceInstance = serviceInstance;
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void close() {
        ResponseQueue.removeChannelQueue(channel);
        serviceInstance = null;
        channel = null;
    }

}
