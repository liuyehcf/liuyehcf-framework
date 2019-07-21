package com.github.liuyehcf.framework.rule.engine;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.RulePromise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.StartOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.DefaultOperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * todo
 * 1. global listener 并发执行，变量update的断言
 * 2. if() 的trace以及拓扑测试用例
 *
 * @author hechenfeng
 * @date 2019/4/23
 */
public abstract class RuleEngine {

    private static final DslCompiler COMPILER = DslCompiler.getInstance();

    private static final List<Factory<DelegateInterceptor>> DELEGATE_INTERCEPTOR_FACTORIES = Lists.newCopyOnWriteArrayList();

    private static final Map<String, Factory<ActionDelegate>> ACTION_DELEGATE_FACTORIES = Maps.newConcurrentMap();
    private static final Map<String, Factory<ConditionDelegate>> CONDITION_DELEGATE_FACTORIES = Maps.newConcurrentMap();
    private static final Map<String, Factory<ListenerDelegate>> LISTENER_DELEGATE_FACTORIES = Maps.newConcurrentMap();

    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat(
            "RULE-ENGINE-ASYNC-t-%d").build();
    private static Executor executor = new ThreadPoolExecutor(16, 128, 5L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024), NAMED_THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * register delegateInterceptor factory
     *
     * @param factory delegate Interceptor factory
     */
    public static void registerDelegateInterceptorFactory(Factory<DelegateInterceptor> factory) {
        Assert.assertNotNull(factory);
        DELEGATE_INTERCEPTOR_FACTORIES.add(factory);
    }

    /**
     * unregister delegateInterceptor factory
     *
     * @param factory delegate Interceptor factory
     */
    public static void unregisterDelegateInterceptorFactory(Factory<DelegateInterceptor> factory) {
        Assert.assertNotNull(factory);
        DELEGATE_INTERCEPTOR_FACTORIES.remove(factory);
    }

    /**
     * get all delegate interceptor factories
     *
     * @return delegate interceptor factories
     */
    public static List<Factory<DelegateInterceptor>> getDelegateInterceptorFactories() {
        return DELEGATE_INTERCEPTOR_FACTORIES;
    }

    /**
     * register action delegate factory
     *
     * @param actionName action name
     * @param factory    action delegate factory
     */
    public static void registerActionDelegateFactory(String actionName, Factory<ActionDelegate> factory) {
        Assert.assertNotNull(actionName);
        Assert.assertNotNull(factory);
        Assert.assertFalse(ACTION_DELEGATE_FACTORIES.containsKey(actionName));
        ACTION_DELEGATE_FACTORIES.put(actionName, factory);
    }

    /**
     * unregister action delegate factory
     *
     * @param actionName action name
     * @return removed action delegate factory
     */
    public static Factory<ActionDelegate> unregisterActionDelegateFactory(String actionName) {
        Assert.assertNotNull(actionName);
        return ACTION_DELEGATE_FACTORIES.remove(actionName);
    }

    /**
     * get action delegate factory by action name
     *
     * @param actionName action name
     * @return action delegate factory
     */
    public static Factory<ActionDelegate> getActionDelegateFactory(String actionName) {
        Assert.assertNotNull(actionName);
        return ACTION_DELEGATE_FACTORIES.get(actionName);
    }

    /**
     * register condition delegate factory
     *
     * @param conditionName condition name
     * @param factory       condition delegate factory
     */
    public static void registerConditionDelegateFactory(String conditionName, Factory<ConditionDelegate> factory) {
        Assert.assertNotNull(conditionName);
        Assert.assertNotNull(factory);
        Assert.assertFalse(CONDITION_DELEGATE_FACTORIES.containsKey(conditionName));
        CONDITION_DELEGATE_FACTORIES.put(conditionName, factory);
    }

    /**
     * unregister condition delegate factory
     *
     * @param conditionName condition name
     * @return removed condition delegate factory
     */
    public static Factory<ConditionDelegate> unregisterConditionDelegateFactory(String conditionName) {
        Assert.assertNotNull(conditionName);
        return CONDITION_DELEGATE_FACTORIES.remove(conditionName);
    }

    /**
     * get condition delegate factory by condition name
     *
     * @param conditionName condition name
     * @return condition delegate factory
     */
    public static Factory<ConditionDelegate> getConditionDelegateFactory(String conditionName) {
        Assert.assertNotNull(conditionName);
        return CONDITION_DELEGATE_FACTORIES.get(conditionName);
    }

    /**
     * register listener delegate factory
     *
     * @param listenerName            listener name
     * @param listenerDelegateFactory listener delegate factory
     */
    public static void registerListenerDelegateFactory(String listenerName, Factory<ListenerDelegate> listenerDelegateFactory) {
        Assert.assertNotNull(listenerName);
        Assert.assertNotNull(listenerDelegateFactory);
        Assert.assertFalse(LISTENER_DELEGATE_FACTORIES.containsKey(listenerName));
        LISTENER_DELEGATE_FACTORIES.put(listenerName, listenerDelegateFactory);
    }

    /**
     * unregister listener delegate factory
     *
     * @param listenerName listener name
     * @return removed listener delegate factory
     */
    public static Factory<ListenerDelegate> unregisterListenerDelegateFactory(String listenerName) {
        Assert.assertNotNull(listenerName);
        return LISTENER_DELEGATE_FACTORIES.remove(listenerName);
    }

    /**
     * get listener delegate factory by listener name
     *
     * @param listenerName listener name
     * @return listener delegate factory
     */
    public static Factory<ListenerDelegate> getListenerDelegateFactory(String listenerName) {
        Assert.assertNotNull(listenerName);
        return LISTENER_DELEGATE_FACTORIES.get(listenerName);
    }

    /**
     * get rule execution async executor
     *
     * @return executor
     */
    public static Executor getExecutor() {
        return executor;
    }

    /**
     * set rule execution async executor
     *
     * @param executor executor
     */
    public static void setExecutor(Executor executor) {
        Assert.assertNotNull(executor);
        RuleEngine.executor = executor;
    }

    /**
     * get action delegate by action name
     *
     * @param actionName action name
     * @return action delegate
     */
    public static ActionDelegate getActionDelegate(String actionName) {
        Factory<ActionDelegate> actionDelegateFactory = getActionDelegateFactory(actionName);
        Assert.assertNotNull(actionDelegateFactory, String.format("unregistered action '%s'", actionName));

        return actionDelegateFactory.create();
    }

    /**
     * get condition delegate by condition name
     *
     * @param conditionName condition name
     * @return condition delegate
     */
    public static ConditionDelegate getConditionDelegate(String conditionName) {
        Factory<ConditionDelegate> conditionDelegateFactory = getConditionDelegateFactory(conditionName);
        Assert.assertNotNull(conditionDelegateFactory, String.format("unregistered condition '%s'", conditionName));

        return conditionDelegateFactory.create();
    }

    /**
     * get listener delegate by listener name
     *
     * @param listenerName listener name
     * @return listener delegate
     */
    public static ListenerDelegate getListenerDelegate(String listenerName) {
        Factory<ListenerDelegate> listenerDelegateFactory = getListenerDelegateFactory(listenerName);
        Assert.assertNotNull(listenerDelegateFactory, String.format("unregistered listener '%s'", listenerName));

        return listenerDelegateFactory.create();
    }

    /**
     * compile dsl to rule
     *
     * @param dsl rule dsl
     * @return rule
     */
    public static Rule compile(String dsl) {
        return doCompile(dsl);
    }

    /**
     * start rule
     *
     * @param rule rule
     * @param env  rule environment
     * @return promise
     */
    public static Promise<ExecutionInstance> startRule(Rule rule, Map<String, Object> env) {
        return doStart(() -> rule, null, env, null);
    }

    /**
     * start rule with specified instanceId
     *
     * @param rule       rule
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    public static Promise<ExecutionInstance> startRule(Rule rule, String instanceId, Map<String, Object> env) {
        return doStart(() -> rule, instanceId, env, null);
    }

    /**
     * compile and start rule with specified instanceId and executionIdGenerator
     *
     * @param rule       rule
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    public static Promise<ExecutionInstance> startRule(Rule rule, String instanceId, Map<String, Object> env, AtomicLong executionIdGenerator) {
        return doStart(() -> rule, instanceId, env, executionIdGenerator);
    }

    /**
     * compile and start rule
     *
     * @param dsl rule dsl
     * @param env rule environment
     * @return promise
     */
    public static Promise<ExecutionInstance> startRule(String dsl, Map<String, Object> env) {
        return doStart(() -> doCompile(dsl), null, env, null);
    }

    /**
     * compile and start rule with specified instanceId
     *
     * @param dsl        rule dsl
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    public static Promise<ExecutionInstance> startRule(String dsl, String instanceId, Map<String, Object> env) {
        return doStart(() -> doCompile(dsl), instanceId, env, null);
    }

    /**
     * compile and start rule with specified instanceId and executionIdGenerator
     *
     * @param dsl        rule dsl
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    public static Promise<ExecutionInstance> startRule(String dsl, String instanceId, Map<String, Object> env, AtomicLong executionIdGenerator) {
        return doStart(() -> doCompile(dsl), instanceId, env, executionIdGenerator);
    }

    private static Rule doCompile(String dsl) {
        CompileResult<Rule> compile = COMPILER.compile(dsl);

        if (!compile.isSuccess()) {
            throw new RuleException(RuleErrorCode.COMPILE, compile.getError());
        }

        return compile.getResult();
    }

    private static Promise<ExecutionInstance> doStart(Callable<Rule> callable, String instanceId, Map<String, Object> env, AtomicLong executionIdGenerator) {
        Promise<ExecutionInstance> promise = new RulePromise();

        try {
            Rule rule = callable.call();
            rule.init();
            OperationContext operationContext = new DefaultOperationContext(rule,
                    instanceId,
                    env == null ? Maps.newHashMap() : env,
                    executionIdGenerator == null ? new AtomicLong(0) : executionIdGenerator,
                    promise);
            operationContext.executeAsync(new StartOperation(operationContext));
        } catch (Throwable e) {
            promise.tryFailure(e);
        }

        return promise;
    }
}
