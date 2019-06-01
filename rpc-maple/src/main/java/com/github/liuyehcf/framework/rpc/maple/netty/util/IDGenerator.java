package com.github.liuyehcf.framework.rpc.maple.netty.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenlu
 * @date 2019/3/25
 */
public abstract class IDGenerator {
    private static final AtomicLong REQUEST_ID_GENERATOR = new AtomicLong();

    public static long randomRequestId() {
        return REQUEST_ID_GENERATOR.incrementAndGet();
    }
}
