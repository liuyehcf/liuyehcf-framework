package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class FrameChunkedWriteHandler extends MessageToMessageEncoder<Package> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Package msg, List<Object> out) {
        out.addAll(Frame.wrap(msg.serialize()));
    }
}
