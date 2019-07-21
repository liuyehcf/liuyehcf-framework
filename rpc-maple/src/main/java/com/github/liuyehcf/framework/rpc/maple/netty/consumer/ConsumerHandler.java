package com.github.liuyehcf.framework.rpc.maple.netty.consumer;

import com.github.liuyehcf.framework.rpc.maple.netty.MaplePacket;
import com.github.liuyehcf.framework.rpc.maple.netty.ResponseQueue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author hechenfeng
 * @date 2019/3/18
 */
public class ConsumerHandler extends SimpleChannelInboundHandler<MaplePacket> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MaplePacket msg) {
        final MaplePacket.Header header = msg.getHeader();

        final long requestId = header.getRequestId();

        ResponseQueue.putResponse(ctx.channel(), requestId, msg);
    }
}
