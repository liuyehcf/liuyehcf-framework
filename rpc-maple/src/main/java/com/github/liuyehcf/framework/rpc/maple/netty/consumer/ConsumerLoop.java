package com.github.liuyehcf.framework.rpc.maple.netty.consumer;

import com.github.liuyehcf.framework.rpc.maple.MapleException;
import com.github.liuyehcf.framework.rpc.maple.netty.MaplePacketHandler;
import com.github.liuyehcf.framework.rpc.maple.netty.NettyConst;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author chenlu
 * @date 2019/3/18
 */
public abstract class ConsumerLoop {

    private static final EventLoopGroup WORKER = new NioEventLoopGroup();

    public static Channel connect(final ServiceAddress serviceAddress) {
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(WORKER);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                final ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(NettyConst.PACKET_HANDLER_NAME, new MaplePacketHandler());
                pipeline.addLast(NettyConst.CONSUMER_HANDLER_NAME, new ConsumerHandler());
            }
        });

        try {
            return bootstrap.connect(new InetSocketAddress(serviceAddress.getHost(), serviceAddress.getPort())).sync().channel();
        } catch (InterruptedException e) {
            throw new MapleException(MapleException.Code.IO, e);
        }
    }
}
