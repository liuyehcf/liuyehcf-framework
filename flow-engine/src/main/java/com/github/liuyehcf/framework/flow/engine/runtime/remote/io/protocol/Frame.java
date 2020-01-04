package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.util.ByteUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/9/4
 */
public class Frame {

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
     * options
     * There are only 256 bytes in the head
     * 256 - 4 = 252 bytes at most
     */
    private final List<Option> options;

    /**
     * payload
     */
    private final byte[] payload;

    private Frame(int version, int headerLength, int totalLength, List<Option> options, byte[] payload) {
        this.version = version;
        this.headerLength = headerLength;
        this.totalLength = totalLength;
        this.options = options;
        this.payload = payload;
    }

    /**
     * wrap payload to Frames
     */
    public static List<Frame> wrap(byte[] payload) {
        if (ArrayUtils.isEmpty(payload)
                || payload.length <= ProtocolConstant.MAX_PAYLOAD_LENGTH) {
            return Lists.newArrayList(wrap(payload, 1, 0));
        } else {
            List<Frame> frames = Lists.newArrayList();

            int totalFrame = (int) Math.ceil(1.0 * payload.length / ProtocolConstant.MAX_PAYLOAD_LENGTH);
            int remainLength = payload.length;

            for (int frameIndex = 0; frameIndex < totalFrame; frameIndex++) {
                if (remainLength < ProtocolConstant.MAX_PAYLOAD_LENGTH) {
                    byte[] subPayload = new byte[remainLength];
                    System.arraycopy(payload, frameIndex * ProtocolConstant.MAX_PAYLOAD_LENGTH, subPayload, 0, remainLength);
                    frames.add(wrap(subPayload, totalFrame, frameIndex));

                    remainLength -= remainLength;
                } else {
                    byte[] subPayload = new byte[ProtocolConstant.MAX_PAYLOAD_LENGTH];
                    System.arraycopy(payload, frameIndex * ProtocolConstant.MAX_PAYLOAD_LENGTH, subPayload, 0, ProtocolConstant.MAX_PAYLOAD_LENGTH);
                    frames.add(wrap(subPayload, totalFrame, frameIndex));

                    remainLength -= ProtocolConstant.MAX_PAYLOAD_LENGTH;
                }
            }

            return frames;
        }
    }

    private static Frame wrap(byte[] payload, int totalFrame, int frameIndex) {
        List<Option> options = Lists.newArrayList();
        options.add(new Option(OptionType.TOTAL_FRAME, new byte[]{ByteUtils.toByte(totalFrame)}));
        options.add(new Option(OptionType.FRAME_INDEX, new byte[]{ByteUtils.toByte(frameIndex)}));

        int headerLength = ProtocolConstant.MIN_HEADER_LENGTH
                + (ProtocolConstant.MIN_OPTION_LENGTH + OptionType.TOTAL_FRAME.getLength())
                + (ProtocolConstant.MIN_OPTION_LENGTH + OptionType.FRAME_INDEX.getLength());

        int payloadLength;

        if (ArrayUtils.isEmpty(payload)) {
            payloadLength = 0;
        } else {
            Assert.assertTrue(payload.length <= ProtocolConstant.MAX_PAYLOAD_LENGTH);
            payloadLength = payload.length;
        }

        int totalLength = headerLength + payloadLength;

        return new Frame(ProtocolConstant.PROTOCOL_VERSION, headerLength, totalLength, options, payload);
    }

    /**
     * deserialize from protocol bytes
     */
    public static Frame deserialize(byte[] bytes) {
        Assert.assertTrue(bytes.length >= 4);
        int offset = 0;

        int version = ByteUtils.toInt(bytes[offset++]);
        int headerLength = ByteUtils.toInt(bytes[offset++]);

        int totalLength = ByteUtils.toInt(bytes[offset++], bytes[offset++]);

        List<Option> options = Lists.newArrayList();
        while (headerLength > offset) {
            int optType = ByteUtils.toInt(bytes[offset++]);
            int optLength = ByteUtils.toInt(bytes[offset++]);
            byte[] optValue = new byte[optLength];
            System.arraycopy(bytes, offset, optValue, 0, optLength);
            offset += optLength;

            options.add(new Option(OptionType.typeOf(optType), optValue));
        }

        int payloadLength = totalLength - headerLength;
        byte[] payload = new byte[payloadLength];
        System.arraycopy(bytes, offset, payload, 0, payloadLength);

        return new Frame(version, headerLength, totalLength, options, payload);
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

    public List<Option> getOptions() {
        return options;
    }

    public byte[] getPayload() {
        return payload;
    }

    public int getTotalFrame() {
        Option option = getOption(OptionType.TOTAL_FRAME);
        Assert.assertNotNull(option, "option 'TOTAL_FRAME'");

        return ByteUtils.toInt(option.getValue()[0]);
    }

    public int getFrameIndex() {
        Option option = getOption(OptionType.FRAME_INDEX);
        Assert.assertNotNull(option, "option 'FRAME_INDEX'");

        return ByteUtils.toInt(option.getValue()[0]);
    }

    private Option getOption(OptionType optionType) {
        for (Option option : options) {
            if (Objects.equals(option.getType(), optionType)) {
                return option;
            }
        }
        return null;
    }

    /**
     * serialize to protocol bytes
     */
    public byte[] serialize() {
        byte[] bytes = new byte[totalLength];
        int offset = 0;

        // version
        bytes[offset++] = ByteUtils.toByte(version);

        // header len
        bytes[offset++] = ByteUtils.toByte(headerLength);

        // total len
        byte[] tmpBytes = ByteUtils.toBytes(totalLength, 2);
        System.arraycopy(tmpBytes, 0, bytes, offset, 2);
        offset += 2;

        // options
        if (CollectionUtils.isNotEmpty(options)) {
            for (Option option : options) {
                OptionType type = option.getType();

                bytes[offset++] = ByteUtils.toByte(type.getType());

                bytes[offset++] = ByteUtils.toByte(type.getLength());

                if (type.getLength() > 0) {
                    System.arraycopy(option.getValue(), 0, bytes, offset, type.getLength());
                }

                offset += type.getLength();
            }
        }

        Assert.assertEquals(headerLength, offset);

        // payload
        if (ArrayUtils.isNotEmpty(payload)) {
            System.arraycopy(payload, 0, bytes, headerLength, payload.length);
        }

        return bytes;
    }
}
