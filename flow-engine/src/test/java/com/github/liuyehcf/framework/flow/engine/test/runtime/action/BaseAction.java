package com.github.liuyehcf.framework.flow.engine.test.runtime.action;


import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.ACTION_TIMEOUT;
import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.IS_ACTION_ASYNC;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
@SuppressWarnings("all")
public abstract class BaseAction implements ActionDelegate {

    private static ExecutorService ACTION_EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("ACTION-ASYNC-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean isAsync() {
        return IS_ACTION_ASYNC;
    }

    @Override
    public ExecutorService getAsyncExecutor() {
        return ACTION_EXECUTOR;
    }

    @Override
    public long getAsyncTimeout() {
        return ACTION_TIMEOUT;
    }
}
