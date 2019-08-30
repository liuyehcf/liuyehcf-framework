package com.github.liuyehcf.framework.rule.engine.runtime.operation.context;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.model.*;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.DefaultActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.DefaultConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.DefaultListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.ReflectiveDelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.UnsafeDelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class DefaultOperationContext implements OperationContext {

    private final Executor executor = RuleEngine.getExecutor();
    private final Promise<ExecutionInstance> promise;
    private final ReentrantLock ruleLock;
    private final Map<String, ReentrantLock> elementLocks;
    private final Rule rule;
    private final boolean isMarkContext;
    private final ExecutionInstance executionInstance;
    private final ExecutionLink executionLink;
    private final AtomicBoolean ruleFinished;
    private final AtomicBoolean globalFailureListenerFinished;
    private final Set<String> finishedElementIds;
    private final Set<String> unreachableNodeIds;
    private final Map<String, Boolean> conditionOutputs;
    private final Map<String, Set<String>> joinGatewayReachedLinkIds;
    private final Set<String> aggregatedJoinGatewayIds;
    private final AtomicLong executionIdGenerator;
    private Node node;

    public DefaultOperationContext(Rule rule,
                                   String instanceId,
                                   Map<String, Object> env,
                                   AtomicLong executionIdGenerator,
                                   Promise<ExecutionInstance> promise) {
        this(promise,
                new ReentrantLock(),
                Maps.newConcurrentMap(),
                rule,
                false,
                new DefaultExecutionInstance(instanceId, rule, env),
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

    private DefaultOperationContext(Promise<ExecutionInstance> promise,
                                    ReentrantLock ruleLock,
                                    Map<String, ReentrantLock> elementLocks,
                                    Rule rule,
                                    boolean isMarkContext,
                                    ExecutionInstance executionInstance,
                                    ExecutionLink executionLink,
                                    AtomicBoolean ruleFinished,
                                    AtomicBoolean globalFailureListenerFinished,
                                    Set<String> finishedElementIds,
                                    Set<String> unreachableNodeIds,
                                    Map<String, Boolean> conditionOutputs,
                                    Map<String, Set<String>> joinGatewayReachedLinkIds,
                                    Set<String> aggregatedJoinGatewayIds,
                                    AtomicLong executionIdGenerator) {
        Assert.assertNotNull(promise);
        Assert.assertNotNull(ruleLock);
        Assert.assertNotNull(elementLocks);
        Assert.assertNotNull(rule);
        Assert.assertNotNull(executionInstance);
        Assert.assertNotNull(ruleFinished);
        Assert.assertNotNull(globalFailureListenerFinished);
        Assert.assertNotNull(finishedElementIds);
        Assert.assertNotNull(unreachableNodeIds);
        Assert.assertNotNull(conditionOutputs);
        Assert.assertNotNull(joinGatewayReachedLinkIds);
        Assert.assertNotNull(aggregatedJoinGatewayIds);
        Assert.assertNotNull(executionIdGenerator);

        this.promise = promise;
        this.ruleLock = ruleLock;
        this.elementLocks = elementLocks;
        this.rule = rule;
        this.isMarkContext = isMarkContext;
        this.executionInstance = executionInstance;
        this.executionLink = executionLink;
        this.ruleFinished = ruleFinished;
        this.globalFailureListenerFinished = globalFailureListenerFinished;
        this.finishedElementIds = finishedElementIds;
        this.unreachableNodeIds = unreachableNodeIds;
        this.conditionOutputs = conditionOutputs;
        this.joinGatewayReachedLinkIds = joinGatewayReachedLinkIds;
        this.aggregatedJoinGatewayIds = aggregatedJoinGatewayIds;
        this.executionIdGenerator = executionIdGenerator;
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
    public final Rule getRule() {
        return rule;
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
    public final boolean markRuleFinished() {
        while (!ruleFinished.get()) {
            // cas operation is thread safe though
            // this lock is echo with addLink method
            try {
                ruleLock.lock();
                if (ruleFinished.compareAndSet(false, true)) {
                    return true;
                }
            } finally {
                ruleLock.unlock();
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
    public final void setConditionOutput(Condition condition, boolean output) {
        conditionOutputs.put(condition.getId(), output);
    }

    @Override
    public final Boolean getConditionOutput(Condition condition) {
        return conditionOutputs.get(condition.getId());
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
                    RuleEngine.getActionDelegate(executable.getName()),
                    this,
                    new DefaultActionContext(action, instanceId, linkId, executionId, getLinkEnv(), executionInstance.getAttributes()),
                    RuleEngine.getDelegateInterceptorFactories());
        } else if (executable instanceof Condition) {
            Condition condition = (Condition) executable;
            return new ReflectiveDelegateInvocation(
                    null,
                    null,
                    executable,
                    RuleEngine.getConditionDelegate(executable.getName()),
                    this,
                    new DefaultConditionContext(condition, instanceId, linkId, executionId, getLinkEnv(), executionInstance.getAttributes()),
                    RuleEngine.getDelegateInterceptorFactories());
        } else if (executable instanceof Listener) {
            Listener listener = (Listener) executable;
            if (ListenerScope.global.equals((listener).getScope())) {
                return new ReflectiveDelegateInvocation(
                        result,
                        cause,
                        executable,
                        RuleEngine.getListenerDelegate(executable.getName()),
                        this,
                        new DefaultListenerContext(listener, instanceId, linkId, executionId, executionInstance.getEnv(), executionInstance.getAttributes(), listener.getScope()),
                        RuleEngine.getDelegateInterceptorFactories());
            } else if (ListenerScope.node.equals((listener).getScope())) {
                return new ReflectiveDelegateInvocation(
                        result,
                        cause,
                        executable,
                        RuleEngine.getListenerDelegate(executable.getName()),
                        this,
                        new DefaultListenerContext(listener, instanceId, linkId, executionId, getLinkEnv(), executionInstance.getAttributes(), listener.getScope()),
                        RuleEngine.getDelegateInterceptorFactories());
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
    public final void executeSync(AbstractOperation operation) {
        operation.run();
    }

    @Override
    public final void executeAsync(AbstractOperation operation) {
        try {
            executor.execute(operation);
        } catch (RejectedExecutionException e) {
            promise.tryFailure(new RuleException(RuleErrorCode.THREAD_POOL, "task rejected by thread pool", e));
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
    public final OperationContext cloneLinkedContext(ExecutionLink executionLink) {
        ExecutionLink actualLink;
        if (executionLink == null) {
            actualLink = new DefaultExecutionLink(CloneUtils.hessianClone(getLinkEnv()), Lists.newCopyOnWriteArrayList(this.executionLink.getTraces()));
        } else {
            actualLink = executionLink;
        }

        addLink(actualLink);

        return new DefaultOperationContext(promise,
                ruleLock,
                elementLocks,
                rule,
                false,
                executionInstance,
                actualLink,
                ruleFinished,
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
        return new DefaultOperationContext(
                promise,
                ruleLock,
                elementLocks,
                rule,
                false,
                executionInstance,
                null,
                ruleFinished,
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
        return new DefaultOperationContext(
                promise,
                ruleLock,
                elementLocks,
                rule,
                true,
                executionInstance,
                null,
                ruleFinished,
                globalFailureListenerFinished,
                finishedElementIds,
                unreachableNodeIds,
                conditionOutputs,
                joinGatewayReachedLinkIds,
                aggregatedJoinGatewayIds,
                executionIdGenerator);
    }

    private void addLink(ExecutionLink link) {
        if (ruleFinished.get()) {
            return;
        }

        // prevent adding link to instance when rule is finished
        // echo with FinishOperation context.executeAsync(new FinishOperation(context.cloneLinkedContext(endedLink)));
        try {
            ruleLock.lock();
            if (!ruleFinished.get()) {
                executionInstance.addLink(link);
            }
        } finally {
            ruleLock.unlock();
        }
    }
}
