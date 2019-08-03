package com.github.liuyehcf.framework.rule.engine.test.runtime.action;


import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.ACTION_ASYNC_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
public abstract class BaseAction implements ActionDelegate {

    private static ExecutorService ACTION_EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("ACTION-ASYNC-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public final void onAction(ActionContext context) throws Exception {
        if (ACTION_ASYNC_SWITCH) {
            context.executeAsync(ACTION_EXECUTOR, () -> {
                doAction(context);
                return null;
            });
        } else {
            doAction(context);
        }
    }

    abstract void doAction(ActionContext context) throws Exception;
}
