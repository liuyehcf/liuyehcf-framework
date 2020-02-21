package com.github.liuyehcf.framework.io.athena.test;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.io.athena.protocol.AthenaFrame;
import com.github.liuyehcf.framework.io.athena.protocol.ProtocolConstant;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class TestAthenaFrame extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestAthenaFrame.class);

    @Test
    public void testBoundary() {
        test(getByteArray(1), 1);
        test(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH), 1);
    }

    @Test
    public void testTwoFrame() {
        test(getByteArray(ProtocolConstant.MAX_PAYLOAD_LENGTH + 1), 2);
        test(getByteArray(2 * ProtocolConstant.MAX_PAYLOAD_LENGTH), 2);
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);

            int len = RANDOM.nextInt(ProtocolConstant.MAX_PAYLOAD_LENGTH * 100);

            int numFrame = (int) Math.ceil(1.0 * len / ProtocolConstant.MAX_PAYLOAD_LENGTH);

            test(getByteArray(len), numFrame);
        }
    }

    @Test
    public void testMemoryLead() {
        for (int i = 0; i < 20000; i++) {
            LOGGER.error("{}M", i);
            List<AthenaFrame> athenaFrames = AthenaFrame.wrap(new byte[NumberUtils._1M]);
            athenaFrames.forEach(AthenaFrame::release);
        }
    }

    private void test(byte[] bytes, int expectedFrameNum) {
        List<AthenaFrame> athenaFrames = AthenaFrame.wrap(bytes);

        Assert.assertEquals(expectedFrameNum, athenaFrames.size());

        for (AthenaFrame athenaFrame : athenaFrames) {
            // save index of payload
            ByteBuf payload = athenaFrame.content();
            int originalReaderIndex = payload.readerIndex();
            int originalWriterIndex = payload.writerIndex();

            // serialize will modify the index of payload
            ByteBuf protocolByteBuf = athenaFrame.serialize();
            AthenaFrame rebuildAthenaFrame = AthenaFrame.deserialize(protocolByteBuf);

            // recover index of payload
            payload.setIndex(originalReaderIndex, originalWriterIndex);
            assertFrameEquals(athenaFrame, rebuildAthenaFrame);

            ReferenceCountUtil.release(rebuildAthenaFrame);
        }
    }
}
