package com.github.liuyehcf.framework.io.athena.util;

import io.netty.buffer.ByteBuf;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public abstract class ByteBufUtils {

    public static byte[] toByteArray(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
