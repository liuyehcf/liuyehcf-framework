package com.github.liuyehcf.framework.rule.engine;

import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public interface RuleEngine {

    /**
     * get rule properties
     *
     * @return rule properties
     */
    RuleProperties getProperties();

    /**
     * register delegateInterceptor factory
     *
     * @param factory delegate Interceptor factory
     */
    void registerDelegateInterceptorFactory(Factory<DelegateInterceptor> factory);

    /**
     * unregister delegateInterceptor factory
     *
     * @param factory delegate Interceptor factory
     */
    void unregisterDelegateInterceptorFactory(Factory<DelegateInterceptor> factory);

    /**
     * get all delegate interceptor factories
     *
     * @return delegate interceptor factories
     */
    List<Factory<DelegateInterceptor>> getDelegateInterceptorFactories();

    /**
     * register action delegate factory
     *
     * @param actionName action name
     * @param factory    action delegate factory
     */
    void registerActionDelegateFactory(String actionName, Factory<ActionDelegate> factory);

    /**
     * unregister action delegate factory
     *
     * @param actionName action name
     * @return removed action delegate factory
     */
    Factory<ActionDelegate> unregisterActionDelegateFactory(String actionName);

    /**
     * get action delegate factory by action name
     *
     * @param actionName action name
     * @return action delegate factory
     */
    Factory<ActionDelegate> getActionDelegateFactory(String actionName);

    /**
     * register condition delegate factory
     *
     * @param conditionName condition name
     * @param factory       condition delegate factory
     */
    void registerConditionDelegateFactory(String conditionName, Factory<ConditionDelegate> factory);

    /**
     * unregister condition delegate factory
     *
     * @param conditionName condition name
     * @return removed condition delegate factory
     */
    Factory<ConditionDelegate> unregisterConditionDelegateFactory(String conditionName);

    /**
     * get condition delegate factory by condition name
     *
     * @param conditionName condition name
     * @return condition delegate factory
     */
    Factory<ConditionDelegate> getConditionDelegateFactory(String conditionName);

    /**
     * register listener delegate factory
     *
     * @param listenerName            listener name
     * @param listenerDelegateFactory listener delegate factory
     */
    void registerListenerDelegateFactory(String listenerName, Factory<ListenerDelegate> listenerDelegateFactory);

    /**
     * unregister listener delegate factory
     *
     * @param listenerName listener name
     * @return removed listener delegate factory
     */
    Factory<ListenerDelegate> unregisterListenerDelegateFactory(String listenerName);

    /**
     * get listener delegate factory by listener name
     *
     * @param listenerName listener name
     * @return listener delegate factory
     */
    Factory<ListenerDelegate> getListenerDelegateFactory(String listenerName);

    /**
     * get rule execution async executor
     *
     * @return executor
     */
    ExecutorService getExecutor();

    /**
     * get rule scheduled async executor
     *
     * @return schedulerExecutor
     */
    ScheduledExecutorService getScheduledExecutor();

    /**
     * get action delegate by action name
     *
     * @param actionName action name
     * @return action delegate
     */
    ActionDelegate getActionDelegate(String actionName);

    /**
     * get condition delegate by condition name
     *
     * @param conditionName condition name
     * @return condition delegate
     */
    ConditionDelegate getConditionDelegate(String conditionName);

    /**
     * get listener delegate by listener name
     *
     * @param listenerName listener name
     * @return listener delegate
     */
    ListenerDelegate getListenerDelegate(String listenerName);

    /**
     * compile dsl to rule
     *
     * @param dsl rule dsl
     * @return rule
     */
    Rule compile(String dsl);

    /**
     * start rule
     *
     * @param rule rule
     * @param env  rule environment
     * @return promise
     */
    Promise<ExecutionInstance> startRule(Rule rule, Map<String, Object> env);

    /**
     * start rule with specified instanceId
     *
     * @param rule       rule
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    Promise<ExecutionInstance> startRule(Rule rule, String instanceId, Map<String, Object> env);

    /**
     * compile and start rule with specified instanceId and executionIdGenerator
     *
     * @param rule       rule
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    Promise<ExecutionInstance> startRule(Rule rule, String instanceId, Map<String, Object> env, AtomicLong executionIdGenerator);

    /**
     * compile and start rule
     *
     * @param dsl rule dsl
     * @param env rule environment
     * @return promise
     */
    Promise<ExecutionInstance> startRule(String dsl, Map<String, Object> env);

    /**
     * compile and start rule with specified instanceId
     *
     * @param dsl        rule dsl
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    Promise<ExecutionInstance> startRule(String dsl, String instanceId, Map<String, Object> env);

    /**
     * compile and start rule with specified instanceId and executionIdGenerator
     *
     * @param dsl        rule dsl
     * @param instanceId specified instanceId
     * @param env        rule environment
     * @return promise
     */
    Promise<ExecutionInstance> startRule(String dsl, String instanceId, Map<String, Object> env, AtomicLong executionIdGenerator);

    /**
     * shutdown the whole rule engine gracefully
     */
    void shutdown();

    /**
     * shutdown the whole rule engine right now
     */
    void shutdownNow();
}
