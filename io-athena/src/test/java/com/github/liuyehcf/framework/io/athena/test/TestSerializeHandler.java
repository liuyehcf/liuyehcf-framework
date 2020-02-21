package com.github.liuyehcf.framework.io.athena.test;

import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.io.athena.protocol.AthenaFrame;
import com.github.liuyehcf.framework.io.athena.protocol.ProtocolConstant;
import com.github.liuyehcf.framework.io.athena.protocol.handler.AthenaFrameHandler;
import com.github.liuyehcf.framework.io.athena.protocol.handler.SerializeHandler;
import com.github.liuyehcf.framework.io.athena.util.ByteBufUtils;
import com.github.liuyehcf.framework.io.athena.util.SerializeUtils;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class TestSerializeHandler extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSerializeHandler.class);

    @Test
    public void testBoundary() {
        test(getByteArray(0));
        test(getByteArray(1));
        test(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH - 1));
        test(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH));
        test(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH + 1));
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);

            int len = RANDOM.nextInt(ProtocolConstant.MAX_PAYLOAD_LENGTH * 100);

            test(getByteArray(len));
        }
    }

    @Test
    public void testObject() {
        test(1);
        test("hello");
        test(Lists.newArrayList());
    }

    @Test
    public void testOutboundMemoryLeak() {
        for (int i = 0; i < 20000; i++) {
            LOGGER.error("{}M", i);
            EmbeddedChannel channel = new EmbeddedChannel();
            ChannelPipeline pipeline = channel.pipeline();

            pipeline.addLast(new AthenaFrameHandler());
            pipeline.addLast(new SerializeHandler());

            byte[] bytes = getByteArray(NumberUtils._1M);
            channel.writeOutbound(new Object[]{bytes});
            channel.flushOutbound();

            ByteBuf cache = Unpooled.buffer();
            ByteBuf buf;
            while ((buf = channel.readOutbound()) != null) {
                cache.writeBytes(buf);
                ReferenceCountUtil.release(buf);
            }

            channel.writeInbound(cache);
            channel.flushInbound();
            byte[] rebuildBytes = channel.readInbound();

            assertBytesEquals(bytes, rebuildBytes);

            channel.close();
        }
    }

    private void test(Object msg) {
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new AthenaFrameHandler());
        pipeline.addLast(new SerializeHandler());

        List<AthenaFrame> athenaFrames = AthenaFrame.wrap(SerializeUtils.hessianSerialize(msg));

        ByteBuf tcpCache = Unpooled.buffer();
        for (AthenaFrame athenaFrame : athenaFrames) {
            ByteBuf subByteBuf = athenaFrame.serialize();
            // save index of subByteBuf
            int originalReaderIndex = subByteBuf.readerIndex();
            int originalWriterIndex = subByteBuf.writerIndex();

            // this will modify index of subByteBuf
            tcpCache.writeBytes(subByteBuf);

            // recover index of subByteBuf
            subByteBuf.setIndex(originalReaderIndex, originalWriterIndex);

            channel.writeInbound(subByteBuf);
            channel.flushInbound();
        }
        byte[] tcpBytes = ByteBufUtils.toByteArray(tcpCache);
        ReferenceCountUtil.release(tcpCache);

        Object receivedMsg = channel.readInbound();
        Assert.assertEquals(msg.getClass(), receivedMsg.getClass());
        Assert.assertNull(channel.readInbound());

        // recover index of originalByteBuf
        if (msg instanceof byte[]) {
            Assert.assertTrue(Objects.deepEquals(msg, receivedMsg));
        } else {
            Assert.assertEquals(msg, receivedMsg);
        }
        channel.writeOutbound(receivedMsg);
        ByteBuf cache = Unpooled.buffer();
        ByteBuf buf;
        while ((buf = channel.readOutbound()) != null) {
            cache.writeBytes(buf);
            ReferenceCountUtil.release(buf);
        }

        assertBytesEquals(tcpBytes, ByteBufUtils.toByteArray(cache));
        ReferenceCountUtil.release(cache);

        channel.close();
    }
}
