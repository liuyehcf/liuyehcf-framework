package com.github.liuyehcf.framework.io.athena.protocol.handler;

import com.github.liuyehcf.framework.io.athena.protocol.AthenaFrame;
import com.github.liuyehcf.framework.io.athena.protocol.ProtocolConstant;
import com.github.liuyehcf.framework.io.athena.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class AthenaFrameHandler extends ByteToMessageCodec<AthenaFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AthenaFrame msg, ByteBuf out) {
        ByteBuf payload = msg.serialize();
        try {
            out.writeBytes(payload);
        } finally {
            ReferenceCountUtil.release(payload);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() >= ProtocolConstant.MIN_HEADER_LENGTH) {
            final int originReaderIndex = in.readerIndex();

            in.setIndex(originReaderIndex + ProtocolConstant.TOTAL_LENGTH_OFFSET, in.writerIndex());
            int totalLength = ByteUtils.toInt(in.readByte(), in.readByte());

            // reset readable index to read all frame bytes
            in.setIndex(originReaderIndex, in.writerIndex());

            if (in.readableBytes() < totalLength) {
                break;
            }

            out.add(AthenaFrame.deserialize(in));
        }
    }
}
