package com.github.liuyehcf.framework.flow.engine;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public interface FlowEngine {

    /**
     * get flow properties
     *
     * @return flow properties
     */
    FlowProperties getProperties();

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
     * get flow execution async executor
     *
     * @return executor
     */
    ExecutorService getExecutor();

    /**
     * get flow scheduled async executor
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
     * compile dsl to flow
     *
     * @param dsl flow dsl
     * @return flow
     */
    Promise<Flow> compile(String dsl);

    /**
     * decompile flow to dsl
     *
     * @param flow flow
     * @return flow dsl
     */
    Promise<String> decompile(Flow flow);

    /**
     * start flow
     *
     * @param executionCondition flow's execution condition
     * @return promise
     */
    Promise<ExecutionInstance> startFlow(ExecutionCondition executionCondition);

    /**
     * shutdown the whole flow engine gracefully
     */
    void shutdown();

    /**
     * shutdown the whole flow engine right now
     */
    void shutdownNow();
}
