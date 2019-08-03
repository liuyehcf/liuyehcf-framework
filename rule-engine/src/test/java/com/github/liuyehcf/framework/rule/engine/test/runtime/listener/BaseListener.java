package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.LISTENER_ASYNC_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
public abstract class BaseListener implements ListenerDelegate {

    private static ExecutorService LISTENER_EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("LISTENER-ASYNC-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void onBefore(ListenerContext context) throws Exception {
        if (LISTENER_ASYNC_SWITCH) {
            context.executeAsync(LISTENER_EXECUTOR, () -> {
                doBefore(context);
                return null;
            });
        } else {
            doBefore(context);
        }
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) throws Exception {
        if (LISTENER_ASYNC_SWITCH) {
            context.executeAsync(LISTENER_EXECUTOR, () -> {
                doSuccess(context, result);
                return null;
            });
        } else {
            doSuccess(context, result);
        }
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) throws Exception {
        if (LISTENER_ASYNC_SWITCH) {
            context.executeAsync(LISTENER_EXECUTOR, () -> {
                doFailure(context, cause);
                return null;
            });
        } else {
            doFailure(context, cause);
        }
    }

    abstract void doBefore(ListenerContext context) throws Exception;

    abstract void doSuccess(ListenerContext context, Object result) throws Exception;

    abstract void doFailure(ListenerContext context, Throwable cause) throws Exception;
}
