package com.github.liuyehcf.framework.rule.engine.test.remote;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.ProtocolConstant;
import org.junit.Test;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/4
 */
@SuppressWarnings("all")
public class TestFrame extends TestRemoteBase {

    @Test
    public void testBoundary() {
        test(null, 1);
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

    private void test(byte[] bytes, int expectedFrameNum) {
        List<Frame> frames = Frame.wrap(bytes);

        Assert.assertEquals(expectedFrameNum, frames.size());

        for (Frame frame : frames) {
            byte[] protocolBytes = frame.serialize();
            Frame rebuildFrame = Frame.deserialize(protocolBytes);

            assertFrameEquals(frame, rebuildFrame);
        }
    }
}
