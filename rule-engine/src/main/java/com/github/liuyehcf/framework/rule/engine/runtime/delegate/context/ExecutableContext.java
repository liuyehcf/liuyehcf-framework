package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegatePromise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Attribute;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.PropertyUpdate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public interface ExecutableContext<E extends Element> {

    /**
     * get rule name
     *
     * @return rule name
     */
    String getRuleName();

    /**
     * get rule id
     *
     * @return rule id
     */
    String getRuleId();

    /**
     * get instanceId of this rule execution
     *
     * @return instanceId
     */
    String getInstanceId();

    /**
     * get linkId of this link execution
     *
     * @return linkId
     */
    String getLinkId();

    /**
     * get element executionId
     *
     * @return executionId
     */
    long getExecutionId();

    /**
     * get rule
     *
     * @return rule
     */
    Rule getRule();

    /**
     * return current element of this execution
     *
     * @return current element
     */
    E getElement();

    /**
     * get name of this delegate
     *
     * @return delegate name
     */
    String getName();

    /**
     * get property from env
     *
     * @param name property name, support multiple levels
     * @param <T>  property value's type
     * @return property value
     */
    <T> T getProperty(String name);

    /**
     * set or update property of env
     *
     * @param name  property name
     * @param value property value
     * @param <T>   property value's type
     */
    <T> void setProperty(String name, T value);

    /**
     * get property updates by this delegate
     *
     * @return property updates
     */
    List<PropertyUpdate> getPropertyUpdates();

    /**
     * get env of this execution link
     * any change of this env is not recorded to trace
     *
     * @return env
     */
    Map<String, Object> getEnv();

    /**
     * whether has the specified node level attribute
     *
     * @param name attr name
     */
    boolean hasLocalAttribute(String name);

    /**
     * add node level attribute, which can be got from trace
     *
     * @param name  attr name
     * @param value attr value
     * @param <T>   attr value's type
     * @see com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace
     */
    <T> void addLocalAttribute(String name, T value);

    /**
     * add node level attribute to trace if name is absent
     *
     * @param name  attr name
     * @param value attr value
     * @param <T>   attr value's type
     * @return old attr value
     */
    <T> Attribute addLocalAttributeIfAbsent(String name, T value);

    /**
     * get node level attribute of specified name
     *
     * @param name attr name
     * @return attribute
     */
    Attribute getLocalAttribute(String name);

    /**
     * get all node level attributes
     *
     * @return attributes
     */
    Map<String, Attribute> getLocalAttributes();

    /**
     * whether has the specified rule level attribute
     *
     * @param name attr name
     */
    boolean hasGlobalAttribute(String name);

    /**
     * add rule level attribute, which can be got from executionInstance
     *
     * @param name  attr name
     * @param value attr value
     * @param <T>   attr value's type
     * @see com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance
     */
    <T> void addGlobalAttribute(String name, T value);

    /**
     * add rule level attribute to trace if name is absent
     *
     * @param name  attr name
     * @param value attr value
     * @param <T>   attr value's type
     * @return old attr value
     */
    <T> Attribute addGlobalAttributeIfAbsent(String name, T value);

    /**
     * get rule level attribute of specified name
     *
     * @param name attr name
     * @return attribute
     */
    Attribute getGlobalAttribute(String name);

    /**
     * get all rule level attributes
     *
     * @return attributes
     */
    Map<String, Attribute> getGlobalAttributes();

    /**
     * execute delegate async
     *
     * @param externalExecutor user's external executor
     * @param callable         asynchronous Execution Logic
     */
    void executeAsync(Executor externalExecutor, Callable<?> callable);

    /**
     * whether execution is async
     */
    boolean isAsync();

    /**
     * get delegate promise
     * null if not async
     *
     * @return delegate promise
     */
    DelegatePromise getDelegatePromise();
}
