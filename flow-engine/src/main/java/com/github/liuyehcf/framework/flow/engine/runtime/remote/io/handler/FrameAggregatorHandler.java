package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Package;
import com.github.liuyehcf.framework.flow.engine.util.ByteBufUtils;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class FrameAggregatorHandler extends MessageToMessageDecoder<Frame> {

    private int total;
    private int index;
    private List<Frame> cache = Lists.newArrayList();

    @Override
    protected void decode(ChannelHandlerContext ctx, Frame msg, List<Object> out) {
        Assert.assertEquals(index, msg.getFrameIndex());

        if (index == 0) {
            total = msg.getTotalFrame();
        } else {
            Assert.assertEquals(total, msg.getTotalFrame());
        }

        cache.add(msg);

        if (++index < total) {
            return;
        }

        ByteBuf buf = Unpooled.buffer();
        try {
            for (Frame frame : cache) {
                buf.writeBytes(frame.getPayload());
            }
            out.add(Package.deserialize(ByteBufUtils.toByteArray(buf)));
        } finally {
            ReferenceCountUtil.release(buf);
            cache.clear();

            index = 0;
            total = -1;
        }
    }
}
