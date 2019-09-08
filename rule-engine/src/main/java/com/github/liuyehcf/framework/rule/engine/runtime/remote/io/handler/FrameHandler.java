package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.handler;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.ProtocolConstant;
import com.github.liuyehcf.framework.rule.engine.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class FrameHandler extends ByteToMessageCodec<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf out) {
        out.writeBytes(msg.serialize());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() >= ProtocolConstant.MIN_HEADER_LENGTH) {
            final int originReaderIndex = in.readerIndex();

            in.setIndex(originReaderIndex + ProtocolConstant.TOTAL_LENGTH_OFFSET, in.writerIndex());
            int totalLength = ByteUtils.toInt(in.readByte(), in.readByte());

            // now readable index is point at options, so remaining readableBytes + ProtocolConstant.MIN_HEADER_LENGTH = totalLength
            if (in.readableBytes() + ProtocolConstant.MIN_HEADER_LENGTH < totalLength) {
                in.setIndex(originReaderIndex, in.writerIndex());
                break;
            }

            // reset readable index to read all frame bytes
            in.setIndex(originReaderIndex, in.writerIndex());

            final byte[] frameBytes = new byte[totalLength];
            in.readBytes(frameBytes);

            out.add(Frame.deserialize(frameBytes));
        }
    }
}
