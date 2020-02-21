package com.github.liuyehcf.framework.io.athena.test;

import com.github.liuyehcf.framework.io.athena.protocol.AthenaFrame;
import com.github.liuyehcf.framework.io.athena.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;

import java.util.Random;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class TestBase {

    static final Random RANDOM = new Random();

    byte[] getByteArray(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    void assertFrameEquals(AthenaFrame athenaFrame1, AthenaFrame athenaFrame2) {
        Assert.assertEquals(athenaFrame1.getVersion(), athenaFrame2.getVersion());
        Assert.assertEquals(athenaFrame1.getHeaderLength(), athenaFrame2.getHeaderLength());
        Assert.assertEquals(athenaFrame1.getTotalLength(), athenaFrame2.getTotalLength());
        Assert.assertEquals(athenaFrame1.hasNextFrame(), athenaFrame2.hasNextFrame());

        assertByteBufEquals(athenaFrame1.content(), athenaFrame2.content());
    }

    void assertByteBufEquals(ByteBuf byteBuf1, ByteBuf byteBuf2) {
        Assert.assertNotNull(byteBuf1);
        Assert.assertNotNull(byteBuf2);

        byte[] bytes1 = ByteBufUtils.toByteArray(byteBuf1);
        byte[] bytes2 = ByteBufUtils.toByteArray(byteBuf2);

        assertBytesEquals(bytes1, bytes2);
    }

    void assertBytesEquals(byte[] bytes1, byte[] bytes2) {
        Assert.assertEquals(bytes1.length, bytes2.length);
        for (int i = 0; i < bytes1.length; i++) {
            Assert.assertEquals(bytes1[i], bytes2[i]);
        }
    }
}
