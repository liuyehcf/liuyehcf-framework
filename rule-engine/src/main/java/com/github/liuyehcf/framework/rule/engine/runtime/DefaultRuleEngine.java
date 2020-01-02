package com.github.liuyehcf.framework.rule.engine.runtime;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.ExecutionCondition;
import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.RulePromise;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.GlobalBeforeListenerOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.DefaultOperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.ClusterEventLoop;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.DefaultClusterEventLoop;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.util.TopoUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
public class DefaultRuleEngine implements RuleEngine {

    /**
     * dsl compiler
     */
    private static final DslCompiler COMPILER = DslCompiler.getInstance();

    /**
     * properties
     */
    private final RuleProperties properties;

    /**
     * delegate interceptor's factories
     */
    private final List<Factory<DelegateInterceptor>> delegateInterceptorFactories = Lists.newCopyOnWriteArrayList();

    /**
     * delegate's factories, including actionDelegate, conditionDelegate, listenerDelegate
     */
    private final Map<String, Factory<ActionDelegate>> actionDelegateFactories = Maps.newConcurrentMap();
    private final Map<String, Factory<ConditionDelegate>> conditionDelegateFactories = Maps.newConcurrentMap();
    private final Map<String, Factory<ListenerDelegate>> listenerDelegateFactories = Maps.newConcurrentMap();

    /**
     * thread pool
     * executor used for normal operation's execution
     * scheduledExecutor used for delegate's timeout async execution
     */
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutor;

    /**
     * cluster event loop, used for cluster mode
     */
    private final ClusterEventLoop clusterEventLoop;

    public DefaultRuleEngine(RuleProperties properties, ExecutorService executor, ScheduledExecutorService scheduledExecutor) {
        if (properties == null) {
            this.properties = new RuleProperties();
        } else {
            this.properties = properties;
        }

        if (executor == null) {
            this.executor = new ThreadPoolExecutor(
                    16, 128,
                    5L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1024),
                    new ThreadFactoryBuilder().setNameFormat("RULE-ENGINE-THREAD-POOL-t-%d").build(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        } else {
            this.executor = executor;
        }

        if (scheduledExecutor == null) {
            this.scheduledExecutor = new ScheduledThreadPoolExecutor(
                    16,
                    new ThreadFactoryBuilder().setNameFormat("RULE-ENGINE-SCHEDULER-THREAD-POOL-t-%d").build(),
                    new ThreadPoolExecutor.DiscardPolicy());
        } else {
            this.scheduledExecutor = scheduledExecutor;
        }

        if (getProperties().isClusterMode()) {
            clusterEventLoop = new DefaultClusterEventLoop(this);
        } else {
            clusterEventLoop = null;
        }
    }

    public DefaultRuleEngine(RuleProperties properties) {
        this(properties, null, null);
    }

    public DefaultRuleEngine() {
        this(null, null, null);
    }

    @Override
    public final RuleProperties getProperties() {
        return properties;
    }

    @Override
    public final void registerDelegateInterceptorFactory(Factory<DelegateInterceptor> factory) {
        Assert.assertNotNull(factory, "factory");
        delegateInterceptorFactories.add(factory);
    }

    @Override
    public final void unregisterDelegateInterceptorFactory(Factory<DelegateInterceptor> factory) {
        Assert.assertNotNull(factory, "factory");
        delegateInterceptorFactories.remove(factory);
    }

    @Override
    public final List<Factory<DelegateInterceptor>> getDelegateInterceptorFactories() {
        return delegateInterceptorFactories;
    }

    @Override
    public final void registerActionDelegateFactory(String actionName, Factory<ActionDelegate> factory) {
        Assert.assertNotNull(actionName, "actionName");
        Assert.assertNotNull(factory, "factory");
        Assert.assertFalse(actionDelegateFactories.containsKey(actionName));
        actionDelegateFactories.put(actionName, factory);
    }

    @Override
    public final Factory<ActionDelegate> unregisterActionDelegateFactory(String actionName) {
        Assert.assertNotNull(actionName, "actionName");
        return actionDelegateFactories.remove(actionName);
    }

    @Override
    public final Factory<ActionDelegate> getActionDelegateFactory(String actionName) {
        Assert.assertNotNull(actionName, "actionName");
        return actionDelegateFactories.get(actionName);
    }

    @Override
    public final void registerConditionDelegateFactory(String conditionName, Factory<ConditionDelegate> factory) {
        Assert.assertNotNull(conditionName, "conditionName");
        Assert.assertNotNull(factory, "factory");
        Assert.assertFalse(conditionDelegateFactories.containsKey(conditionName));
        conditionDelegateFactories.put(conditionName, factory);
    }

    @Override
    public final Factory<ConditionDelegate> unregisterConditionDelegateFactory(String conditionName) {
        Assert.assertNotNull(conditionName, "conditionName");
        return conditionDelegateFactories.remove(conditionName);
    }

    @Override
    public final Factory<ConditionDelegate> getConditionDelegateFactory(String conditionName) {
        Assert.assertNotNull(conditionName, "conditionName");
        return conditionDelegateFactories.get(conditionName);
    }

    @Override
    public void registerListenerDelegateFactory(String listenerName, Factory<ListenerDelegate> listenerDelegateFactory) {
        Assert.assertNotNull(listenerName, "listenerName");
        Assert.assertNotNull(listenerDelegateFactory, "listenerDelegateFactory");
        Assert.assertFalse(listenerDelegateFactories.containsKey(listenerName));
        listenerDelegateFactories.put(listenerName, listenerDelegateFactory);
    }

    @Override
    public final Factory<ListenerDelegate> unregisterListenerDelegateFactory(String listenerName) {
        Assert.assertNotNull(listenerName, "listenerName");
        return listenerDelegateFactories.remove(listenerName);
    }

    @Override
    public final Factory<ListenerDelegate> getListenerDelegateFactory(String listenerName) {
        Assert.assertNotNull(listenerName, "listenerName");
        return listenerDelegateFactories.get(listenerName);
    }

    @Override
    public final ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public final ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    @Override
    public final ActionDelegate getActionDelegate(String actionName) {
        Factory<ActionDelegate> actionDelegateFactory = getActionDelegateFactory(actionName);
        Assert.assertNotNull(actionDelegateFactory, () -> String.format("unregistered action '%s'", actionName));

        return actionDelegateFactory.create();
    }

    @Override
    public final ConditionDelegate getConditionDelegate(String conditionName) {
        Factory<ConditionDelegate> conditionDelegateFactory = getConditionDelegateFactory(conditionName);
        Assert.assertNotNull(conditionDelegateFactory, () -> String.format("unregistered condition '%s'", conditionName));

        return conditionDelegateFactory.create();
    }

    @Override
    public final ListenerDelegate getListenerDelegate(String listenerName) {
        Factory<ListenerDelegate> listenerDelegateFactory = getListenerDelegateFactory(listenerName);
        Assert.assertNotNull(listenerDelegateFactory, () -> String.format("unregistered listener '%s'", listenerName));

        return listenerDelegateFactory.create();
    }

    @Override
    public final Rule compile(String dsl) {
        return doCompile(dsl);
    }

    @Override
    public Promise<ExecutionInstance> startRule(ExecutionCondition executionCondition) {
        Promise<ExecutionInstance> promise = new RulePromise();

        try {
            Rule rule = executionCondition.getRule();
            if (rule == null) {
                rule = doCompile(executionCondition.getDsl());
            }
            rule.init();

            Map<String, Object> env = executionCondition.getEnv();
            AtomicLong executionIdGenerator = executionCondition.getExecutionIdGenerator();

            OperationContext operationContext = new DefaultOperationContext(this,
                    rule,
                    TopoUtils.isSingleLinkRule(rule),
                    executionCondition.getInstanceId(),
                    env == null ? Maps.newHashMap() : env,
                    executionCondition.getAttributes(),
                    executionIdGenerator == null ? new AtomicLong(0) : executionIdGenerator,
                    promise);
            operationContext.executeAsync(new GlobalBeforeListenerOperation(operationContext));
        } catch (Throwable e) {
            promise.tryFailure(e);
        }

        return promise;
    }

    @Override
    public final void shutdown() {
        executor.shutdown();
        scheduledExecutor.shutdown();
        if (clusterEventLoop != null) {
            clusterEventLoop.shutdown();
        }
    }

    @Override
    public final void shutdownNow() {
        executor.shutdownNow();
        scheduledExecutor.shutdownNow();
        if (clusterEventLoop != null) {
            clusterEventLoop.shutdown();
        }
    }

    private Rule doCompile(String dsl) {
        CompileResult<Rule> compile = COMPILER.compile(dsl);

        if (!compile.isSuccess()) {
            throw new RuleException(RuleErrorCode.COMPILE, compile.getError());
        }

        return compile.getResult();
    }
}
