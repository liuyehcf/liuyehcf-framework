package com.github.liuyehcf.framework.io.athena.protocol;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.util.ByteUtils;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class AthenaFrame extends DefaultByteBufHolder {

    /**
     * protocol version
     * 1 byte
     */
    private final int version;

    /**
     * header length
     * 1 byte
     */
    private final int headerLength;

    /**
     * total length, including header length and payload length
     * 2 bytes
     */
    private final int totalLength;

    /**
     * whether has next frame
     * 0 means no next frame
     * 1 means has next frame
     * 1 byte
     */
    private final int hasNextFrame;

    private AthenaFrame(int version, int headerLength, int totalLength, int hasNextFrame, ByteBuf payload) {
        super(payload);
        this.version = version;
        this.headerLength = headerLength;
        this.totalLength = totalLength;
        this.hasNextFrame = hasNextFrame;
    }

    /**
     * wrap bytes to Frames
     */
    public static List<AthenaFrame> wrap(byte[] bytes) {
        Assert.assertNotNull(bytes, "bytes");
        if (bytes.length <= ProtocolConstant.MAX_PAYLOAD_LENGTH) {
            return Lists.newArrayList(wrap(ProtocolConstant.HAS_NOT_NEXT_FRAME, Unpooled.wrappedBuffer(bytes)));
        } else {
            List<AthenaFrame> athenaFrames = Lists.newArrayList();

            int totalFrame = (int) Math.ceil(1.0 * bytes.length / ProtocolConstant.MAX_PAYLOAD_LENGTH);
            int remainLength = bytes.length;

            for (int frameIndex = 0; frameIndex < totalFrame; frameIndex++) {
                int hasNextFrame = (frameIndex == totalFrame - 1 ? ProtocolConstant.HAS_NOT_NEXT_FRAME : ProtocolConstant.HAS_NEXT_FRAME);
                if (remainLength < ProtocolConstant.MAX_PAYLOAD_LENGTH) {
                    ByteBuf payload = Unpooled.wrappedBuffer(bytes, frameIndex * ProtocolConstant.MAX_PAYLOAD_LENGTH, remainLength);
                    athenaFrames.add(wrap(hasNextFrame, payload));

                    remainLength -= remainLength;
                } else {
                    ByteBuf payload = Unpooled.wrappedBuffer(bytes, frameIndex * ProtocolConstant.MAX_PAYLOAD_LENGTH, ProtocolConstant.MAX_PAYLOAD_LENGTH);
                    athenaFrames.add(wrap(hasNextFrame, payload));

                    remainLength -= ProtocolConstant.MAX_PAYLOAD_LENGTH;
                }
            }

            return athenaFrames;
        }
    }

    private static AthenaFrame wrap(int hasNextFrame, ByteBuf payload) {
        int headerLength = ProtocolConstant.MIN_HEADER_LENGTH;

        int totalLength = headerLength + payload.readableBytes();

        return new AthenaFrame(ProtocolConstant.PROTOCOL_VERSION, headerLength, totalLength, hasNextFrame, payload);
    }

    /**
     * deserialize from protocol bytes
     */
    public static AthenaFrame deserialize(ByteBuf byteBuf) {
        Assert.assertTrue(byteBuf.readableBytes() >= 4);

        int version = ByteUtils.toInt(byteBuf.readByte());
        int headerLength = ByteUtils.toInt(byteBuf.readByte());

        int totalLength = ByteUtils.toInt(byteBuf.readByte(), byteBuf.readByte());
        int hasNextFrame = ByteUtils.toInt(byteBuf.readByte());

        int payloadLength = totalLength - headerLength;
        ByteBuf payload = byteBuf.readBytes(payloadLength);

        return new AthenaFrame(version, headerLength, totalLength, hasNextFrame, payload);
    }

    public int getVersion() {
        return version;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public boolean hasNextFrame() {
        return ProtocolConstant.HAS_NEXT_FRAME == hasNextFrame;
    }

    /**
     * serialize to protocol bytes
     */
    public ByteBuf serialize() {
        ByteBuf byteBuf = Unpooled.buffer();

        // version
        byteBuf.writeByte(ByteUtils.toByte(version));

        // headerLength
        byteBuf.writeByte(ByteUtils.toByte(headerLength));

        // totalLength
        byte[] tmpBytes = ByteUtils.toBytes(totalLength, 2);
        byteBuf.writeBytes(tmpBytes);

        // hasNextFrame
        byteBuf.writeByte(ByteUtils.toByte(hasNextFrame));

        // payload
        byteBuf.writeBytes(content());

        return byteBuf;
    }
}
