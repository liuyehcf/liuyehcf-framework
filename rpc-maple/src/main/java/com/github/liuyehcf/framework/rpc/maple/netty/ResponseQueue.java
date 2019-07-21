package com.github.liuyehcf.framework.rpc.maple.netty;


import com.google.common.collect.Maps;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.SynchronousQueue;

/**
 * @author hechenfeng
 * @date 2019/3/25
 */
public class ResponseQueue {
    private static final Map<Channel, Map<Long, SynchronousQueue<MaplePacket>>> cacheQueueMap = Maps.newConcurrentMap();

    public static void createChannelQueue(final Channel channel) {
        cacheQueueMap.put(channel, Maps.newConcurrentMap());
    }

    public static void removeChannelQueue(final Channel channel) {
        cacheQueueMap.remove(channel);
    }

    public static void putResponse(final Channel channel, final long requestId, MaplePacket response) {
        final Map<Long, SynchronousQueue<MaplePacket>> cacheQueue = cacheQueueMap.get(channel);

        if (cacheQueue == null) {
            return;
        }

        final SynchronousQueue<MaplePacket> queue = cacheQueue.get(requestId);
        if (queue == null) {
            return;
        }

        queue.offer(response);
        cacheQueue.put(requestId, queue);
    }

    public static void createResponseQueue(final Channel channel, final long requestId) {
        final Map<Long, SynchronousQueue<MaplePacket>> cacheQueue = cacheQueueMap.get(channel);

        if (cacheQueue == null) {
            return;
        }

        final SynchronousQueue<MaplePacket> queue = new SynchronousQueue<>();

        cacheQueue.put(requestId, queue);
    }

    public static SynchronousQueue<MaplePacket> getResponseQueue(final Channel channel, final long requestId) {
        final Map<Long, SynchronousQueue<MaplePacket>> cacheQueue = cacheQueueMap.get(channel);

        if (cacheQueue == null) {
            return null;
        }

        return cacheQueue.get(requestId);
    }

    public static SynchronousQueue<MaplePacket> removeResponseQueue(final Channel channel, final long requestId) {
        final Map<Long, SynchronousQueue<MaplePacket>> cacheQueue = cacheQueueMap.get(channel);

        if (cacheQueue == null) {
            return null;
        }

        return cacheQueue.remove(requestId);
    }
}
