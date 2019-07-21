package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.base.AbstractOperation;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/7/7
 */
class GlobalEndListenerOperation extends AbstractOperation<Void> {

    private List<Listener> globalEndListeners;

    GlobalEndListenerOperation(OperationContext context, Promise<Void> optPromise, List<Listener> globalEndListeners) {
        super(context, optPromise);
        this.globalEndListeners = globalEndListeners;
    }

    @Override
    protected void execute() throws Throwable {
        try {
            invokeListeners(globalEndListeners);
            optPromise.trySuccess(null);
        } catch (LinkExecutionTerminateException e) {
            optPromise.trySuccess(null);
            // must swallow this kind of exception
            // because AbstractOperation.run() will unReach all successors of current node and current node itself
            // but since listener is triggered, current node is already reached
        }
    }
}
