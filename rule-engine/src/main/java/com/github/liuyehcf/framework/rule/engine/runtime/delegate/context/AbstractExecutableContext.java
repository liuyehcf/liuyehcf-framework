package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.Executable;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegatePromise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public abstract class AbstractExecutableContext<E extends Element> implements ExecutableContext<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutableContext.class);

    final Element element;
    private final Rule rule;
    private final String instanceId;
    private final String linkId;
    private final long executionId;
    private final Map<String, Object> env;
    private final Map<String, Attribute> localAttributes = Maps.newConcurrentMap();
    private final Map<String, Attribute> globalAttributes;
    private final List<PropertyUpdate> propertyUpdates = Lists.newCopyOnWriteArrayList();
    private final Promise<ExecutionInstance> rulePromise;
    private final AtomicBoolean isAsync = new AtomicBoolean(false);
    private final AtomicReference<DelegatePromise> delegatePromise = new AtomicReference<>(null);

    AbstractExecutableContext(Element element, String instanceId, String linkId, long executionId,
                              Map<String, Object> env, Map<String, Attribute> globalAttributes,
                              Promise<ExecutionInstance> rulePromise) {
        Assert.assertNotNull(element);
        Assert.assertNotNull(instanceId);
        Assert.assertNotNull(linkId);
        Assert.assertNotNull(env);
        Assert.assertNotNull(globalAttributes);
        Assert.assertNotNull(rulePromise);
        this.element = element;
        this.rule = this.element.getRule();
        this.instanceId = instanceId;
        this.linkId = linkId;
        this.executionId = executionId;
        this.env = env;
        this.globalAttributes = globalAttributes;
        this.rulePromise = rulePromise;
    }

    @Override
    public final String getRuleName() {
        return rule.getName();
    }

    @Override
    public final String getRuleId() {
        return rule.getId();
    }

    @Override
    public final String getInstanceId() {
        return instanceId;
    }

    @Override
    public final String getLinkId() {
        return linkId;
    }

    @Override
    public final long getExecutionId() {
        return executionId;
    }

    @Override
    public final Rule getRule() {
        return rule;
    }

    @Override
    public final String getName() {
        return ((Executable) element).getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getProperty(String name) {
        try {
            Assert.assertNotNull(name);
            return (T) PropertyUtils.getProperty(env, name);
        } catch (Exception e) {
            throw new RuleException(RuleErrorCode.PROPERTY, String.format("propertyName='%s'", name), e);
        }
    }

    @Override
    public final <T> void setProperty(String name, T value) {
        try {
            Assert.assertNotNull(name);
            Assert.assertNotNull(value);
            Object oldValue = getProperty(name);
            PropertyUtils.setProperty(env, name, value);

            PropertyUpdateType updateType;
            if (oldValue == null) {
                updateType = PropertyUpdateType.CREATE;
            } else {
                updateType = PropertyUpdateType.UPDATE;
            }

            propertyUpdates.add(new DefaultPropertyUpdate(updateType, name, oldValue, value));
        } catch (Exception e) {
            throw new RuleException(RuleErrorCode.PROPERTY, String.format("propertyName='%s'", name), e);
        }
    }

    @Override
    public final List<PropertyUpdate> getPropertyUpdates() {
        return propertyUpdates;
    }

    @Override
    public final Map<String, Object> getEnv() {
        return env;
    }

    @Override
    public final boolean hasLocalAttribute(String name) {
        return localAttributes.containsKey(name);
    }

    @Override
    public final <T> void addLocalAttribute(String name, T value) {
        Attribute previous = localAttributes.put(name, new DefaultAttribute(name, value));
        if (previous != null) {
            LOGGER.warn("local attribute is replaced. ruleId={}; elementId={}; previousAttribute={}",
                    getRuleId(), getElement().getId(), JSON.toJSONString(previous));
        }
    }

    @Override
    public final <T> Attribute addLocalAttributeIfAbsent(String name, T value) {
        return localAttributes.putIfAbsent(name, new DefaultAttribute(name, value));
    }

    @Override
    public final Attribute getLocalAttribute(String name) {
        return localAttributes.get(name);
    }

    @Override
    public final Map<String, Attribute> getLocalAttributes() {
        return localAttributes;
    }

    @Override
    public final boolean hasGlobalAttribute(String name) {
        return globalAttributes.containsKey(name);
    }

    @Override
    public final <T> void addGlobalAttribute(String name, T value) {
        Attribute previous = globalAttributes.put(name, new DefaultAttribute(name, value));
        if (previous != null) {
            LOGGER.warn("global attribute is replaced. ruleId={}; previousAttribute={}",
                    getRuleId(), JSON.toJSONString(previous));
        }
    }

    @Override
    public final <T> Attribute addGlobalAttributeIfAbsent(String name, T value) {
        return globalAttributes.putIfAbsent(name, new DefaultAttribute(name, value));
    }

    @Override
    public final Attribute getGlobalAttribute(String name) {
        return globalAttributes.get(name);
    }

    @Override
    public final Map<String, Attribute> getGlobalAttributes() {
        return globalAttributes;
    }

    @Override
    public final void executeAsync(Executor externalExecutor, Callable<?> callable) {
        if (isAsync.compareAndSet(false, true)
                && delegatePromise.compareAndSet(null, new DelegatePromise())) {

            rulePromise.addListener((rulePromise) ->
                    delegatePromise.get().tryFailure(new RuleException(RuleErrorCode.RULE_ALREADY_FINISHED, rulePromise.cause()))
            );

            try {
                externalExecutor.execute(() -> {
                    Throwable cause = null;
                    Object result = null;
                    try {
                        result = callable.call();
                    } catch (Throwable e) {
                        cause = e;
                    } finally {
                        if (cause != null) {
                            delegatePromise.get().tryFailure(cause);
                        } else {
                            delegatePromise.get().trySuccess(result);
                        }
                    }
                });
            } catch (Throwable e) {
                delegatePromise.get().tryFailure(e);
            }
        }
    }

    @Override
    public final boolean isAsync() {
        return isAsync.get();
    }

    @Override
    public DelegatePromise getDelegatePromise() {
        return delegatePromise.get();
    }
}
