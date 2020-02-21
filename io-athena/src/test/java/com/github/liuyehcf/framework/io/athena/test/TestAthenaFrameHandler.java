package com.github.liuyehcf.framework.io.athena.test;

import com.github.liuyehcf.framework.io.athena.protocol.AthenaFrame;
import com.github.liuyehcf.framework.io.athena.protocol.ProtocolConstant;
import com.github.liuyehcf.framework.io.athena.protocol.handler.AthenaFrameHandler;
import com.github.liuyehcf.framework.io.athena.util.ByteUtils;
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
 * @date 2020/2/6
 */
public class TestAthenaFrameHandler extends TestBase {

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
        EmbeddedChannel channel = new EmbeddedChannel(new AthenaFrameHandler());

        // mock input through writeInbound
        List<AthenaFrame> athenaFrames = AthenaFrame.wrap(bytes);
        Assert.assertEquals(expectedFrameNum, athenaFrames.size());
        for (int i = 0; i < expectedFrameNum; i++) {
            AthenaFrame athenaFrame = athenaFrames.get(i);
            // save index of payload
            ByteBuf payload = athenaFrame.content();
            int originalReaderIndex = payload.readerIndex();
            int originalWriterIndex = payload.writerIndex();

            // serialize will modify the index of payload
            ByteBuf serialize = athenaFrame.serialize();
            channel.writeInbound(serialize);

            // recover index of payload
            payload.setIndex(originalReaderIndex, originalWriterIndex);
        }
        channel.flush();

        // read data from inbound
        List<AthenaFrame> receivedAthenaFrames = Lists.newArrayList();
        for (int i = 0; i < expectedFrameNum; i++) {
            receivedAthenaFrames.add(channel.readInbound());
        }
        Assert.assertNull(channel.readInbound());

        for (int i = 0; i < expectedFrameNum; i++) {
            assertFrameEquals(athenaFrames.get(i), receivedAthenaFrames.get(i));
            ReferenceCountUtil.release(athenaFrames.get(i));
            ReferenceCountUtil.release(receivedAthenaFrames.get(i));
        }
    }

    private void testOutbound(byte[] bytes, int expectedFrameNum) {
        EmbeddedChannel channel = new EmbeddedChannel(new AthenaFrameHandler());

        // mock output through writeOutbound
        List<AthenaFrame> athenaFrames = AthenaFrame.wrap(bytes);
        Assert.assertEquals(expectedFrameNum, athenaFrames.size());
        for (int i = 0; i < expectedFrameNum; i++) {
            AthenaFrame athenaFrame = athenaFrames.get(i);
            athenaFrame.retain();

            // save index of payload
            ByteBuf payload = athenaFrame.content();
            int originalReaderIndex = payload.readerIndex();
            int originalWriterIndex = payload.writerIndex();

            // serialize will modify the index of payload
            channel.writeOutbound(athenaFrame);
            channel.flush();

            // recover index of payload
            payload.setIndex(originalReaderIndex, originalWriterIndex);
        }

        ByteBuf cache = Unpooled.buffer();
        ByteBuf tmpByteBuf;
        while ((tmpByteBuf = channel.readOutbound()) != null) {
            cache.writeBytes(tmpByteBuf);
            ReferenceCountUtil.release(tmpByteBuf);
        }

        List<AthenaFrame> receivedAthenaFrames = Lists.newArrayList();

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

            receivedAthenaFrames.add(AthenaFrame.deserialize(cache));
        }

        ReferenceCountUtil.release(cache);

        Assert.assertEquals(expectedFrameNum, receivedAthenaFrames.size());

        for (int i = 0; i < expectedFrameNum; i++) {
            assertFrameEquals(athenaFrames.get(i), receivedAthenaFrames.get(i));
        }

        for (AthenaFrame athenaFrame : receivedAthenaFrames) {
            ReferenceCountUtil.release(athenaFrame);
        }
    }
}
