package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateResult;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.UnsafeDelegateInvocation;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
class ActionOperation extends AbstractOperation<Void> {

    private final Action action;

    ActionOperation(OperationContext context, Action action) {
        super(context);
        Assert.assertNotNull(action, "action");
        this.action = action;
    }

    @Override
    void operate() throws Throwable {
        context.setNode(action);

        invokeNodeBeforeListeners(action, this::continueAction);
    }

    private void continueAction() throws Throwable {
        UnsafeDelegateInvocation delegateInvocation = context.getDelegateInvocation(action, null, null);
        DelegateResult delegateResult;
        try {
            delegateResult = delegateInvocation.unsafeProceed();
        } catch (Throwable e) {
            invokeNodeFailureListeners(action, e, () -> throwCause(e));
            return;
        }

        if (delegateResult.isAsync()) {
            // if this delete invocation failed, then trigger failure listeners
            // after all the failure listeners executed, then rethrow the origin exception
            delegateResult.getDelegatePromise()
                    .addListener(promise -> processAsyncPromise(promise,
                            this::continueSuccessListener,
                            (e) -> invokeNodeFailureListeners(action, e, () -> throwCause(e))));
        } else {
            continueSuccessListener();
        }
    }

    private void continueSuccessListener() throws Throwable {
        invokeNodeSuccessListeners(action, null, this::continueForward);
    }

    private void continueForward() {
        context.markElementFinished(action);

        forward(LinkType.NORMAL, action.getSuccessors());
    }
}
