package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.CONDITION_TIMEOUT;
import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.IS_CONDITION_ASYNC;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
@SuppressWarnings("all")
public abstract class BaseCondition implements ConditionDelegate {

    private static ExecutorService CONDITION_EXECUTOR = new ThreadPoolExecutor(64, 64, 5L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("CONDITION-ASYNC-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean isAsync() {
        return IS_CONDITION_ASYNC.get();
    }

    @Override
    public ExecutorService getAsyncExecutor() {
        return CONDITION_EXECUTOR;
    }

    @Override
    public long getAsyncTimeout() {
        return CONDITION_TIMEOUT;
    }
}
