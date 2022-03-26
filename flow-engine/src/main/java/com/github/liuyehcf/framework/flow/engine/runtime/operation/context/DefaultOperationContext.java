package com.github.liuyehcf.framework.flow.engine.runtime.operation.context;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.model.*;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.flow.engine.promise.ExecutionLinkPausePromise;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.DefaultActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.DefaultConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.DefaultListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.ReflectiveDelegateInvocation;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.UnsafeDelegateInvocation;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.AbstractOperation;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.flow.engine.util.CloneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class DefaultOperationContext implements OperationContext {

    private final FlowEngine engine;
    private final Promise<ExecutionInstance> promise;
    private final ReentrantLock flowLock;
    private final Map<String, ReentrantLock> elementLocks;
    private final Flow flow;
    private final boolean isSingleLink;
    private final boolean isMarkContext;
    private final ExecutionInstance executionInstance;
    private final ExecutionLink executionLink;
    private final AtomicBoolean flowFinished;
    private final AtomicBoolean globalFailureListenerFinished;
    private final Set<String> finishedElementIds;
    private final Set<String> unreachableNodeIds;
    private final Map<String, Boolean> conditionOutputs;
    private final Map<String, Set<String>> joinGatewayReachedLinkIds;
    private final Set<String> aggregatedJoinGatewayIds;
    private final AtomicLong executionIdGenerator;
    private Node node;
    private ExecutionLinkPausePromise executionLinkPausePromise;

    public DefaultOperationContext(FlowEngine engine,
                                   Flow flow,
                                   boolean isSingleLink,
                                   String instanceId,
                                   Map<String, Object> env,
                                   List<Attribute> attributes,
                                   AtomicLong executionIdGenerator,
                                   Promise<ExecutionInstance> promise) {
        this(engine,
                promise,
                new ReentrantLock(),
                Maps.newConcurrentMap(),
                flow,
                isSingleLink,
                false,
                new DefaultExecutionInstance(instanceId, flow, env, attributes),
                null,
                new AtomicBoolean(false),
                new AtomicBoolean(false),
                Sets.newConcurrentHashSet(),
                Sets.newConcurrentHashSet(),
                Maps.newConcurrentMap(),
                Maps.newConcurrentMap(),
                Sets.newConcurrentHashSet(),
                executionIdGenerator);
    }

    private DefaultOperationContext(FlowEngine engine,
                                    Promise<ExecutionInstance> promise,
                                    ReentrantLock flowLock,
                                    Map<String, ReentrantLock> elementLocks,
                                    Flow flow,
                                    boolean isSingleLink,
                                    boolean isMarkContext,
                                    ExecutionInstance executionInstance,
                                    ExecutionLink executionLink,
                                    AtomicBoolean flowFinished,
                                    AtomicBoolean globalFailureListenerFinished,
                                    Set<String> finishedElementIds,
                                    Set<String> unreachableNodeIds,
                                    Map<String, Boolean> conditionOutputs,
                                    Map<String, Set<String>> joinGatewayReachedLinkIds,
                                    Set<String> aggregatedJoinGatewayIds,
                                    AtomicLong executionIdGenerator) {
        Assert.assertNotNull(engine, "engine");
        Assert.assertNotNull(promise, "promise");
        Assert.assertNotNull(flowLock, "flowLock");
        Assert.assertNotNull(elementLocks, "elementLocks");
        Assert.assertNotNull(flow, "flow");
        Assert.assertNotNull(executionInstance, "executionInstance");
        Assert.assertNotNull(flowFinished, "flowFinished");
        Assert.assertNotNull(globalFailureListenerFinished, "globalFailureListenerFinished");
        Assert.assertNotNull(finishedElementIds, "finishedElementIds");
        Assert.assertNotNull(unreachableNodeIds, "unreachableNodeIds");
        Assert.assertNotNull(conditionOutputs, "conditionOutputs");
        Assert.assertNotNull(joinGatewayReachedLinkIds, "joinGatewayReachedLinkIds");
        Assert.assertNotNull(aggregatedJoinGatewayIds, "aggregatedJoinGatewayIds");
        Assert.assertNotNull(executionIdGenerator, "executionIdGenerator");

        this.engine = engine;
        this.promise = promise;
        this.flowLock = flowLock;
        this.elementLocks = elementLocks;
        this.flow = flow;
        this.isSingleLink = isSingleLink;
        this.isMarkContext = isMarkContext;
        this.executionInstance = executionInstance;
        this.executionLink = executionLink;
        this.flowFinished = flowFinished;
        this.globalFailureListenerFinished = globalFailureListenerFinished;
        this.finishedElementIds = finishedElementIds;
        this.unreachableNodeIds = unreachableNodeIds;
        this.conditionOutputs = conditionOutputs;
        this.joinGatewayReachedLinkIds = joinGatewayReachedLinkIds;
        this.aggregatedJoinGatewayIds = aggregatedJoinGatewayIds;
        this.executionIdGenerator = executionIdGenerator;
    }

    @Override
    public final FlowEngine getEngine() {
        return engine;
    }

    @Override
    public final Promise<ExecutionInstance> getPromise() {
        return promise;
    }

    @Override
    public final ReentrantLock getElementLock(Element element) {
        String id = element.getId();
        if (elementLocks.containsKey(id)) {
            return elementLocks.get(id);
        }
        elementLocks.putIfAbsent(id, new ReentrantLock());
        return elementLocks.get(id);
    }

    @Override
    public final Flow getFlow() {
        return flow;
    }

    @Override
    public final boolean isSingleLink() {
        return isSingleLink;
    }

    @Override
    public final boolean isMarkContext() {
        return isMarkContext;
    }

    @Override
    public final ExecutionInstance getExecutionInstance() {
        return executionInstance;
    }

    @Override
    public final ExecutionLink getExecutionLink() {
        return executionLink;
    }

    @Override
    public final Map<String, Object> getLinkEnv() {
        return executionLink.getEnv();
    }

    @Override
    public final Node getNode() {
        return node;
    }

    @Override
    public final void setNode(Node node) {
        this.node = node;
    }

    @Override
    public final AtomicLong getExecutionIdGenerator() {
        return executionIdGenerator;
    }

    @Override
    public final boolean markFlowFinished() {
        while (!flowFinished.get()) {
            // cas operation is thread safe though
            // this lock is echo with addLink method
            try {
                flowLock.lock();
                if (flowFinished.compareAndSet(false, true)) {
                    return true;
                }
            } finally {
                flowLock.unlock();
            }
        }
        return false;
    }

    @Override
    public final boolean markElementFinished(Element element) {
        return finishedElementIds.add(element.getId());
    }

    @Override
    public final boolean isElementFinished(Element element) {
        return finishedElementIds.contains(element.getId());
    }

    @Override
    public final void markNodeUnreachable(Node node) {
        unreachableNodeIds.add(node.getId());
    }

    @Override
    public final boolean isNodeUnreachable(Node node) {
        return unreachableNodeIds.contains(node.getId());
    }

    @Override
    public final boolean markGlobalFailureListenerFinished() {
        return globalFailureListenerFinished.compareAndSet(false, true);
    }

    @Override
    public final void setConditionalOutput(Node node, boolean output) {
        conditionOutputs.put(node.getId(), output);
    }

    @Override
    public final Boolean getConditionalOutput(Node node) {
        return conditionOutputs.get(node.getId());
    }

    @Override
    public final void linkReachesJoinGateway(JoinGateway joinGateway) {
        if (!joinGatewayReachedLinkIds.containsKey(joinGateway.getId())) {
            joinGatewayReachedLinkIds.putIfAbsent(joinGateway.getId(), Sets.newConcurrentHashSet());
        }

        joinGatewayReachedLinkIds.get(joinGateway.getId()).add(executionLink.getId());
    }

    @Override
    public final boolean isLinkReachedJoinGateway(JoinGateway joinGateway, ExecutionLink link) {
        if (!joinGatewayReachedLinkIds.containsKey(joinGateway.getId())) {
            joinGatewayReachedLinkIds.putIfAbsent(joinGateway.getId(), Sets.newConcurrentHashSet());
        }

        return joinGatewayReachedLinkIds.get(joinGateway.getId()).contains(link.getId());
    }

    @Override
    public final int getJoinGatewayReachesNum(JoinGateway joinGateway) {
        if (!joinGatewayReachedLinkIds.containsKey(joinGateway.getId())) {
            joinGatewayReachedLinkIds.putIfAbsent(joinGateway.getId(), Sets.newConcurrentHashSet());
        }

        return joinGatewayReachedLinkIds.get(joinGateway.getId()).size();
    }

    @Override
    public final void markJoinGatewayAggregated(JoinGateway joinGateway) {
        aggregatedJoinGatewayIds.add(joinGateway.getId());
    }

    @Override
    public final boolean isJoinGatewayAggregated(JoinGateway joinGateway) {
        return aggregatedJoinGatewayIds.contains(joinGateway.getId());
    }

    @Override
    public final UnsafeDelegateInvocation getDelegateInvocation(Executable executable, Object result, Throwable cause) {
        String instanceId = executionInstance.getId();
        String linkId = executionLink == null ? IDGenerator.generateUuid() : executionLink.getId();
        long executionId = getNextExecutionId();

        if (executable instanceof Action) {
            Action action = (Action) executable;
            return new ReflectiveDelegateInvocation(
                    null,
                    null,
                    executable,
                    engine.getActionDelegate(executable.getName()),
                    this,
                    new DefaultActionContext(action, promise, instanceId, linkId, executionId, getLinkEnv(), executionInstance.getAttributes()),
                    engine.getDelegateInterceptorFactories());
        } else if (executable instanceof Condition) {
            Condition condition = (Condition) executable;
            return new ReflectiveDelegateInvocation(
                    null,
                    null,
                    executable,
                    engine.getConditionDelegate(executable.getName()),
                    this,
                    new DefaultConditionContext(condition, promise, instanceId, linkId, executionId, getLinkEnv(), executionInstance.getAttributes()),
                    engine.getDelegateInterceptorFactories());
        } else if (executable instanceof Listener) {
            Listener listener = (Listener) executable;
            if (ListenerScope.global.equals((listener).getScope())) {
                return new ReflectiveDelegateInvocation(
                        result,
                        cause,
                        executable,
                        engine.getListenerDelegate(executable.getName()),
                        this,
                        new DefaultListenerContext(listener, promise, instanceId, linkId, executionId, executionInstance.getEnv(), executionInstance.getAttributes(), listener.getScope()),
                        engine.getDelegateInterceptorFactories());
            } else if (ListenerScope.node.equals((listener).getScope())) {
                return new ReflectiveDelegateInvocation(
                        result,
                        cause,
                        executable,
                        engine.getListenerDelegate(executable.getName()),
                        this,
                        new DefaultListenerContext(listener, promise, instanceId, linkId, executionId, getLinkEnv(), executionInstance.getAttributes(), listener.getScope()),
                        engine.getDelegateInterceptorFactories());
            } else {
                throw new UnsupportedOperationException("unexpected listener scope");
            }
        } else {
            throw new UnsupportedOperationException("unexpected executable");
        }
    }

    @Override
    public final long getNextExecutionId() {
        return executionIdGenerator.getAndIncrement();
    }

    @Override
    public final void executeSync(AbstractOperation<?> operation) {
        operation.run();
    }

    @Override
    public final void executeAsync(AbstractOperation<?> operation) {
        try {
            engine.getExecutor().execute(operation);
        } catch (RejectedExecutionException e) {
            promise.tryFailure(new FlowException(FlowErrorCode.THREAD_POOL, "task rejected by thread pool", e));
        }
    }

    @Override
    public final void addTraceToExecutionLink(Trace trace) {
        executionLink.addTrace(trace);
    }

    @Override
    public final void addTraceToExecutionInstance(Trace trace) {
        executionInstance.addTrace(trace);
    }

    @Override
    public void setExecutionLinkPausePromise(ExecutionLinkPausePromise executionLinkPausePromise) {
        this.executionLinkPausePromise = executionLinkPausePromise;
    }

    @Override
    public ExecutionLinkPausePromise getAndResetExecutionLinkPausePromise() {
        ExecutionLinkPausePromise executionLinkPausePromise = this.executionLinkPausePromise;
        this.executionLinkPausePromise = null;
        return executionLinkPausePromise;
    }

    @Override
    public final OperationContext cloneLinkedContext(ExecutionLink executionLink) {
        ExecutionLink actualLink;
        if (executionLink == null) {
            actualLink = new DefaultExecutionLink(CloneUtils.cloneEnv(engine, getLinkEnv()), Lists.newCopyOnWriteArrayList(this.executionLink.getTraces()));
        } else {
            actualLink = executionLink;
        }

        addLink(actualLink);

        return new DefaultOperationContext(engine,
                promise,
                flowLock,
                elementLocks,
                flow,
                isSingleLink,
                false,
                executionInstance,
                actualLink,
                flowFinished,
                globalFailureListenerFinished,
                finishedElementIds,
                unreachableNodeIds,
                conditionOutputs,
                joinGatewayReachedLinkIds,
                aggregatedJoinGatewayIds,
                executionIdGenerator);
    }

    @Override
    public final OperationContext cloneUnLinkedContext() {
        return new DefaultOperationContext(engine,
                promise,
                flowLock,
                elementLocks,
                flow,
                isSingleLink,
                false,
                executionInstance,
                null,
                flowFinished,
                globalFailureListenerFinished,
                finishedElementIds,
                unreachableNodeIds,
                conditionOutputs,
                joinGatewayReachedLinkIds,
                aggregatedJoinGatewayIds,
                executionIdGenerator);
    }

    @Override
    public final OperationContext cloneMarkContext() {
        return new DefaultOperationContext(engine,
                promise,
                flowLock,
                elementLocks,
                flow,
                isSingleLink,
                true,
                executionInstance,
                null,
                flowFinished,
                globalFailureListenerFinished,
                finishedElementIds,
                unreachableNodeIds,
                conditionOutputs,
                joinGatewayReachedLinkIds,
                aggregatedJoinGatewayIds,
                executionIdGenerator);
    }

    private void addLink(ExecutionLink link) {
        if (flowFinished.get()) {
            return;
        }

        // prevent adding link to instance when flow is finished
        // echo with FinishOperation context.executeAsync(new FinishOperation(context.cloneLinkedContext(endedLink)));
        try {
            flowLock.lock();
            if (!flowFinished.get()) {
                executionInstance.addLink(link);
            }
        } finally {
            flowLock.unlock();
        }
    }
}
