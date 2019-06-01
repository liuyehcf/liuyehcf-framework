package com.github.liuyehcf.framework.rpc.maple.netty;

import com.github.liuyehcf.framework.rpc.maple.MapleException;
import com.github.liuyehcf.framework.rpc.maple.util.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author chenlu
 * @date 2019/3/24
 */
@Data
public class DefaultMaplePacket implements MaplePacket {

    private Header header;

    private byte[] body;

    public DefaultMaplePacket(Header header, byte[] body) {
        Assert.assertNotNull(header);
        Assert.assertNotNull(body);
        Assert.assertEquals(header.getBodyLength(), body.length);

        this.header = header;
        this.body = body;
    }

    private static int bytesToInt(final byte[] bytes, final int offset) {
        int b0 = bytes[offset] & 0xFF;
        int b1 = bytes[offset + 1] & 0xFF;
        int b2 = bytes[offset + 2] & 0xFF;
        int b3 = bytes[offset + 3] & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }

    private static long bytesToLong(final byte[] bytes, final int offset) {
        long b0 = bytes[offset] & 0xFF;
        long b1 = bytes[offset + 1] & 0xFF;
        long b2 = bytes[offset + 2] & 0xFF;
        long b3 = bytes[offset + 3] & 0xFF;
        long b4 = bytes[offset + 4] & 0xFF;
        long b5 = bytes[offset + 5] & 0xFF;
        long b6 = bytes[offset + 6] & 0xFF;
        long b7 = bytes[offset + 7] & 0xFF;
        return (b0 << 56) | (b1 << 48) | (b2 << 40) | (b3 << 32)
                | (b4 << 24) | (b5 << 16) | (b6 << 8) | b7;
    }

    public static byte[] serialize(final Header header, final byte[] body) {
        return new DefaultMaplePacket(header, body).serialize();
    }

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public byte[] serialize() {
        try (final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
             final DataOutputStream dataOutputStream = new DataOutputStream(byteOutputStream)) {

            dataOutputStream.write(header.serialize());
            dataOutputStream.write(body);

            dataOutputStream.flush();
            return byteOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MapleException(MapleException.Code.IO, e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class DefaultHeader implements Header {

        private long requestId;

        private byte serializeType;

        private byte status;

        private int bodyLength;

        public static Header parse(final byte[] bytes) {
            Assert.assertTrue(bytes.length >= HEADER_LENGTH);

            final long requestId = bytesToLong(bytes, 0);
            final byte serializeType = bytes[8];
            final byte status = bytes[9];
            final int bodyLength = bytesToInt(bytes, 10);

            return new DefaultHeader(requestId, serializeType, status, bodyLength);
        }

        @Override
        public byte getSerializeType() {
            return serializeType;
        }

        @Override
        public int getBodyLength() {
            return bodyLength;
        }

        @Override
        public byte[] serialize() {
            try (final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                 final DataOutputStream dataOutputStream = new DataOutputStream(byteOutputStream)) {

                dataOutputStream.writeLong(requestId);
                dataOutputStream.writeByte(serializeType);
                dataOutputStream.writeByte(status);
                dataOutputStream.writeInt(bodyLength);

                dataOutputStream.flush();
                return byteOutputStream.toByteArray();
            } catch (IOException e) {
                throw new MapleException(MapleException.Code.IO, e);
            }
        }
    }
}
