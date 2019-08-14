package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.IS_LISTENER_ASYNC;
import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.LISTENER_TIMEOUT;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
@SuppressWarnings("all")
public abstract class BaseListener implements ListenerDelegate {

    private static ExecutorService LISTENER_EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("LISTENER-ASYNC-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean isAsync() {
        return IS_LISTENER_ASYNC;
    }

    @Override
    public ExecutorService getAsyncExecutor() {
        return LISTENER_EXECUTOR;
    }

    @Override
    public long getAsyncTimeout() {
        return LISTENER_TIMEOUT;
    }
}
