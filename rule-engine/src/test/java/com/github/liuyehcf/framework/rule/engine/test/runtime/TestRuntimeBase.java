package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.rule.engine.ExecutionCondition;
import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultRuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.constant.EnvCloneType;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Attribute;
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

    public static final int ACTION_TIMEOUT = 0;
    public static final int CONDITION_TIMEOUT = 0;
    public static final int LISTENER_TIMEOUT = 0;

    public static final boolean STD_OUT_SWITCH = false;
    public static final boolean IS_ACTION_ASYNC = true;
    public static final boolean IS_CONDITION_ASYNC = true;
    public static final boolean IS_LISTENER_ASYNC = true;
    protected static final Random RANDOM = new Random();
    protected static final int EXECUTE_TIMES = 1;
    protected static final RuleEngine engine;
    private static ExecutorService EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("RULE-ENGINE-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        RuleProperties properties = new RuleProperties();
        properties.setEnvCloneType(EnvCloneType.hessian);
        engine = new DefaultRuleEngine(properties);

        engine.registerActionDelegateFactory("printAction", PrintAction::new);
        engine.registerActionDelegateFactory("throwExceptionAction", ThrowExceptionAction::new);
        engine.registerActionDelegateFactory("throwLinkTerminateAction", ThrowLinkTerminateAction::new);
        engine.registerActionDelegateFactory("getPropertyAction", GetPropertyAction::new);
        engine.registerActionDelegateFactory("setPropertyAction", SetPropertyAction::new);
        engine.registerActionDelegateFactory("missingArgumentAction", MissingArgumentAction::new);
        engine.registerActionDelegateFactory("setTraceAttrAction", SetTraceAttrAction::new);
        engine.registerActionDelegateFactory("dot.name.action", DotNameAction::new);
        engine.registerActionDelegateFactory("slash/name/action", SlashNameAction::new);
        engine.registerActionDelegateFactory("sleepAction", SleepAction::new);

        engine.registerConditionDelegateFactory("printCondition", PrintCondition::new);
        engine.registerConditionDelegateFactory("throwExceptionCondition", ThrowExceptionCondition::new);
        engine.registerConditionDelegateFactory("throwLinkTerminateCondition", ThrowLinkTerminateCondition::new);
        engine.registerConditionDelegateFactory("getPropertyCondition", GetPropertyCondition::new);
        engine.registerConditionDelegateFactory("setPropertyCondition", SetPropertyCondition::new);
        engine.registerConditionDelegateFactory("missingArgumentCondition", MissingArgumentCondition::new);
        engine.registerConditionDelegateFactory("setTraceAttrCondition", SetTraceAttrCondition::new);
        engine.registerConditionDelegateFactory("dot.name.condition", DotNameCondition::new);
        engine.registerConditionDelegateFactory("slash/name/condition", SlashNameCondition::new);
        engine.registerConditionDelegateFactory("sleepCondition", SleepCondition::new);

        engine.registerListenerDelegateFactory("printListener", PrintListener::new);
        engine.registerListenerDelegateFactory("getPropertyListener", GetPropertyListener::new);
        engine.registerListenerDelegateFactory("setPropertyListener", SetPropertyListener::new);
        engine.registerListenerDelegateFactory("missingArgumentListener", MissingArgumentListener::new);
        engine.registerListenerDelegateFactory("setTraceAttrListener", SetTraceAttrListener::new);
        engine.registerListenerDelegateFactory("dot.name.listener", DotNameListener::new);
        engine.registerListenerDelegateFactory("slash/name/listener", SlashNameListener::new);
        engine.registerListenerDelegateFactory("throwExceptionListener", ThrowExceptionListener::new);
        engine.registerListenerDelegateFactory("throwLinkTerminateListener", ThrowLinkTerminateListener::new);
        engine.registerListenerDelegateFactory("successResultListener", SuccessResultListener::new);
        engine.registerListenerDelegateFactory("scopeListener", ScopeListener::new);

        engine.registerDelegateInterceptorFactory(ActionRegexInterceptor::new);

        if (STD_OUT_SWITCH) {
            engine.registerDelegateInterceptorFactory(EmptyInterceptor::new);
            engine.registerDelegateInterceptorFactory(LogInterceptor::new);
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
        return engine.compile(content);
    }

    protected Promise<ExecutionInstance> startRule(String content, Map<String, Object> env) {
        System.out.println(content);
        return engine.startRule(new ExecutionCondition(content)
                .env(env));
    }

    protected Promise<ExecutionInstance> startRule(Rule rule, Map<String, Object> env) {
        return engine.startRule(new ExecutionCondition(rule)
                .env(env));
    }

    protected Promise<ExecutionInstance> startRule(Rule rule, Map<String, Object> env, List<Attribute> attributes) {
        return engine.startRule(new ExecutionCondition(rule)
                .env(env)
                .attributes(attributes));
    }
}
