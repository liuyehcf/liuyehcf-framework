package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.runtime.exception.InstanceExecutionTerminateException;
import com.github.liuyehcf.framework.flow.engine.runtime.exception.LinkExecutionTerminateException;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.promise.ListenerPromise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.flow.engine.util.CloneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public abstract class AbstractOperation<T> implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperation.class);

    final OperationContext context;
    final Promise<T> optPromise;

    AbstractOperation(OperationContext context) {
        this(context, null);
    }

    AbstractOperation(OperationContext context, Promise<T> optPromise) {
        this(context, optPromise, false);
    }

    AbstractOperation(OperationContext context, Promise<T> optPromise, boolean skipBind) {
        Assert.assertNotNull(context, "context");
        this.context = context;
        this.optPromise = optPromise;
        if (!skipBind) {
            bindToFlowPromise();
        }
    }

    @Override
    public final void run() {
        execute(this::operate);
    }

    abstract void operate() throws Throwable;

    final void processAsyncPromise(Promise<?> promise, Runnable continuation, ExceptionHandler exceptionHandler) {
        execute(() -> {
            if (promise.isSuccess()) {
                if (continuation != null) {
                    continuation.run();
                }
            } else {
                Throwable cause = getCauseOf(promise);
                if (optPromise != null) {
                    optPromise.tryFailure(cause);
                }

                if (exceptionHandler != null) {
                    exceptionHandler.handleException(cause);
                } else {
                    throwCause(cause);
                }
            }
        });
    }

    final boolean isLinkTerminateException(Throwable e) {
        if (e instanceof LinkExecutionTerminateException) {
            return true;
        }

        if (e instanceof FlowException) {
            return Objects.equals(FlowErrorCode.LINK_TERMINATE, ((FlowException) e).getCode());
        }

        return false;
    }

    final void forward(LinkType linkType, List<Node> successors) {
        List<Node> actualSuccessors = getSuccessorsByLinkType(linkType, successors);

        if (actualSuccessors.isEmpty()) {
            context.executeAsync(new FinishOperation(this.context));
            return;
        }

        Node firstNextNode = actualSuccessors.get(0);

        // guarantee all parallel execution link created before execution
        // avoid remaining executing links(this link should be moved to unreachable links, but not) when FinishOperation finished
        List<AbstractOperation<?>> asyncOperations = Lists.newArrayList();

        asyncOperations.add(new ContinueOperation(this.context, firstNextNode));

        if (actualSuccessors.size() > 1) {
            for (int i = 1; i < actualSuccessors.size(); i++) {
                Node parallelNextNode = actualSuccessors.get(i);

                asyncOperations.add(new ContinueOperation(this.context.cloneLinkedContext(null), parallelNextNode));
            }
        }

        asyncOperations.forEach(context::executeAsync);
    }

    final void invokeNodeBeforeListeners(Node node, Runnable continuation) throws Throwable {
        invokeListeners(getNodeListenerByEvent(node, ListenerEvent.before),
                null,
                null,
                continuation,
                promise -> processAsyncPromise(promise, continuation, null));
    }

    final void invokeNodeSuccessListeners(Node node, Object result, Runnable continuation) throws Throwable {
        invokeListeners(getNodeListenerByEvent(node, ListenerEvent.success),
                result,
                null,
                continuation,
                promise -> processAsyncPromise(promise, continuation, null));
    }

    final void invokeNodeFailureListeners(Node node, Throwable cause, Runnable rethrowContinuation) throws Throwable {
        // guarantee rethrowContinuation will be executed in all cases
        // so let `(e) -> rethrowContinuation.run()` be the exceptionHandler
        invokeListeners(getNodeListenerByEvent(node, ListenerEvent.failure),
                null,
                cause,
                rethrowContinuation,
                promise -> processAsyncPromise(promise, rethrowContinuation, (e) -> {
                    if (e instanceof LinkExecutionTerminateException) {
                        // exception cannot degrade
                        // avoid the exception degradation when the exception of node is normal exception and the exception of listener is LinkExecutionTerminateException
                        throwCause(cause);
                    } else {
                        throwCause(e);
                    }
                }));
    }

    final void invokeGlobalBeforeListeners(Runnable continuation) throws Throwable {
        try {
            invokeListeners(getGlobalListenerByEvent(ListenerEvent.before),
                    null,
                    null,
                    continuation,
                    promise -> execute(() -> {
                        if (promise.isSuccess()) {
                            if (continuation != null) {
                                continuation.run();
                            }
                        } else {
                            if (promise.cause() == null) {
                                throw new FlowException(FlowErrorCode.PROMISE, "promise failed");
                            } else if (promise.cause() instanceof LinkExecutionTerminateException) {
                                ((DefaultExecutionInstance) context.getExecutionInstance()).setEndTime();
                                context.getPromise().trySuccess(context.getExecutionInstance());
                                throw new InstanceExecutionTerminateException();
                            } else {
                                throw promise.cause();
                            }
                        }
                    })
            );
        } catch (LinkExecutionTerminateException e) {
            ((DefaultExecutionInstance) context.getExecutionInstance()).setEndTime();
            context.getPromise().trySuccess(context.getExecutionInstance());

            throw new InstanceExecutionTerminateException(e);
        }
    }

    final void invokeGlobalSuccessListeners(Object result, Runnable continuation) throws Throwable {
        try {
            invokeListeners(getGlobalListenerByEvent(ListenerEvent.success),
                    result,
                    null,
                    continuation,
                    promise -> execute(() -> {
                        if (promise.isSuccess()) {
                            if (continuation != null) {
                                continuation.run();
                            }
                        } else {
                            if (promise.cause() == null) {
                                throw new FlowException(FlowErrorCode.PROMISE, "promise failed");
                            } else if (promise.cause() instanceof LinkExecutionTerminateException) {
                                if (continuation != null) {
                                    continuation.run();
                                }
                            } else {
                                throw promise.cause();
                            }
                        }
                    }));
        } catch (LinkExecutionTerminateException e) {
            //ignore
        }
    }

    private void invokeGlobalFailureListeners(Throwable cause, Runnable continuation) throws Throwable {
        invokeListeners(getGlobalListenerByEvent(ListenerEvent.failure),
                null,
                cause,
                continuation,
                promise -> execute(() -> {
                    // ignore all exception throw by global failure listener
                    if (promise.cause() != null) {
                        LOGGER.error("an exception was thrown by global failure listener", promise.cause());
                    }

                    if (continuation != null) {
                        continuation.run();
                    }
                }));
    }

    final Throwable getCauseOf(Promise<?> promise) {
        Throwable cause = promise.cause();
        return cause == null ?
                new FlowException(FlowErrorCode.PROMISE, "promise failed") :
                cause;
    }

    final void throwCause(Throwable cause) throws Throwable {
        throw cause;
    }

    final int getUnreachableNumOfJoinGateway(JoinGateway joinGateway) {
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

    final ExecutionLink mergeLinks(List<ExecutionLink> executionLinks) {
        // return the very link if contains only one link
        if (executionLinks.size() == 1) {
            return executionLinks.get(0);
        }

        // merge trace
        Set<String> ids = Sets.newHashSet();
        List<Trace> traces = Lists.newCopyOnWriteArrayList();

        for (ExecutionLink link : executionLinks) {
            for (Trace trace : link.getTraces()) {
                if (ids.add(trace.getId())) {
                    traces.add(trace);
                }
            }
        }

        traces.sort(Comparator.comparingLong(Trace::getExecutionId));

        // merge env
        ExecutionLink firstLink = executionLinks.get(0);
        ExecutionLink mergedLink = new DefaultExecutionLink(
                CloneUtils.cloneEnv(context.getEngine(), firstLink.getEnv()),
                Lists.newCopyOnWriteArrayList(firstLink.getTraces())
        );

        for (int i = 1; i < executionLinks.size(); i++) {
            ExecutionLink link = executionLinks.get(i);
            mergeEnv(mergedLink, link);
        }

        mergedLink.getTraces().clear();
        mergedLink.getTraces().addAll(traces);
        return mergedLink;
    }

    private void execute(Runnable runnable) {
        if (!isDone()) {
            try {
                runnable.run();
            } catch (Throwable e1) {
                try {
                    // escape mark link
                    if (!context.isMarkContext()
                            && isLinkTerminateException(e1)) {
                        context.markNodeUnreachable(context.getNode());
                        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), context.getNode(), LinkType.NORMAL));
                        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), context.getNode(), LinkType.TRUE));
                        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), context.getNode(), LinkType.FALSE));
                    } else {
                        terminate(e1);
                    }
                } catch (Throwable e2) {
                    // may be RejectedExecutionException if executor's reject policy is abort
                    terminate(e2);
                }
            }
        }
    }

    private void bindToFlowPromise() {
        if (optPromise != null) {
            context.getPromise().addListener(flowPromise -> optPromise.tryFailure(new FlowException(FlowErrorCode.FLOW_ALREADY_FINISHED, flowPromise.cause())));
        }
    }

    private List<Listener> getGlobalListenerByEvent(ListenerEvent event) {
        List<Listener> listeners = Lists.newArrayList();

        Flow flow = context.getFlow();

        for (Listener listener : flow.getListeners()) {
            if (listener.getScope().isGlobal()
                    && Objects.equals(event, listener.getEvent())) {
                listeners.add(listener);
            }
        }

        return listeners;
    }

    private List<Listener> getNodeListenerByEvent(Node node, ListenerEvent event) {
        List<Listener> listeners = Lists.newArrayList();

        for (Listener listener : node.getListeners()) {
            // sub flow's listeners including sub flow itself's listener and it's sub nodes listeners
            if (Objects.equals(event, listener.getEvent())
                    && Objects.equals(node.getId(), listener.getAttachedId())) {
                listeners.add(listener);
            }
        }

        return listeners;
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

    private void invokeListeners(List<Listener> listeners, Object result, Throwable cause, Runnable continuation, PromiseListener<Void> promiseListener) throws Throwable {
        if (CollectionUtils.isEmpty(listeners)) {
            if (continuation != null) {
                continuation.run();
            }
        } else {
            ListenerPromise listenerPromise = new ListenerPromise();

            listenerPromise.addListener(promiseListener);

            context.executeAsync(new ListenerOperation(context, listenerPromise, false, listeners, 0, result, cause));
        }
    }

    private void mergeEnv(ExecutionLink targetLink, ExecutionLink sourceLink) {
        Map<String, Object> targetEnv = targetLink.getEnv();

        List<Trace> targetTraces = targetLink.getTraces();
        List<Trace> sourceTraces = sourceLink.getTraces();

        int splitPos = -1;

        int minSize = Math.min(targetTraces.size(), sourceTraces.size());

        for (int i = 0; i < minSize; i++) {
            if (targetTraces.get(i).getExecutionId() != sourceTraces.get(i).getExecutionId()) {
                splitPos = i;
                break;
            }
        }

        Assert.assertFalse(splitPos == -1);

        for (int i = splitPos; i < sourceTraces.size(); i++) {
            Trace sourceTrace = sourceTraces.get(i);
            List<PropertyUpdate> propertyUpdates = sourceTrace.getPropertyUpdates();
            if (!CollectionUtils.isEmpty(propertyUpdates)) {
                for (PropertyUpdate propertyUpdate : propertyUpdates) {
                    try {
                        PropertyUtils.setProperty(targetEnv, propertyUpdate.getName(), propertyUpdate.getNewValue());
                    } catch (Exception e) {
                        throw new FlowException(FlowErrorCode.PROPERTY, e);
                    }
                }
            }
        }
    }

    private boolean isDone() {
        return context.getPromise().isDone();
    }

    private void terminate(Throwable e) {
        //all the optPromise will terminate either
        try {
            if (context.markGlobalFailureListenerFinished()) {
                invokeGlobalFailureListeners(e, () ->
                        context.getPromise().tryFailure(e)
                );
            } else {
                context.getPromise().tryFailure(e);
            }
        } catch (Throwable e2) {
            LOGGER.error("an exception was thrown by invokeGlobalFailureListeners", e2);
            context.getPromise().tryFailure(e);
        }
    }

    protected interface Runnable {

        void run() throws Throwable;
    }

    protected interface ExceptionHandler {

        void handleException(Throwable e) throws Throwable;
    }
}
