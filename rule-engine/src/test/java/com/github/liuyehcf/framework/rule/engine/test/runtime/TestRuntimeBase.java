package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.test.runtime.action.*;
import com.github.liuyehcf.framework.rule.engine.test.runtime.condition.*;
import com.github.liuyehcf.framework.rule.engine.test.runtime.interceptor.ActionRegexInterceptor;
import com.github.liuyehcf.framework.rule.engine.test.runtime.interceptor.EmptyInterceptor;
import com.github.liuyehcf.framework.rule.engine.test.runtime.interceptor.LogInterceptor;
import com.github.liuyehcf.framework.rule.engine.test.runtime.listener.*;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class TestRuntimeBase {

    public static final int SYNC_MODE = 1;
    public static final int ASYNC_MODE_WITHOUT_TIMEOUT = 1;
    public static final int ASYNC_MODE_WITH_TIMEOUT = 2;

    public static final boolean STD_OUT_SWITCH = false;
    public static final int ACTION_EXECUTION_MODE = ASYNC_MODE_WITHOUT_TIMEOUT;
    public static final int CONDITION_EXECUTION_MODE = ASYNC_MODE_WITHOUT_TIMEOUT;
    public static final int LISTENER_EXECUTION_MODE = ASYNC_MODE_WITHOUT_TIMEOUT;
    protected static final Random RANDOM = new Random();
    protected static final int EXECUTE_TIMES = 1;
    private static ExecutorService EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("RULE-ENGINE-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        RuleEngine.registerActionDelegateFactory("printAction", PrintAction::new);
        RuleEngine.registerActionDelegateFactory("throwExceptionAction", ThrowExceptionAction::new);
        RuleEngine.registerActionDelegateFactory("throwLinkTerminateAction", ThrowLinkTerminateAction::new);
        RuleEngine.registerActionDelegateFactory("getPropertyAction", GetPropertyAction::new);
        RuleEngine.registerActionDelegateFactory("setPropertyAction", SetPropertyAction::new);
        RuleEngine.registerActionDelegateFactory("missingArgumentAction", MissingArgumentAction::new);
        RuleEngine.registerActionDelegateFactory("setTraceAttrAction", SetTraceAttrAction::new);
        RuleEngine.registerActionDelegateFactory("dot.name.action", DotNameAction::new);
        RuleEngine.registerActionDelegateFactory("slash/name/action", SlashNameAction::new);
        RuleEngine.registerActionDelegateFactory("sleepAction", SleepAction::new);

        RuleEngine.registerConditionDelegateFactory("printCondition", PrintCondition::new);
        RuleEngine.registerConditionDelegateFactory("throwExceptionCondition", ThrowExceptionCondition::new);
        RuleEngine.registerConditionDelegateFactory("throwLinkTerminateCondition", ThrowLinkTerminateCondition::new);
        RuleEngine.registerConditionDelegateFactory("getPropertyCondition", GetPropertyCondition::new);
        RuleEngine.registerConditionDelegateFactory("setPropertyCondition", SetPropertyCondition::new);
        RuleEngine.registerConditionDelegateFactory("missingArgumentCondition", MissingArgumentCondition::new);
        RuleEngine.registerConditionDelegateFactory("setTraceAttrCondition", SetTraceAttrCondition::new);
        RuleEngine.registerConditionDelegateFactory("dot.name.condition", DotNameCondition::new);
        RuleEngine.registerConditionDelegateFactory("slash/name/condition", SlashNameCondition::new);
        RuleEngine.registerConditionDelegateFactory("sleepCondition", SleepCondition::new);

        RuleEngine.registerListenerDelegateFactory("printListener", PrintListener::new);
        RuleEngine.registerListenerDelegateFactory("getPropertyListener", GetPropertyListener::new);
        RuleEngine.registerListenerDelegateFactory("setPropertyListener", SetPropertyListener::new);
        RuleEngine.registerListenerDelegateFactory("missingArgumentListener", MissingArgumentListener::new);
        RuleEngine.registerListenerDelegateFactory("setTraceAttrListener", SetTraceAttrListener::new);
        RuleEngine.registerListenerDelegateFactory("dot.name.listener", DotNameListener::new);
        RuleEngine.registerListenerDelegateFactory("slash/name/listener", SlashNameListener::new);
        RuleEngine.registerListenerDelegateFactory("throwExceptionListener", ThrowExceptionListener::new);
        RuleEngine.registerListenerDelegateFactory("throwLinkTerminateListener", ThrowLinkTerminateListener::new);
        RuleEngine.registerListenerDelegateFactory("successResultListener", SuccessResultListener::new);

        RuleEngine.registerDelegateInterceptorFactory(ActionRegexInterceptor::new);

        if (STD_OUT_SWITCH) {
            RuleEngine.registerDelegateInterceptorFactory(EmptyInterceptor::new);
            RuleEngine.registerDelegateInterceptorFactory(LogInterceptor::new);
        }
    }

    protected void assertPromise(Promise<ExecutionInstance> promise, boolean isCanceled, boolean isDone, boolean isSuccess, boolean isFailure) {
        if (promise.isFailure()) {
            Throwable cause = promise.cause();
            if (cause != null) {
                cause.printStackTrace();
            }
        }

        Assert.assertEquals(isCanceled, promise.isCancelled());
        Assert.assertEquals(isDone, promise.isDone());
        Assert.assertEquals(isSuccess, promise.isSuccess());
        Assert.assertEquals(isFailure, promise.isFailure());
    }

    protected void executeTimes(Runnable runnable) {
        executeTimes(runnable, EXECUTE_TIMES);
    }

    protected void executeTimes(Runnable runnable, int times) {
        List<Future<?>> futures = Lists.newArrayList();
        for (int i = 0; i < times; i++) {
            futures.add(EXECUTOR.submit(runnable));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    protected Rule compile(String content) {
        System.out.println(content);
        return RuleEngine.compile(content);
    }

    protected Promise<ExecutionInstance> startRule(String content, Map<String, Object> env) {
        System.out.println(content);
        return RuleEngine.startRule(content, env);
    }

    protected Promise<ExecutionInstance> startRule(Rule rule, Map<String, Object> env) {
        return RuleEngine.startRule(rule, env);
    }
}
