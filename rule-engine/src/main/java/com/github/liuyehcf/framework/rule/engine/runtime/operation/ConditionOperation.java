package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class ConditionOperation extends AbstractOperation<Boolean> {

    private final Condition condition;

    ConditionOperation(OperationContext context, Condition condition) {
        this(context, condition, null);
    }

    ConditionOperation(OperationContext context, Condition condition, Promise<Boolean> optPromise) {
        super(context, optPromise);
        Assert.assertNotNull(condition);
        this.condition = condition;
    }

    @Override
    protected void execute() throws Throwable {
        context.setNode(condition);

        Throwable cause = null;
        boolean hasSuccess = false;
        try {
            invokeListeners(getNodeListenerByEvent(condition, ListenerEvent.start));

            DelegateInvocation delegateInvocation = context.getDelegateInvocation(condition);
            boolean conditionOutput = (boolean) delegateInvocation.proceed();
            context.setConditionOutput(condition, conditionOutput);
            LinkType linkType = conditionOutput ? LinkType.TRUE : LinkType.FALSE;
            LinkType unReachableLinkType = conditionOutput ? LinkType.FALSE : LinkType.TRUE;

            invokeListeners(getNodeListenerByEvent(condition, ListenerEvent.end));

            if (optPromise != null && optPromise.trySuccess(conditionOutput)) {
                hasSuccess = true;
            }

            context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), condition, unReachableLinkType));

            context.markElementFinished(condition);

            forward(linkType, condition.getSuccessors());
        } catch (Throwable e) {
            cause = e;
            throw e;
        } finally {
            if (optPromise != null && !hasSuccess) {
                optPromise.tryFailure(cause);
            }
        }
    }
}
