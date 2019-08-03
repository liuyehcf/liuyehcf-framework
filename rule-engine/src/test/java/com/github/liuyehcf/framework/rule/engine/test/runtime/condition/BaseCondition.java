package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.CONDITION_ASYNC_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
public abstract class BaseCondition implements ConditionDelegate {

    private static ExecutorService CONDITION_EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("CONDITION-ASYNC-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean onCondition(ConditionContext context) throws Exception {
        if (CONDITION_ASYNC_SWITCH) {
            context.executeAsync(CONDITION_EXECUTOR, () -> doCondition(context));
            return false;
        } else {
            return doCondition(context);
        }
    }

    abstract boolean doCondition(ConditionContext context) throws Exception;
}
