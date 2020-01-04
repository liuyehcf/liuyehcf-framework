package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Frame;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Option;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol.Package;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;

import java.util.Random;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class TestRemoteBase {

    static final Random RANDOM = new Random();

    byte[] getByteArray(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    void assertMessageEquals(Package package1, Package package2) {
        Assert.assertEquals(package1.getType(), package2.getType());
        Assert.assertEquals(package1.getSerializeType(), package2.getSerializeType());
        assertBytesEquals(package1.getPayload(), package2.getPayload());
    }

    void assertFrameEquals(Frame frame1, Frame frame2) {
        Assert.assertEquals(frame1.getVersion(), frame2.getVersion());
        Assert.assertEquals(frame1.getHeaderLength(), frame2.getHeaderLength());
        Assert.assertEquals(frame1.getTotalLength(), frame2.getTotalLength());
        Assert.assertEquals(frame1.getOptions().size(), frame2.getOptions().size());
        for (int i = 0; i < frame1.getOptions().size(); i++) {
            assertOptionEquals(frame1.getOptions().get(i), frame2.getOptions().get(i));
        }
        assertBytesEquals(frame1.getPayload(), frame2.getPayload());
    }

    private void assertOptionEquals(Option option1, Option option2) {
        Assert.assertEquals(option1.getType(), option2.getType());
        assertBytesEquals(option1.getValue(), option2.getValue());
    }

    void assertBytesEquals(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == null || bytes2 == null) {
            Assert.assertTrue(ArrayUtils.isEmpty(bytes1));
            Assert.assertTrue(ArrayUtils.isEmpty(bytes2));
        } else {
            Assert.assertEquals(bytes1.length, bytes2.length);
            for (int i = 0; i < bytes1.length; i++) {
                Assert.assertEquals(bytes1[i], bytes2[i]);
            }
        }
    }
}
