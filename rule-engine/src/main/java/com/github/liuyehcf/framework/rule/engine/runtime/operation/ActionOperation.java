package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class ActionOperation extends AbstractOperation<Void> {

    private final Action action;

    ActionOperation(OperationContext context, Action action) {
        super(context);
        Assert.assertNotNull(action);
        this.action = action;
    }

    @Override
    protected void execute() throws Throwable {
        context.setNode(action);

        invokeNodeBeforeListeners(action);

        DelegateInvocation delegateInvocation = context.getDelegateInvocation(action, null, null);
        try {
            delegateInvocation.proceed();
        } catch (Throwable e) {
            invokeNodeFailureListeners(action, e);
            throw e;
        }

        invokeNodeSuccessListeners(action, null);

        context.markElementFinished(action);

        forward(LinkType.NORMAL, action.getSuccessors());
    }
}
