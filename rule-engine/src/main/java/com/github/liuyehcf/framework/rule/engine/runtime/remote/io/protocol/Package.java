package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.util.ByteUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class Package {

    /**
     * type of payload
     * 1 byte
     */
    private final int type;

    /**
     * type of serialization
     */
    private final int serializeType;

    /**
     * payload
     */
    private final byte[] payload;

    private Package(int type, int serializeType, byte[] payload) {
        this.type = type;
        this.serializeType = serializeType;
        this.payload = payload;
    }

    public static Package wrap(int type, int serializeType, byte[] payload) {
        return new Package(type, serializeType, payload);
    }

    public static Package deserialize(byte[] bytes) {
        Assert.assertNotNull(bytes, "bytes");
        Assert.assertTrue(bytes.length > 0);

        int offset = 0;

        int type = ByteUtils.toInt(bytes[offset++]);
        int serializeType = ByteUtils.toInt(bytes[offset++]);

        byte[] payload = new byte[bytes.length - offset];

        System.arraycopy(bytes, offset, payload, 0, payload.length);

        return new Package(type, serializeType, payload);
    }

    public byte[] serialize() {
        int payloadLength;
        if (ArrayUtils.isEmpty(payload)) {
            payloadLength = 0;
        } else {
            payloadLength = payload.length;
        }

        byte[] bytes = new byte[2 + payload.length];

        bytes[0] = ByteUtils.toByte(type);
        bytes[1] = ByteUtils.toByte(serializeType);
        System.arraycopy(payload, 0, bytes, 2, payloadLength);

        return bytes;
    }

    public int getType() {
        return type;
    }

    public int getSerializeType() {
        return serializeType;
    }

    public byte[] getPayload() {
        return payload;
    }
}
