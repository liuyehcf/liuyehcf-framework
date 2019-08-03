package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegatePromise;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateResult;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/8/1
 */
class ListenerOperation extends AbstractOperation<Void> {

    private final List<Listener> listeners;
    private final Listener listener;
    private final int offset;
    private final Object result;
    private final Throwable cause;

    ListenerOperation(OperationContext context, Promise<Void> optPromise, List<Listener> listeners, int offset, Object result, Throwable cause) {
        super(context, optPromise);
        Assert.assertNotNull(listeners);
        Assert.assertNotNull(optPromise);
        Assert.assertTrue(offset <= listeners.size());
        this.listeners = listeners;
        if (offset < listeners.size()) {
            this.listener = this.listeners.get(offset);
        } else {
            this.listener = null;
        }
        this.offset = offset;
        this.result = result;
        this.cause = cause;
    }

    @Override
    void operate() throws Throwable {
        if (listener == null) {
            optPromise.trySuccess(null);
            return;
        }

        DelegateInvocation delegateInvocation = context.getDelegateInvocation(listener, result, cause);
        DelegateResult delegateResult;

        try {
            delegateResult = delegateInvocation.proceed();
        } catch (Throwable e) {
            optPromise.tryFailure(e);
            throw e;
        }

        if (delegateResult.isAsync()) {
            DelegatePromise delegatePromise = delegateResult.getDelegatePromise();
            delegatePromise.addListener(promise -> processAsyncPromise(promise, this::continueNextListener));
        } else {
            continueNextListener();
        }
    }

    private void continueNextListener() {
        context.markElementFinished(listener);

        if (offset + 1 < listeners.size()) {
            context.executeAsync(new ListenerOperation(context, optPromise, listeners, offset + 1, result, cause));
        } else {
            optPromise.trySuccess(null);
        }
    }
}
