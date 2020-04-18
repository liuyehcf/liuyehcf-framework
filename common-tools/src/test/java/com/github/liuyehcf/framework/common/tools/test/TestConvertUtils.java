package com.github.liuyehcf.framework.common.tools.test;

import com.github.liuyehcf.framework.common.tools.io.netty.ConvertUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class TestConvertUtils {

    @Test
    public void testRequest2ByteBuf() {
        for (int i = 0; i < 10000; i++) {
            ByteBuf byteBuf = ConvertUtils.convertRequest2ByteBuf(new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1,
                    HttpMethod.GET,
                    "/test"
            ), true);

            Assert.assertNotNull(byteBuf);
            Assert.assertEquals(1, byteBuf.refCnt());
            ReferenceCountUtil.release(byteBuf);
        }
    }

    @Test
    public void testRequest2Bytes() {
        for (int i = 0; i < 10000; i++) {
            byte[] bytes = ConvertUtils.convertRequest2Bytes(new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1,
                    HttpMethod.GET,
                    "/test"
            ), true);

            Assert.assertNotNull(bytes);
        }
    }

    @Test
    public void testResponse2ByteBuf() {
        for (int i = 0; i < 10000; i++) {
            ByteBuf byteBuf = ConvertUtils.convertResponse2ByteBuf(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK
            ), true);

            Assert.assertNotNull(byteBuf);
            Assert.assertEquals(1, byteBuf.refCnt());
            ReferenceCountUtil.release(byteBuf);
        }
    }

    @Test
    public void testResponse2Bytes() {
        for (int i = 0; i < 10000; i++) {
            byte[] bytes = ConvertUtils.convertResponse2Bytes(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK
            ), true);

            Assert.assertNotNull(bytes);
        }
    }
}
