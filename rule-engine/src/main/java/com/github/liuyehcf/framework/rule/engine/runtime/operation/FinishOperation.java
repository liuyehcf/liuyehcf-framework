package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.PromiseListener;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.promise.GlobalEndListenerPromise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Trace;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class FinishOperation extends AbstractOperation<Void> implements PromiseListener<Void> {

    private AtomicInteger reachableLinkNum;

    public FinishOperation(OperationContext context) {
        super(context);
    }

    @Override
    public void operationComplete(Promise<Void> promise) {
        if (promise.isSuccess()) {
            int pre = reachableLinkNum.get();
            int next = pre - 1;

            while (!reachableLinkNum.compareAndSet(pre, next)) {
                pre = reachableLinkNum.get();
                next = pre - 1;
            }

            if (next == 0) {
                finishRulePromise();
            }
        } else {
            // this is not necessary to do so if GlobalEndListenerOperation's optPromise failed
            // because any unknown exception trigger rule terminate
            // just for the sake of insurance, execute one more time
            context.getPromise().tryFailure(promise.cause());
        }
    }

    @Override
    protected void execute() {
        Rule rule = context.getRule();

        List<Node> ends = rule.getEnds();

        boolean allFinished = true;

        for (Node end : ends) {
            // any link after reaching the endpoint will trigger this method
            // this is concurrency safe
            if (!context.isNodeUnreachable(end) && !isNodeFinished(end)) {
                allFinished = false;
            }
        }

        if (allFinished && isMarkLink()) {
            // if current execution link is MarkSuccessorUnreachableOperation link
            // then delegate to any ended link to do the finish operation
            ExecutionLink endedLink = findAnyEndedLink();
            if (endedLink != null) {
                // this clone operation may add link when rule is finished
                // echo with DefaultOperationContext.addLink
                context.executeAsync(new FinishOperation(context.cloneLinkedContext(endedLink)));
                return;
            }

            // means this rule cannot reach end
            // so let the rule finish
            ExecutionLink link = findAnyLink();
            if (link != null) {
                context.executeAsync(new FinishOperation(context.cloneUnLinkedContext(link)));
                return;
            }

            // other link is doing the finish operation, so do nothing in this link
            return;
        }

        if (allFinished && context.markRuleFinished()) {
            List<Listener> globalEndListeners = getGlobalListenerByEvent(ListenerEvent.end);
            removeUnreachableLinks();

            if (!context.getExecutionInstance().getLinks().isEmpty()) {
                reachableLinkNum = new AtomicInteger(context.getExecutionInstance().getLinks().size());
                for (ExecutionLink link : context.getExecutionInstance().getLinks()) {
                    Promise<Void> listenerPromise = new GlobalEndListenerPromise();
                    listenerPromise.addListener(this);
                    context.executeAsync(new GlobalEndListenerOperation(context.cloneLinkedContext(link), listenerPromise, globalEndListeners));
                }
            } else {
                finishRulePromise();
            }
        }
    }

    private void finishRulePromise() {
        context.getExecutionInstance().setEndNanos(System.nanoTime());

        Promise<ExecutionInstance> promise = context.getPromise();
        promise.trySuccess(context.getExecutionInstance());
    }

    private boolean isNodeFinished(Node end) {
        if (!context.isElementFinished(end)) {
            return false;
        }

        List<Listener> listeners = end.getListeners();

        // ignore sub rule's listeners, it will execution within sub rule's execution
        if (end instanceof Rule) {
            return true;
        }
        for (Listener listener : listeners) {
            if (!context.isElementFinished(listener)) {
                return false;
            }
        }

        return true;
    }

    private boolean isMarkLink() {
        return context.getExecutionLink() == null;
    }

    private ExecutionLink findAnyEndedLink() {
        Set<String> endIds = context.getRule().getEnds()
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
        Set<String> endIds = context.getRule().getEnds()
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

            Element element = context.getRule().getElement(trace.getId());

            // except exclusive gateway
            if (element instanceof Start
                    || element instanceof Action
                    || element instanceof Condition
                    || element instanceof JoinGateway
                    || element instanceof Rule) {
                lastNode = trace;
                break;
            }
        }

        return lastNode;
    }
}