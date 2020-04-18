package com.github.liuyehcf.framework.common.tools.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.ReferenceCountUtil;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class ConvertUtils {

    public static ByteBuf convertRequest2ByteBuf(FullHttpRequest msg, boolean needRetain) {
        EmbeddedChannel ch = new EmbeddedChannel(new HttpRequestEncoder());
        return convert2ByteBuf(ch, msg, needRetain);
    }

    public static byte[] convertRequest2Bytes(FullHttpRequest msg, boolean needRetain) {
        ByteBuf byteBuf = convertRequest2ByteBuf(msg, needRetain);
        try {
            return ByteBufUtil.getBytes(byteBuf);
        } finally {
            ReferenceCountUtil.release(byteBuf);
        }
    }

    public static ByteBuf convertResponse2ByteBuf(FullHttpResponse msg, boolean needRetain) {
        EmbeddedChannel ch = new EmbeddedChannel(new HttpResponseEncoder());
        return convert2ByteBuf(ch, msg, needRetain);
    }

    public static byte[] convertResponse2Bytes(FullHttpResponse msg, boolean needRetain) {
        ByteBuf byteBuf = convertResponse2ByteBuf(msg, needRetain);
        try {
            return ByteBufUtil.getBytes(byteBuf);
        } finally {
            ReferenceCountUtil.release(byteBuf);
        }
    }

    private static ByteBuf convert2ByteBuf(EmbeddedChannel ch, ByteBufHolder msg, boolean needRetain) {
        ByteBuf byteBuf;
        ByteBuf cache = Unpooled.buffer();
        try {
            if (needRetain) {
                ch.writeOutbound(msg.retain());
            } else {
                ch.writeOutbound(msg);
            }

            while ((byteBuf = ch.readOutbound()) != null) {
                try {
                    cache.writeBytes(byteBuf);
                } finally {
                    ReferenceCountUtil.release(byteBuf);
                }
            }

            return cache;
        } finally {
            ch.close();
        }
    }
}
