package com.github.liuyehcf.framework.rule.engine.util;

import io.netty.buffer.ByteBuf;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public abstract class ByteBufUtils {

    public static byte[] toByteArray(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
