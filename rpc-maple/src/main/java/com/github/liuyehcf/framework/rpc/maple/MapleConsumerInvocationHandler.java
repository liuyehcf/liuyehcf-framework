package com.github.liuyehcf.framework.rpc.maple;

import com.github.liuyehcf.framework.rpc.maple.netty.*;
import com.github.liuyehcf.framework.rpc.maple.netty.consumer.ConsumerChannelPool;
import com.github.liuyehcf.framework.rpc.maple.netty.consumer.ConsumerLoop;
import com.github.liuyehcf.framework.rpc.maple.netty.util.IDGenerator;
import com.github.liuyehcf.framework.rpc.maple.netty.util.SerializeUtils;
import com.github.liuyehcf.framework.rpc.maple.register.ConfigClient;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceAddress;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceInstance;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/3/18
 */
public class MapleConsumerInvocationHandler implements InvocationHandler {

    private static final Method GENERIC_INVOKE_METHOD;

    static {
        try {
            GENERIC_INVOKE_METHOD = GenericService.class.getMethod("genericInvoke", String.class, String[].class, Object[].class);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final ConfigClient configClient;
    private final ServiceMeta serviceMeta;
    private final ChannelProxy channelProxy = new ChannelProxy();

    public MapleConsumerInvocationHandler(final ConfigClient configClient, final ServiceMeta serviceMeta) {
        this.configClient = configClient;
        this.serviceMeta = serviceMeta;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final DefaultInvocationMeta invocationMeta;
        final Object[] actualArgs;

        if (isGenericInvoke(method)) {
            invocationMeta = new DefaultInvocationMeta(serviceMeta, (String) args[0], (String[]) args[1]);
            actualArgs = (Object[]) args[2];
        } else {
            invocationMeta = new DefaultInvocationMeta(serviceMeta, method.getName(), getParameterTypes(method));
            actualArgs = args;
        }
        final MapleRpcFrame frame = new MapleRpcFrame(actualArgs, invocationMeta);

        return syncInvoke(frame);
    }

    private boolean isGenericInvoke(final Method method) {
        return Objects.equals(GENERIC_INVOKE_METHOD, method);
    }

    private String[] getParameterTypes(final Method method) {
        final Class<?>[] types = method.getParameterTypes();
        final String[] parameterTypes = new String[types.length];

        for (int i = 0; i < types.length; i++) {
            parameterTypes[i] = types[i].getName();
        }

        return parameterTypes;
    }

    @SuppressWarnings("all")
    private Object syncInvoke(final MapleRpcFrame frame) throws Throwable {
        if (channelProxy.getChannel() == null) {
            synchronized (this) {
                if (channelProxy.getChannel() == null) {
                    final ServiceInstance serviceInstance = configClient.fetchAnyServiceAddress(serviceMeta);
                    final ServiceAddress serviceAddress = serviceInstance.getServiceAddress();

                    Channel channel;
                    while ((channel = ConsumerChannelPool.getChannel(serviceAddress)) == null) {
                        ConsumerChannelPool.addChannel(serviceAddress,
                                () -> ConsumerLoop.connect(serviceAddress));
                    }

                    channelProxy.bind(serviceInstance, channel);
                    channel.closeFuture().addListener((ChannelFuture future) -> channelProxy.close());
                }
            }
        }

        final ServiceInstance serviceInstance = channelProxy.getServiceInstance();
        final Channel channel = channelProxy.getChannel();

        final long requestId = IDGenerator.randomRequestId();
        final byte serializeType = serviceInstance.getServiceMeta().getSerializeType();
        final byte[] body = SerializeUtils.serialize(serializeType, frame);

        final DefaultMaplePacket.DefaultHeader header = new DefaultMaplePacket.DefaultHeader(
                requestId, serializeType, Status.NORMAL.getStatus(), body.length);

        ResponseQueue.createResponseQueue(channel, requestId);
        channel.writeAndFlush(new DefaultMaplePacket(header, body));

        return getResponseMillis(channel, requestId);
    }

    private Object getResponseMillis(final Channel channel, final long requestId) throws Throwable {
        try {
            final SynchronousQueue<MaplePacket> queue = ResponseQueue.getResponseQueue(channel, requestId);

            if (queue == null) {
                throw new MapleException(MapleException.Code.TIMEOUT);
            }

            final MaplePacket packet = queue.poll(serviceMeta.getClientTimeout(), TimeUnit.MILLISECONDS);
            ResponseQueue.removeResponseQueue(channel, requestId);

            if (packet == null) {
                throw new MapleException(MapleException.Code.TIMEOUT);
            }

            final MaplePacket.Header header = packet.getHeader();
            final byte serializeType = header.getSerializeType();
            final Status status = Status.of(header.getStatus());

            switch (status) {
                case NORMAL:
                    return SerializeUtils.deserialize(serializeType, packet.getBody());
                case PROVIDER_EXCEPTION:
                    throw (Throwable) SerializeUtils.deserialize(serializeType, packet.getBody());
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (InterruptedException e) {
            throw new MapleException(MapleException.Code.TIMEOUT, e);
        }
    }
}
