package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;

/**
 * @author hechenfeng
 * @date 2019/7/7
 */
public class ThrowLinkTerminateListener implements ListenerDelegate {

    @Override
    public void onBefore(ListenerContext context) {
        throw new LinkExecutionTerminateException();
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        throw new LinkExecutionTerminateException();
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        throw new LinkExecutionTerminateException();
    }
}
