package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.handler;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.Message;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.Package;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.SerializeType;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class SerializeWriteHandler extends MessageToMessageEncoder<Message> {

    private final SerializeType serializeType;

    public SerializeWriteHandler(SerializeType serializeType) {
        this.serializeType = serializeType;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) {
        switch (serializeType) {
            case java:
                out.add(Package.wrap(
                        msg.getType().getType(),
                        serializeType.getType(),
                        CloneUtils.javaSerialize(msg)));
            case hessian:
                out.add(Package.wrap(
                        msg.getType().getType(),
                        serializeType.getType(),
                        CloneUtils.hessianSerialize(msg)));
            default:
                // todo
                break;
        }
    }
}
