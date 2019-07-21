package com.github.liuyehcf.framework.rule.engine.runtime.operation.base;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.ContinueOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.FinishOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.MarkSuccessorUnreachableOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.PropertyUpdate;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public abstract class AbstractOperation<T> implements Runnable {

    protected final OperationContext context;
    protected final Promise<T> optPromise;

    protected AbstractOperation(OperationContext context) {
        this(context, null);
    }

    protected AbstractOperation(OperationContext context, Promise<T> optPromise) {
        Assert.assertNotNull(context);
        this.context = context;
        this.optPromise = optPromise;
        bindToRulePromise();
    }

    @Override
    public final void run() {
        if (!isDone()) {
            try {
                execute();
            } catch (Throwable e) {
                // escape mark link
                if (context.getNode() != null
                        && isLinkTerminateException(e)) {
                    context.markNodeUnreachable(context.getNode());
                    context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), context.getNode(), LinkType.NORMAL));
                    context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), context.getNode(), LinkType.TRUE));
                    context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), context.getNode(), LinkType.FALSE));
                } else {
                    terminate(e);
                }
            }
        }
    }

    protected abstract void execute() throws Throwable;

    protected final boolean isLinkTerminateException(Throwable e) {
        if (e instanceof LinkExecutionTerminateException) {
            return true;
        }

        if (e instanceof RuleException) {
            return Objects.equals(RuleErrorCode.LINK_TERMINATE, ((RuleException) e).getCode());
        }

        return false;
    }

    protected final void forward(LinkType linkType, List<Node> successors) {
        List<Node> actualSuccessors = getSuccessorsByLinkType(linkType, successors);

        if (actualSuccessors.isEmpty()) {
            context.executeAsync(new FinishOperation(this.context));
            return;
        }

        Node firstNextNode = actualSuccessors.get(0);

        // guarantee all parallel execution link created before execution
        // avoid remaining executing links(this link should be moved to unreachable links, but not) when FinishOperation finished
        List<AbstractOperation> asyncOperations = Lists.newArrayList();

        asyncOperations.add(new ContinueOperation(this.context, firstNextNode));

        if (actualSuccessors.size() > 1) {
            for (int i = 1; i < actualSuccessors.size(); i++) {
                Node parallelNextNode = actualSuccessors.get(i);

                asyncOperations.add(new ContinueOperation(this.context.cloneLinkedContext(null), parallelNextNode));
            }
        }

        asyncOperations.forEach(context::executeAsync);
    }

    protected final List<Listener> getGlobalListenerByEvent(ListenerEvent event) {
        List<Listener> listeners = Lists.newArrayList();

        Rule rule = context.getRule();

        for (Listener listener : rule.getListeners()) {
            if (Objects.equals(ListenerScope.GLOBAL, listener.getScope())
                    && Objects.equals(event, listener.getEvent())) {
                listeners.add(listener);
            }
        }

        return listeners;
    }

    protected final List<Listener> getNodeListenerByEvent(Node node, ListenerEvent event) {
        List<Listener> listeners = Lists.newArrayList();

        for (Listener listener : node.getListeners()) {
            // sub rule's listeners including sub rule itself's listener and it's sub nodes listeners
            if (Objects.equals(event, listener.getEvent())
                    && Objects.equals(node.getId(), listener.getAttachedId())) {
                listeners.add(listener);
            }
        }

        return listeners;
    }

    protected final void invokeListeners(List<Listener> listeners) throws Throwable {
        for (Listener listener : listeners) {
            invokeListener(listener);
        }
    }

    protected final int getUnreachableNumOfJoinGateway(JoinGateway joinGateway) {
        int numOfUnreachable = 0;
        for (Node predecessor : joinGateway.getPredecessors()) {
            if (predecessor instanceof Condition) {
                // in case of exclusive gateway, remaining conditions are unreachable
                if (context.isNodeUnreachable(predecessor)) {
                    numOfUnreachable++;
                } else {
                    Boolean output;

                    // in case of not execute yet or output is true
                    // if this condition is going to be unreachable, then JoinGatewayMergeOperation is bound to trigger again
                    // because MarkConditionSuccessorUnreachableOperation will do compensate operation(MarkConditionSuccessorUnreachableOperation#isSoftJoinGatewayUnreachable)
                    if ((output = context.getConditionOutput((Condition) predecessor)) != null
                            && !output) {
                        numOfUnreachable++;
                    }
                }
            } else if (context.isNodeUnreachable(predecessor)) {
                numOfUnreachable++;
            }
        }
        return numOfUnreachable;
    }

    protected final ExecutionLink mergeLink(List<ExecutionLink> refExecutionLinks) {
        // merge trace
        Set<String> ids = Sets.newHashSet();
        List<Trace> traces = Lists.newCopyOnWriteArrayList();

        for (ExecutionLink link : refExecutionLinks) {
            for (Trace trace : link.getTraces()) {
                if (ids.add(trace.getId())) {
                    traces.add(trace);
                }
            }
        }

        traces.sort(Comparator.comparingLong(Trace::getExecutionId));

        // merge env
        ExecutionLink refLink = refExecutionLinks.get(0);

        for (int i = 1; i < refExecutionLinks.size(); i++) {
            ExecutionLink link = refExecutionLinks.get(i);
            if (Objects.equals(refLink, link)) {
                continue;
            }

            redoOnCurEnvFromSplitPoint(refLink, link);
        }


        return new DefaultExecutionLink(refLink.getEnv(), traces);
    }

    protected final void redoOnCurEnvFromSplitPoint(ExecutionLink refLink, ExecutionLink targetLink) {
        Map<String, Object> refEnv = refLink.getEnv();

        List<Trace> refTraces = refLink.getTraces();
        List<Trace> targetTraces = targetLink.getTraces();

        int splitPos = -1;

        int minSize = Math.min(refTraces.size(), targetTraces.size());

        for (int i = 0; i < minSize; i++) {
            if (refTraces.get(i).getExecutionId() != targetTraces.get(i).getExecutionId()) {
                splitPos = i;
                break;
            }
        }

        Assert.assertFalse(splitPos == -1);

        for (int i = splitPos; i < targetTraces.size(); i++) {
            Trace targetTrace = targetTraces.get(i);
            List<PropertyUpdate> propertyUpdates = targetTrace.getPropertyUpdates();
            if (!CollectionUtils.isEmpty(propertyUpdates)) {
                for (PropertyUpdate propertyUpdate : propertyUpdates) {
                    try {
                        PropertyUtils.setProperty(refEnv, propertyUpdate.getName(), propertyUpdate.getNewValue());
                    } catch (Exception e) {
                        throw new RuleException(RuleErrorCode.PROPERTY, e);
                    }
                }
            }
        }
    }

    private void bindToRulePromise() {
        if (optPromise != null) {
            context.getPromise().addListener(rulePromise ->
                    optPromise.tryFailure(new RuleException(RuleErrorCode.PROMISE, rulePromise.cause()))
            );
        }
    }

    private List<Node> getSuccessorsByLinkType(LinkType linkType, List<Node> successors) {
        if (LinkType.NORMAL.equals(linkType)) {
            return successors;
        }

        List<Node> actualSuccessors = Lists.newArrayList();

        for (Node successor : successors) {
            if (Objects.equals(linkType, successor.getLinkType())) {
                actualSuccessors.add(successor);
            }
        }

        return actualSuccessors;
    }

    private void invokeListener(Listener listener) throws Throwable {
        DelegateInvocation delegateInvocation = context.getDelegateInvocation(listener);
        delegateInvocation.proceed();

        context.markElementFinished(listener);
    }

    private boolean isDone() {
        return context.getPromise().isDone();
    }

    private void terminate(Throwable e) {
        //all the optPromise will terminate either
        context.getPromise().tryFailure(e);
    }
}
