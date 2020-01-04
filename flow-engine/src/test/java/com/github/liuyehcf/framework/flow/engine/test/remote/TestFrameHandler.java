package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler.FrameHandler;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.ProtocolConstant;
import com.github.liuyehcf.framework.flow.engine.util.ByteUtils;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
@SuppressWarnings("all")
public class TestFrameHandler extends TestRemoteBase {

    @Test
    public void testInboundBoundary() {
        testInbound(getByteArray(0), 1);
        testInbound(getByteArray(1), 1);
        testInbound(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH), 1);
    }

    @Test
    public void testInboundTwoFrame() {
        testInbound(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH + 1), 2);
        testInbound(getByteArray(2 * ProtocolConstant.MAX_PAYLOAD_LENGTH), 2);
    }

    @Test
    public void testInboundRandom() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);

            int len = RANDOM.nextInt(ProtocolConstant.MAX_PAYLOAD_LENGTH * 100);

            int numFrame = (int) Math.ceil(1.0 * len / ProtocolConstant.MAX_PAYLOAD_LENGTH);

            testInbound(getByteArray(len), numFrame);
        }
    }

    @Test
    public void testOutboundBoundary() {
        testOutbound(getByteArray(0), 1);
        testOutbound(getByteArray(1), 1);
        testOutbound(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH), 1);
    }

    @Test
    public void testOutboundTwoFrame() {
        testOutbound(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH + 1), 2);
        testOutbound(getByteArray(2 * ProtocolConstant.MAX_PAYLOAD_LENGTH), 2);
    }

    @Test
    public void testOutboundRandom() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);

            int len = RANDOM.nextInt(ProtocolConstant.MAX_PAYLOAD_LENGTH * 100);

            int numFrame = (int) Math.ceil(1.0 * len / ProtocolConstant.MAX_PAYLOAD_LENGTH);

            testOutbound(getByteArray(len), numFrame);
        }
    }

    private void testInbound(byte[] bytes, int expectedFrameNum) {
        EmbeddedChannel channel = new EmbeddedChannel(new FrameHandler());

        List<Frame> frames = Frame.wrap(bytes);
        Assert.assertEquals(expectedFrameNum, frames.size());

        for (int i = 0; i < expectedFrameNum; i++) {
            channel.writeInbound(Unpooled.wrappedBuffer(frames.get(i).serialize()));
        }
        channel.flush();

        List<Frame> receivedFrames = Lists.newArrayList();
        for (int i = 0; i < expectedFrameNum; i++) {
            receivedFrames.add(channel.readInbound());
        }
        Assert.assertNull(channel.readInbound());

        for (int i = 0; i < expectedFrameNum; i++) {
            assertFrameEquals(frames.get(i), receivedFrames.get(i));
        }
    }

    private void testOutbound(byte[] bytes, int expectedFrameNum) {
        EmbeddedChannel channel = new EmbeddedChannel(new FrameHandler());

        List<Frame> frames = Frame.wrap(bytes);
        Assert.assertEquals(expectedFrameNum, frames.size());

        for (int i = 0; i < expectedFrameNum; i++) {
            channel.writeOutbound(frames.get(i));
        }
        channel.flush();

        ByteBuf cache = Unpooled.buffer();

        ByteBuf byteBuf;
        while ((byteBuf = channel.readOutbound()) != null) {
            try {
                cache.writeBytes(byteBuf);
            } finally {
                ReferenceCountUtil.release(byteBuf);
            }
        }

        List<Frame> receivedFrames = Lists.newArrayList();

        while (cache.readableBytes() >= ProtocolConstant.MIN_HEADER_LENGTH) {
            final int originReaderIndex = cache.readerIndex();

            cache.setIndex(originReaderIndex + ProtocolConstant.TOTAL_LENGTH_OFFSET, cache.writerIndex());
            int totalLength = ByteUtils.toInt(cache.readByte(), cache.readByte());

            // now readable index is point at options, so remaining readableBytes + ProtocolConstant.MIN_HEADER_LENGTH = totalLength
            if (cache.readableBytes() + ProtocolConstant.MIN_HEADER_LENGTH < totalLength) {
                cache.setIndex(originReaderIndex, cache.writerIndex());
                break;
            }

            // reset readable index to read all frame bytes
            cache.setIndex(originReaderIndex, cache.writerIndex());

            final byte[] frameBytes = new byte[totalLength];
            cache.readBytes(frameBytes);

            receivedFrames.add(Frame.deserialize(frameBytes));
        }

        ReferenceCountUtil.release(cache);

        Assert.assertEquals(expectedFrameNum, receivedFrames.size());

        for (int i = 0; i < expectedFrameNum; i++) {
            assertFrameEquals(frames.get(i), receivedFrames.get(i));
        }
    }
}
