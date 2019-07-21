package com.github.liuyehcf.framework.rpc.maple.netty.provider;

import com.github.liuyehcf.framework.rpc.maple.netty.*;
import com.github.liuyehcf.framework.rpc.maple.netty.util.SerializeUtils;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @author hechenfeng
 * @date 2019/3/18
 */
public class ProviderHandler extends SimpleChannelInboundHandler<MaplePacket> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MaplePacket msg) {
        final MaplePacket.Header header = msg.getHeader();
        final long requestId = header.getRequestId();
        final byte serializeType = header.getSerializeType();

        try {
            final MapleRpcFrame frame = SerializeUtils.deserialize(serializeType, msg.getBody());

            final InvocationMeta invocationMeta = frame.getInvocationMeta();
            final ServiceMeta serviceMeta = invocationMeta.getServiceMeta();
            final String[] parameterTypes = invocationMeta.getParameterTypes();
            final Object[] args = frame.getArgs();

            final Object targetBean = ProviderCache.getProviderTargetBean(serviceMeta);

            final Class<?> clazz = targetBean.getClass();
            final Method method = clazz.getMethod(invocationMeta.getMethodName(), getParameterTypes(parameterTypes));

            final Object result = method.invoke(targetBean, args);

            byte[] body = SerializeUtils.serialize(serializeType, result);
            final DefaultMaplePacket.DefaultHeader responseHeader = new DefaultMaplePacket.DefaultHeader(
                    requestId, serializeType, Status.NORMAL.getStatus(), body.length);

            ctx.channel().writeAndFlush(new DefaultMaplePacket(responseHeader, body));
        } catch (Throwable e) {
            final byte[] body = SerializeUtils.serialize(serializeType, e);

            final DefaultMaplePacket.DefaultHeader responseHeader = new DefaultMaplePacket.DefaultHeader(
                    requestId, serializeType, Status.PROVIDER_EXCEPTION.getStatus(), body.length);

            ctx.channel().writeAndFlush(new DefaultMaplePacket(responseHeader, body));
        }
    }

    private Class<?>[] getParameterTypes(final String[] parameterTypes) throws ClassNotFoundException {
        final Class<?>[] types = new Class[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Class.forName(parameterTypes[i]);
        }

        return types;
    }
}
