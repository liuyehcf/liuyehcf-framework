package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.promise.ExecutionLinkPausePromise;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateResult;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.UnsafeDelegateInvocation;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;

/**
 * must guarantee optPromise will end in any cases
 *
 * @author hechenfeng
 * @date 2019/4/30
 */
class ConditionOperation extends AbstractOperation<Boolean> {

    private final Condition condition;
    private DelegateResult delegateResult;
    private volatile boolean conditionOutput;

    ConditionOperation(OperationContext context, Condition condition) {
        this(context, condition, null);
    }

    ConditionOperation(OperationContext context, Condition condition, Promise<Boolean> optPromise) {
        super(context, optPromise);
        Assert.assertNotNull(condition, "condition");
        this.condition = condition;
    }

    @Override
    void operate() throws Throwable {
        context.setNode(condition);

        invokeNodeBeforeListeners(condition, this::conditionCondition);
    }

    private void conditionCondition() throws Throwable {
        UnsafeDelegateInvocation delegateInvocation = context.getDelegateInvocation(condition, null, null);

        try {
            delegateResult = delegateInvocation.unsafeProceed();
        } catch (Throwable e) {
            invokeNodeFailureListeners(condition, e, () -> {
                if (optPromise != null) {
                    optPromise.tryFailure(e);
                }
                throwCause(e);
            });
            return;
        }

        if (delegateResult.isAsync()) {
            // if this delete invocation failed, then trigger failure listeners
            // after all the failure listeners executed, then rethrow the origin exception
            delegateResult.getDelegatePromise()
                    .addListener(promise -> processAsyncPromise(promise,
                            this::continueSuccessListener,
                            (e) -> invokeNodeFailureListeners(condition, e, () -> {
                                if (optPromise != null) {
                                    optPromise.tryFailure(e);
                                }
                                throwCause(e);
                            })
                    ));
        } else {
            continueSuccessListener();
        }
    }

    private void continueSuccessListener() throws Throwable {
        ExecutionLinkPausePromise executionLinkPausePromise = context.getAndResetExecutionLinkPausePromise();
        if (executionLinkPausePromise != null) {
            executionLinkPausePromise.addListener(promise -> processAsyncPromise(promise,
                    this::doContinueSuccessListener,
                    null));
        } else {
            doContinueSuccessListener();
        }
    }

    private void doContinueSuccessListener() throws Throwable {
        if (delegateResult.isAsync()) {
            conditionOutput = (boolean) delegateResult.getDelegatePromise().get();
        } else {
            conditionOutput = (boolean) delegateResult.getResult();
        }

        invokeNodeSuccessListeners(condition, conditionOutput, this::continueForward);
    }

    private void continueForward() {
        context.setConditionalOutput(condition, conditionOutput);
        LinkType linkType = conditionOutput ? LinkType.TRUE : LinkType.FALSE;
        LinkType unReachableLinkType = conditionOutput ? LinkType.FALSE : LinkType.TRUE;

        if (optPromise != null) {
            optPromise.trySuccess(conditionOutput);
        }

        context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), condition, unReachableLinkType));

        context.markElementFinished(condition);

        forward(condition, linkType);
    }
}
