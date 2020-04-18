package com.github.liuyehcf.framework.common.tools.test.io.netty;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.io.netty.ConvertUtils;
import com.github.liuyehcf.framework.common.tools.io.netty.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class TestConverter {

    @Test
    public void test() {
        HttpConverter httpConverter = new HttpConverter();

        AtomicReference<ByteBuf> outboundDataHolder = new AtomicReference<>();
        httpConverter.writeOutbound(new DefaultFullHttpRequest(
                        HttpVersion.HTTP_1_1,
                        HttpMethod.GET,
                        "/test"),
                (inboundData) -> {
                    throw new UnsupportedOperationException();
                },
                (outboundData) -> outboundDataHolder.set(outboundData.retain())
        );
        Assert.assertNotNull(outboundDataHolder);

        AtomicReference<FullHttpResponse> inboundDataHolder = new AtomicReference<>();
        httpConverter.writeInbound(ConvertUtils.convertResponse2ByteBuf(
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK), true),
                (inboundData) -> inboundDataHolder.set(inboundData.retain()),
                (outboundData) -> {
                    throw new UnsupportedOperationException();
                }
        );
        Assert.assertNotNull(inboundDataHolder);
    }

    private static final class HttpConverter extends Converter<ByteBuf, FullHttpResponse, FullHttpRequest, ByteBuf> {

        public HttpConverter() {
            super();
            channel.pipeline().addLast(new HttpClientCodec());
            channel.pipeline().addLast(new HttpObjectAggregator(1024));
        }
    }
}
