package com.github.liuyehcf.framework.rpc.maple.netty.provider;

import com.github.liuyehcf.framework.rpc.maple.netty.MaplePacketHandler;
import com.github.liuyehcf.framework.rpc.maple.netty.NettyConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hechenfeng
 * @date 2019/3/18
 */
public class ProviderLoop {

    private static final EventLoopGroup BOSS = new NioEventLoopGroup();
    private static final EventLoopGroup WORKER = new NioEventLoopGroup();

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    public static void boot() throws Exception {
        if (!INIT.compareAndSet(false, true)) {
            return;
        }

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(BOSS, WORKER);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                final ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(NettyConst.PACKET_HANDLER_NAME, new MaplePacketHandler());
                pipeline.addLast(NettyConst.PROVIDER_HANDLER_NAME, new ProviderHandler());
            }
        });

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        bootstrap.bind(ProviderAddress.getServiceAddress().getPort()).sync();
    }
}
