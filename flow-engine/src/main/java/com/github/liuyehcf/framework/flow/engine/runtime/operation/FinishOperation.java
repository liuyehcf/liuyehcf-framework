package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.Element;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.DefaultExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
class FinishOperation extends AbstractOperation<Void> {

    FinishOperation(OperationContext context) {
        super(context);
    }

    @Override
    void operate() throws Throwable {
        Flow flow = context.getFlow();

        List<Node> ends = flow.getEnds();

        boolean allFinished = true;

        for (Node end : ends) {
            // any link after reaching the endpoint will trigger this method
            // this is concurrency safe
            if (!context.isNodeUnreachable(end) && !isNodeFinished(end)) {
                allFinished = false;
            }
        }

        if (allFinished && context.isMarkContext()) {
            // if current execution link is MarkSuccessorUnreachableOperation link
            // then delegate to any ended link to do the finish operation
            ExecutionLink endedLink = findAnyEndedLink();
            if (endedLink != null) {
                // this clone operation may add link when flow is finished
                // echo with DefaultOperationContext.addLink
                context.executeAsync(new FinishOperation(context.cloneLinkedContext(endedLink)));
                return;
            }

            // means this flow cannot reach end
            // so let the flow finish
            ExecutionLink link = findAnyLink();
            if (link != null) {
                context.executeAsync(new FinishOperation(context.cloneUnLinkedContext()));
                return;
            }

            // other link is doing the finish operation, so do nothing in this link
            return;
        }

        if (allFinished && context.markFlowFinished()) {
            removeUnreachableLinks();

            boolean hasReachableLinks = !context.getExecutionInstance().getLinks().isEmpty();

            if (hasReachableLinks) {
                // if this flow is single link, then the env of instance and link are the same object
                // so there is no need to write back
                if (!context.isSingleLink()) {
                    ExecutionLink executionLink = mergeLinks(context.getExecutionInstance().getLinks());
                    context.getExecutionInstance().getEnv().clear();
                    context.getExecutionInstance().getEnv().putAll(executionLink.getEnv());
                }

                invokeGlobalSuccessListeners(true, this::finishFlowPromise);
            } else {
                invokeGlobalSuccessListeners(false, this::finishFlowPromise);
            }
        }
    }

    private void finishFlowPromise() {
        ((DefaultExecutionInstance) context.getExecutionInstance()).setEndTime();

        Promise<ExecutionInstance> promise = context.getPromise();
        promise.trySuccess(context.getExecutionInstance());
    }

    private boolean isNodeFinished(Node end) {
        if (!context.isElementFinished(end)) {
            return false;
        }

        List<Listener> listeners = end.getListeners();

        // ignore sub flow's listeners, it will execution within sub flow's execution
        if (end instanceof Flow) {
            return true;
        }
        for (Listener listener : listeners) {
            if (!context.isElementFinished(listener)) {
                return false;
            }
        }

        return true;
    }

    private ExecutionLink findAnyEndedLink() {
        Set<String> endIds = context.getFlow().getEnds()
                .stream()
                .map(Element::getId)
                .collect(Collectors.toSet());

        for (ExecutionLink link : context.getExecutionInstance().getLinks()) {
            Trace lastNode = lastNodeTraceOfLink(link);

            if (lastNode == null) {
                continue;
            }

            if (endIds.contains(lastNode.getId())) {
                return link;
            }
        }

        return null;
    }

    private ExecutionLink findAnyLink() {
        return context.getExecutionInstance().getLinks()
                .stream()
                .findAny()
                .orElse(null);
    }

    private void removeUnreachableLinks() {
        Set<String> endIds = context.getFlow().getEnds()
                .stream()
                .filter(e -> !context.isNodeUnreachable(e))
                .map(Element::getId)
                .collect(Collectors.toSet());

        // it's safe of CopyOnWriteList to remove element when traversal
        for (ExecutionLink link : context.getExecutionInstance().getLinks()) {
            Trace lastNode = lastNodeTraceOfLink(link);

            if (lastNode == null
                    || lastNode.getCause() != null
                    || !endIds.contains(lastNode.getId())) {
                context.getExecutionInstance().removeLink(link);
                context.getExecutionInstance().addUnreachableLink(link);
            }
        }
    }

    private Trace lastNodeTraceOfLink(ExecutionLink link) {
        List<Trace> traces = link.getTraces();
        Trace lastNode = null;
        for (int i = traces.size() - 1; i >= 0; i--) {
            Trace trace = traces.get(i);

            Element element = context.getFlow().getElement(trace.getId());

            // except exclusive gateway
            if (element instanceof Start
                    || element instanceof Action
                    || element instanceof Condition
                    || element instanceof JoinGateway
                    || element instanceof Flow) {
                lastNode = trace;
                break;
            }
        }

        return lastNode;
    }
}
