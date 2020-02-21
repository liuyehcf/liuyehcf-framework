package com.github.liuyehcf.framework.io.athena.protocol.handler;

import com.github.liuyehcf.framework.io.athena.protocol.AthenaFrame;
import com.github.liuyehcf.framework.io.athena.util.ByteBufUtils;
import com.github.liuyehcf.framework.io.athena.util.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class SerializeHandler extends MessageToMessageCodec<AthenaFrame, Object> {

    private ByteBuf cache;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        cache = ctx.alloc().directBuffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        ReferenceCountUtil.release(cache);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) {
        out.addAll(AthenaFrame.wrap(SerializeUtils.hessianSerialize(msg)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, AthenaFrame msg, List<Object> out) {
        cache.writeBytes(msg.content());
        if (msg.hasNextFrame()) {
            return;
        }

        try {
            byte[] bytes = ByteBufUtils.toByteArray(cache);
            out.add(SerializeUtils.hessianDeserialize(bytes));
        } finally {
            ReferenceCountUtil.release(cache);
        }
        cache = ctx.alloc().directBuffer();
    }
}
