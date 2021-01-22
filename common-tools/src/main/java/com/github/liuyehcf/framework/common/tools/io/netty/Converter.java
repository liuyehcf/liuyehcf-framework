package com.github.liuyehcf.framework.common.tools.io.netty;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;

/*
writeInbound                             readInbound
     +                                         ^
     |                                         |
     |                                         |
     |                                         |
     | T1                                      | T2
 +---v---------->channelInboundHandlers+-------+------->
                                                       |
                                                       |
                                                       |
                                                       |
                                                       |
                                                       |
       T4                                        T3    |
 <---+---------+ channelOutboundHandlers<--------^-----+
     |                                           |
     |                                           |
     |                                           |
     |                                           |
     v                                           |
readOutbound                                  writeOutbound
 */

/**
 * @author hechenfeng
 * @date 2020/4/14
 */
public abstract class Converter<T1, T2, T3, T4> {

    protected final EmbeddedChannel channel;

    protected Converter() {
        channel = new EmbeddedChannel();
    }

    public final void writeOutbound(T3 msg, Consumer<T2> inboundConsumer, Consumer<T4> outboundConsumer) {
        channel.writeOneOutbound(msg).addListener(future -> {
            if (!future.isSuccess()) {
                ReferenceCountUtil.release(msg);
            }
        });
        channel.flushOutbound();

        callBothConsumersIfNecessary(inboundConsumer, outboundConsumer);
    }

    public final void writeInbound(T1 msg, Consumer<T2> inboundConsumer, Consumer<T4> outboundConsumer) {
        channel.writeOneInbound(msg).addListener(future -> {
            if (!future.isSuccess()) {
                ReferenceCountUtil.release(msg);
            }
        });
        channel.flushInbound();

        callBothConsumersIfNecessary(inboundConsumer, outboundConsumer);
    }

    public void close() {
        channel.finishAndReleaseAll();
    }

    private void callBothConsumersIfNecessary(Consumer<T2> inboundConsumer, Consumer<T4> outboundConsumer) {
        T2 inboundData;
        while ((inboundData = readInbound()) != null) {
            int expectedCnt = -1;
            try {
                expectedCnt = CommonUtils.getRefCnt(inboundData);
                if (inboundConsumer != null) {
                    inboundConsumer.consume(inboundData);
                }
            } finally {
                CommonUtils.releaseToExpectedRefCnt(inboundData, expectedCnt);
            }
        }

        T4 outboundData;
        while ((outboundData = readOutbound()) != null) {
            int expectedCnt = -1;
            try {
                expectedCnt = CommonUtils.getRefCnt(outboundData);
                if (outboundConsumer != null) {
                    outboundConsumer.consume(outboundData);
                }
            } finally {
                CommonUtils.releaseToExpectedRefCnt(outboundData, expectedCnt);
            }
        }
    }

    private T2 readInbound() {
        return channel.readInbound();
    }

    private T4 readOutbound() {
        return channel.readOutbound();
    }

    @FunctionalInterface
    public interface Consumer<I> {

        /**
         * implement consume the data
         * need not to release the data
         */
        void consume(I data);
    }

    public static abstract class ByteBufConsumer implements Consumer<ByteBuf> {

        private final int maxSegmentSize;

        public ByteBufConsumer(int maxSegmentSize) {
            Assert.assertTrue(maxSegmentSize > 0, "maxSegmentSize must be positive");
            this.maxSegmentSize = maxSegmentSize;
        }

        @Override
        public void consume(ByteBuf data) {
            int index = 0;

            while (data.readableBytes() > 0) {
                int readNum = Math.min(data.readableBytes(), maxSegmentSize);
                ByteBuf segment = data.readBytes(readNum);
                int expectedCnt = -1;
                try {
                    expectedCnt = CommonUtils.getRefCnt(segment);
                    consumeSegment(index++, segment);
                } finally {
                    CommonUtils.releaseToExpectedRefCnt(segment, expectedCnt);
                }
            }
        }

        /**
         * consume segment with length smaller than or equals to maxSegmentSize
         * need not to release the segment
         */
        protected abstract void consumeSegment(int segmentIndex, ByteBuf segment);
    }
}
