package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.flow.engine.util.CloneUtils;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/7/3
 */
class SubFlowMergeOperation extends AbstractOperation<Void> implements PromiseListener<ExecutionInstance> {

    private final Flow subFlow;
    private final long startTimestamp;
    private final long startNanos;
    private Promise<ExecutionInstance> subFlowPromise;

    SubFlowMergeOperation(OperationContext context, Flow subFlow, long startTimestamp, long startNanos) {
        super(context);
        this.subFlow = subFlow;
        this.startTimestamp = startTimestamp;
        this.startNanos = startNanos;
    }

    @Override
    public void operationComplete(Promise<ExecutionInstance> promise) {
        subFlowPromise = promise;
        this.run();
    }

    @Override
    void operate() throws Throwable {
        context.setNode(subFlow);

        if (!subFlowPromise.isSuccess()) {
            Trace subFlowErrorTrace = createErrorSubFlowTrace(subFlowPromise.cause());
            ExecutionLink subFlowErrorExecutionLink = mergeSubTraceAndCurLink(subFlowErrorTrace);
            context.getExecutionInstance().addUnreachableLink(subFlowErrorExecutionLink);
            if (subFlowPromise.cause() != null) {
                throw subFlowPromise.cause();
            } else {
                throw new FlowException(FlowErrorCode.SUB_FLOW);
            }
        }

        ExecutionInstance subExecutionInstance = subFlowPromise.get();

        // sub flow succeeded if there is any reachable link
        if (!subExecutionInstance.getLinks().isEmpty()) {

            // merge unreachable links if not empty
            if (!subExecutionInstance.getUnreachableLinks().isEmpty()) {
                ExecutionLink mergedSubUnreachableLink = mergeLinks(subExecutionInstance.getUnreachableLinks());
                context.getExecutionInstance().addUnreachableLink(appendSubFlowTracesToCurrentLink(subExecutionInstance, mergedSubUnreachableLink));
            }

            ExecutionLink mergedSubReachableLink = mergeLinks(subExecutionInstance.getLinks());
            context.getExecutionInstance().removeLink(context.getExecutionLink());
            context.executeAsync(new SubFlowOperation(
                    context.cloneLinkedContext(appendSubFlowTracesToCurrentLink(subExecutionInstance, mergedSubReachableLink)),
                    subFlow,
                    startTimestamp,
                    startNanos,
                    LinkType.TRUE)
            );
        } else {
            ExecutionLink mergedSubUnreachableLink = mergeLinks(subExecutionInstance.getUnreachableLinks());
            context.getExecutionInstance().removeLink(context.getExecutionLink());
            context.executeAsync(new SubFlowOperation(
                    context.cloneLinkedContext(appendSubFlowTracesToCurrentLink(subExecutionInstance,
                            mergedSubUnreachableLink)),
                    subFlow,
                    startTimestamp,
                    startNanos,
                    LinkType.FALSE));
        }
    }

    private Trace createErrorSubFlowTrace(Throwable cause) {
        return new DefaultTrace(
                context.getNextExecutionId(),
                subFlow.getId(),
                subFlow.getType(),
                subFlow.getName(),
                null,
                null,
                null,
                null,
                cause,
                startTimestamp,
                System.currentTimeMillis(),
                System.nanoTime() - startNanos);
    }

    private ExecutionLink mergeSubTraceAndCurLink(Trace trace) {
        ExecutionLink executionLink = context.getExecutionLink();

        DefaultExecutionLink appendedExecutionLink = new DefaultExecutionLink(
                CloneUtils.cloneEnv(context.getEngine(), executionLink.getEnv()),
                Lists.newCopyOnWriteArrayList(executionLink.getTraces()));
        appendedExecutionLink.addTrace(trace);

        return appendedExecutionLink;
    }

    private ExecutionLink appendSubFlowTracesToCurrentLink(ExecutionInstance subExecutionInstance, ExecutionLink mergedSubFlowLink) {

        // env is subject to subFlow
        DefaultExecutionLink clonedAppendedLink = new DefaultExecutionLink(
                mergedSubFlowLink.getEnv(),
                Lists.newCopyOnWriteArrayList(context.getExecutionLink().getTraces()));

        List<Trace> traces = Lists.newCopyOnWriteArrayList();
        traces.addAll(subExecutionInstance.getTraces());
        traces.addAll(mergedSubFlowLink.getTraces());
        traces.sort(Comparator.comparingLong(Trace::getExecutionId));

        traces.forEach(clonedAppendedLink::addTrace);

        return clonedAppendedLink;
    }
}
