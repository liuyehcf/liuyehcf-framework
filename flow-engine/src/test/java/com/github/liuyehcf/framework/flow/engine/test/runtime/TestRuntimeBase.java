package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
import com.github.liuyehcf.framework.flow.engine.runtime.constant.EnvCloneType;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Attribute;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.test.runtime.action.*;
import com.github.liuyehcf.framework.flow.engine.test.runtime.condition.*;
import com.github.liuyehcf.framework.flow.engine.test.runtime.interceptor.ActionRegexInterceptor;
import com.github.liuyehcf.framework.flow.engine.test.runtime.interceptor.EmptyInterceptor;
import com.github.liuyehcf.framework.flow.engine.test.runtime.interceptor.LogInterceptor;
import com.github.liuyehcf.framework.flow.engine.test.runtime.listener.*;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public static final Switch STD_OUT_SWITCH = new Switch(() -> false);
    protected static final Random RANDOM = new Random();
    public static final Switch PAUSE_SWITCH = new Switch(RANDOM::nextBoolean);
    public static final Switch IS_ACTION_ASYNC = new Switch(RANDOM::nextBoolean);
    public static final Switch IS_CONDITION_ASYNC = new Switch(RANDOM::nextBoolean);
    public static final Switch IS_LISTENER_ASYNC = new Switch(RANDOM::nextBoolean);
    protected static final int EXECUTE_TIMES = 1;
    protected static final FlowEngine engine;
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(16, 16, 5L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("FLOW-ENGINE-TEST-t-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        FlowProperties properties = new FlowProperties();
        properties.setEnvCloneType(EnvCloneType.hessian);
        engine = new DefaultFlowEngine(properties);

        engine.registerActionDelegateFactory("dash-name-action", DashNameAction::new);
        engine.registerActionDelegateFactory("dot.name.action", DotNameAction::new);
        engine.registerActionDelegateFactory("fieldConditionAction", FieldConditionAction::new);
        engine.registerActionDelegateFactory("getPropertyAction", GetPropertyAction::new);
        engine.registerActionDelegateFactory("markAction", MarkAction::new);
        engine.registerActionDelegateFactory("missingArgumentAction", MissingArgumentAction::new);
        engine.registerActionDelegateFactory("pauseAction", PauseAction::new);
        engine.registerActionDelegateFactory("printAction", PrintAction::new);
        engine.registerActionDelegateFactory("setPropertyAction", SetPropertyAction::new);
        engine.registerActionDelegateFactory("setTraceAttrAction", SetTraceAttrAction::new);
        engine.registerActionDelegateFactory("slash/name/action", SlashNameAction::new);
        engine.registerActionDelegateFactory("sleepAction", SleepAction::new);
        engine.registerActionDelegateFactory("throwExceptionAction", ThrowExceptionAction::new);
        engine.registerActionDelegateFactory("throwLinkTerminateAction", ThrowLinkTerminateAction::new);

        engine.registerConditionDelegateFactory("dash-name-condition", DashNameCondition::new);
        engine.registerConditionDelegateFactory("dot.name.condition", DotNameCondition::new);
        engine.registerConditionDelegateFactory("getPropertyCondition", GetPropertyCondition::new);
        engine.registerConditionDelegateFactory("missingArgumentCondition", MissingArgumentCondition::new);
        engine.registerConditionDelegateFactory("pauseCondition", PauseCondition::new);
        engine.registerConditionDelegateFactory("printCondition", PrintCondition::new);
        engine.registerConditionDelegateFactory("setPropertyCondition", SetPropertyCondition::new);
        engine.registerConditionDelegateFactory("setTraceAttrCondition", SetTraceAttrCondition::new);
        engine.registerConditionDelegateFactory("slash/name/condition", SlashNameCondition::new);
        engine.registerConditionDelegateFactory("sleepCondition", SleepCondition::new);
        engine.registerConditionDelegateFactory("throwExceptionCondition", ThrowExceptionCondition::new);
        engine.registerConditionDelegateFactory("throwLinkTerminateCondition", ThrowLinkTerminateCondition::new);

        engine.registerListenerDelegateFactory("dash-name-listener", DashNameListener::new);
        engine.registerListenerDelegateFactory("dot.name.listener", DotNameListener::new);
        engine.registerListenerDelegateFactory("getPropertyListener", GetPropertyListener::new);
        engine.registerListenerDelegateFactory("markListener", MarkListener::new);
        engine.registerListenerDelegateFactory("missingArgumentListener", MissingArgumentListener::new);
        engine.registerListenerDelegateFactory("pauseListener", PauseListener::new);
        engine.registerListenerDelegateFactory("printListener", PrintListener::new);
        engine.registerListenerDelegateFactory("scopeListener", ScopeListener::new);
        engine.registerListenerDelegateFactory("setPropertyListener", SetPropertyListener::new);
        engine.registerListenerDelegateFactory("setTraceAttrListener", SetTraceAttrListener::new);
        engine.registerListenerDelegateFactory("slash/name/listener", SlashNameListener::new);
        engine.registerListenerDelegateFactory("successResultListener", SuccessResultListener::new);
        engine.registerListenerDelegateFactory("throwExceptionListener", ThrowExceptionListener::new);
        engine.registerListenerDelegateFactory("throwLinkTerminateListener", ThrowLinkTerminateListener::new);

        engine.registerDelegateInterceptorFactory(ActionRegexInterceptor::new);

        if (STD_OUT_SWITCH.get()) {
            engine.registerDelegateInterceptorFactory(EmptyInterceptor::new);
            engine.registerDelegateInterceptorFactory(LogInterceptor::new);
        }
    }

    protected void assertPromise(Promise<ExecutionInstance> promise, boolean isCanceled, boolean isDone, boolean isSuccess, boolean isFailure) {
        if (promise.isFailure()) {
            Throwable cause = promise.cause();
            if (cause != null) {
                if (STD_OUT_SWITCH.get()) {
                    cause.printStackTrace();
                }
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

    protected Flow compile(String dsl) {
        System.out.println(dsl);
        Flow flow = engine.compile(dsl).get();
        String rebuildDsl = engine.decompile(flow).get();
        Flow rebuildFlow = engine.compile(rebuildDsl).get();
        Assert.assertTrue(isSame(flow, rebuildFlow));
        return flow;
    }

    protected Promise<ExecutionInstance> startFlow(String content, Map<String, Object> env) {
        System.out.println(content);
        return engine.startFlow(new ExecutionCondition(content)
                .env(env));
    }

    protected Promise<ExecutionInstance> startFlow(Flow flow, Map<String, Object> env) {
        return engine.startFlow(new ExecutionCondition(flow)
                .env(env));
    }

    protected Promise<ExecutionInstance> startFlow(Flow flow, Map<String, Object> env, List<Attribute> attributes) {
        return engine.startFlow(new ExecutionCondition(flow)
                .env(env)
                .attributes(attributes));
    }

    private boolean isSame(Flow flow1, Flow flow2) {
        for (ElementType elementType : ElementType.values()) {
            long count1 = flow1.getElements().stream()
                    .filter(e -> Objects.equals(elementType, e.getType()))
                    .count();

            long count2 = flow2.getElements().stream()
                    .filter(e -> Objects.equals(elementType, e.getType()))
                    .count();

            if (count1 != count2) {
                return false;
            }
        }

        return true;
    }

    private String simplify(String dsl) {
        StringBuilder buffer = new StringBuilder();
        boolean inStringContext = false;
        for (char c : dsl.toCharArray()) {
            if (c == '\"') {
                buffer.append(c);
                inStringContext = !inStringContext;
                continue;
            }
            if (inStringContext) {
                buffer.append(c);
            } else {
                if (c == ' ' || c == '\n' || c == '\t') {
                    continue;
                }
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
