package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler.FrameAggregatorHandler;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler.FrameChunkedWriteHandler;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler.FrameHandler;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Package;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.ProtocolConstant;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.SerializeType;
import com.github.liuyehcf.framework.flow.engine.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
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
public class TestPackageHandler extends TestRemoteBase {

    @Test
    public void testBoundary() {
        test(Package.wrap(0, SerializeType.hessian.getType(), getByteArray(0)));
        test(Package.wrap(0, SerializeType.hessian.getType(), getByteArray(1)));
        test(Package.wrap(0, SerializeType.hessian.getType(), getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH - 1)));
        test(Package.wrap(0, SerializeType.hessian.getType(), getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH)));
        test(Package.wrap(0, SerializeType.hessian.getType(), getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH + 1)));
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);

            int len = RANDOM.nextInt(ProtocolConstant.MAX_PAYLOAD_LENGTH * 100);

            test(Package.wrap(0, SerializeType.hessian.getType(), getByteArray(len)));
        }
    }

    private void test(Package aPackage) {
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new FrameHandler());
        pipeline.addLast(new FrameAggregatorHandler());
        pipeline.addLast(new FrameChunkedWriteHandler());

        List<Frame> frames = Frame.wrap(aPackage.serialize());
        ByteBuf tcpCache = Unpooled.buffer();
        for (int i = 0; i < frames.size(); i++) {
            byte[] bytes = frames.get(i).serialize();
            tcpCache.writeBytes(bytes);
            channel.writeInbound(Unpooled.wrappedBuffer(bytes));
        }
        byte[] tcpBytes = ByteBufUtils.toByteArray(tcpCache);
        ReferenceCountUtil.release(tcpCache);
        channel.flush();

        Package receivedPackage = channel.readInbound();
        Assert.assertNull(channel.readInbound());

        assertMessageEquals(aPackage, receivedPackage);

        channel.writeOutbound(receivedPackage);
        ByteBuf cache = Unpooled.buffer();
        ByteBuf buf;
        while ((buf = channel.readOutbound()) != null) {
            try {
                cache.writeBytes(buf);
            } finally {
                ReferenceCountUtil.release(buf);
            }
        }

        assertBytesEquals(tcpBytes, ByteBufUtils.toByteArray(cache));
        ReferenceCountUtil.release(cache);
    }
}
