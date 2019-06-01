package com.github.liuyehcf.framework.rpc.maple.netty.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenlu
 * @date 2019/3/26
 */
public abstract class MapleThreadPool {
    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("MAPLE-CONSUMER-t-%d")
            .build();
    public static final ThreadPoolExecutor CONSUMER_WORKER = new ThreadPoolExecutor(
            5,
            20,
            0L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            NAMED_THREAD_FACTORY);
    public static final ThreadPoolExecutor PROVIDER_WORKER = new ThreadPoolExecutor(
            5,
            20,
            0L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            NAMED_THREAD_FACTORY);
    private static final ThreadFactory PROVIDER_NAMED_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("MAPLE-FRAMEWORK-t-%d")
            .build();
}
