package com.github.liuyehcf.framework.rpc.maple.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/3/24
 */
public class MaplePacketHandler extends ByteToMessageCodec<MaplePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MaplePacket msg, ByteBuf out) {
        out.writeBytes(msg.serialize());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() >= MaplePacket.Header.HEADER_LENGTH) {
            final int originReaderIndex = in.readerIndex();

            final byte[] headerBytes = new byte[MaplePacket.Header.HEADER_LENGTH];
            in.readBytes(headerBytes);

            final MaplePacket.Header header = DefaultMaplePacket.DefaultHeader.parse(headerBytes);

            if (in.readableBytes() < header.getBodyLength()) {
                in.setIndex(originReaderIndex, in.writerIndex());
                break;
            }

            final byte[] body = new byte[header.getBodyLength()];
            in.readBytes(body);

            out.add(new DefaultMaplePacket(header, body));
        }
    }
}
