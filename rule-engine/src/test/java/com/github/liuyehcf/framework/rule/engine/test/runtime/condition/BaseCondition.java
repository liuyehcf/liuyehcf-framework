package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.*;

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
    public boolean onCondition(ConditionContext context) throws Exception {
        if (CONDITION_EXECUTION_MODE == ASYNC_MODE_WITHOUT_TIMEOUT) {
            context.executeAsync(CONDITION_EXECUTOR, () -> doCondition(context));
            return false;
        } else if (CONDITION_EXECUTION_MODE == ASYNC_MODE_WITH_TIMEOUT) {
            context.executeAsync(CONDITION_EXECUTOR, () -> doCondition(context), 3, TimeUnit.SECONDS);
            return false;
        } else {
            return doCondition(context);
        }
    }

    abstract boolean doCondition(ConditionContext context) throws Exception;
}
