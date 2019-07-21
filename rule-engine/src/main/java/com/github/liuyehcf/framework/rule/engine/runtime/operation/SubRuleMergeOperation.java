package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.PromiseListener;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/7/3
 */
public class SubRuleMergeOperation extends AbstractOperation<Void> implements PromiseListener<ExecutionInstance> {

    private final Rule subRule;
    private final long startNanos;
    private Promise<ExecutionInstance> subRulePromise;

    SubRuleMergeOperation(OperationContext context, Rule subRule, long startNanos) {
        super(context);
        this.subRule = subRule;
        this.startNanos = startNanos;
    }

    @Override
    public void operationComplete(Promise<ExecutionInstance> promise) {
        subRulePromise = promise;
        this.run();
    }

    @Override
    protected void execute() throws Throwable {
        context.setNode(subRule);

        if (!subRulePromise.isSuccess()) {
            Trace subRuleErrorTrace = createErrorSubRuleTrace(startNanos, subRulePromise.cause());
            ExecutionLink subRuleErrorExecutionLink = mergeSubTraceAndCurLink(subRuleErrorTrace);
            context.getExecutionInstance().addUnreachableLink(subRuleErrorExecutionLink);
            if (subRulePromise.cause() != null) {
                throw subRulePromise.cause();
            } else {
                throw new RuleException(RuleErrorCode.SUB_RULE);
            }
        }

        ExecutionInstance subExecutionInstance = subRulePromise.get();

        // sub rule succeeded if there is any reachable link
        if (!subExecutionInstance.getLinks().isEmpty()) {

            // merge unreachable links if not empty
            if (!subExecutionInstance.getUnreachableLinks().isEmpty()) {
                ExecutionLink mergedSubUnreachableLink = mergeLinks(subExecutionInstance.getUnreachableLinks());
                context.getExecutionInstance().addUnreachableLink(appendSubRuleTracesToCurrentLink(subExecutionInstance, mergedSubUnreachableLink));
            }

            ExecutionLink mergedSubReachableLink = mergeLinks(subExecutionInstance.getLinks());
            context.getExecutionInstance().removeLink(context.getExecutionLink());

            context.executeAsync(new SubRuleOperation(context.cloneLinkedContext(appendSubRuleTracesToCurrentLink(subExecutionInstance, mergedSubReachableLink)), subRule, startNanos, LinkType.TRUE));
        } else {
            ExecutionLink mergedSubUnreachableLink = mergeLinks(subExecutionInstance.getUnreachableLinks());
            context.getExecutionInstance().removeLink(context.getExecutionLink());
            context.executeAsync(new SubRuleOperation(context.cloneLinkedContext(appendSubRuleTracesToCurrentLink(subExecutionInstance, mergedSubUnreachableLink)), subRule, startNanos, LinkType.FALSE));
        }
    }

    private Trace createErrorSubRuleTrace(long startNanos, Throwable cause) {
        return new DefaultTrace(
                context.getNextExecutionId(),
                subRule.getId(),
                subRule.getType(),
                subRule.getName(),
                null,
                null,
                null,
                null,
                cause,
                startNanos,
                System.nanoTime());
    }

    private ExecutionLink mergeSubTraceAndCurLink(Trace trace) {
        ExecutionLink executionLink = context.getExecutionLink();

        DefaultExecutionLink appendedExecutionLink = new DefaultExecutionLink(
                CloneUtils.hessianClone(executionLink.getEnv()),
                Lists.newCopyOnWriteArrayList(executionLink.getTraces()));
        appendedExecutionLink.addTrace(trace);

        return appendedExecutionLink;
    }

    private ExecutionLink appendSubRuleTracesToCurrentLink(ExecutionInstance subExecutionInstance, ExecutionLink mergedSubRuleLink) {

        // env is subject to subRule
        DefaultExecutionLink clonedAppendedLink = new DefaultExecutionLink(
                CloneUtils.hessianClone(mergedSubRuleLink.getEnv()),
                Lists.newCopyOnWriteArrayList(context.getExecutionLink().getTraces()));

        List<Trace> traces = Lists.newCopyOnWriteArrayList();
        traces.addAll(subExecutionInstance.getTraces());
        traces.addAll(mergedSubRuleLink.getTraces());
        traces.sort(Comparator.comparingLong(Trace::getExecutionId));

        traces.forEach(clonedAppendedLink::addTrace);

        return clonedAppendedLink;
    }
}
