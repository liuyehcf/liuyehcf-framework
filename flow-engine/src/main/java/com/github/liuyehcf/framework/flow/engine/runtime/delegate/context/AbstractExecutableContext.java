package com.github.liuyehcf.framework.flow.engine.runtime.delegate.context;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.model.Element;
import com.github.liuyehcf.framework.flow.engine.model.Executable;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.promise.ExecutionLinkPausePromise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public abstract class AbstractExecutableContext<E extends Element> implements ExecutableContext<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutableContext.class);

    final Element element;
    private final Promise<ExecutionInstance> promise;
    private final Flow flow;
    private final String instanceId;
    private final String linkId;
    private final long executionId;
    private final Map<String, Object> env;
    private final Map<String, Attribute> localAttributes = Maps.newConcurrentMap();
    private final Map<String, Attribute> globalAttributes;
    private final List<PropertyUpdate> propertyUpdates = Lists.newCopyOnWriteArrayList();
    private ExecutionLinkPausePromise executionLinkPausePromise;

    AbstractExecutableContext(Element element, Promise<ExecutionInstance> promise, String instanceId, String linkId, long executionId,
                              Map<String, Object> env, Map<String, Attribute> globalAttributes) {
        Assert.assertNotNull(element, "element");
        Assert.assertNotNull(promise, "promise");
        Assert.assertNotNull(instanceId, "instanceId");
        Assert.assertNotNull(linkId, "linkId");
        Assert.assertNotNull(env, "env");
        Assert.assertNotNull(globalAttributes, "globalAttributes");
        this.element = element;
        this.promise = promise;
        this.flow = this.element.getFlow();
        this.instanceId = instanceId;
        this.linkId = linkId;
        this.executionId = executionId;
        this.env = env;
        this.globalAttributes = globalAttributes;
    }

    @Override
    public final String getFlowName() {
        return flow.getName();
    }

    @Override
    public final String getFlowId() {
        return flow.getId();
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
    public final Flow getFlow() {
        return flow;
    }

    @Override
    public final String getName() {
        return ((Executable) element).getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getProperty(String name) {
        try {
            Assert.assertNotNull(name, "name");
            return (T) PropertyUtils.getProperty(env, name);
        } catch (Exception e) {
            throw new FlowException(FlowErrorCode.PROPERTY, String.format("propertyName='%s'", name), e);
        }
    }

    @Override
    public final <T> void setProperty(String name, T value) {
        try {
            Assert.assertNotNull(name, "name");
            Assert.assertNotNull(value, "value");
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
            throw new FlowException(FlowErrorCode.PROPERTY, String.format("propertyName='%s'", name), e);
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
            LOGGER.warn("local attribute is replaced. flowId={}; elementId={}; previousAttribute={}",
                    getFlowId(), getElement().getId(), JSON.toJSONString(previous));
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
            LOGGER.warn("global attribute is replaced. flowId={}; previousAttribute={}",
                    getFlowId(), JSON.toJSONString(previous));
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
    public final void addFlowPromiseListener(PromiseListener<ExecutionInstance> listener) {
        promise.addListener(listener);
    }

    @Override
    public final ExecutionLinkPausePromise pauseExecutionLink() {
        if (executionLinkPausePromise != null) {
            return executionLinkPausePromise;
        }
        synchronized (this) {
            if (executionLinkPausePromise != null) {
                return executionLinkPausePromise;
            }
            executionLinkPausePromise = new ExecutionLinkPausePromise();
            return executionLinkPausePromise;
        }
    }

    public ExecutionLinkPausePromise getExecutionLinkPausePromise() {
        return executionLinkPausePromise;
    }
}
