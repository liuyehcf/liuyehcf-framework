package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;

/**
 * @author hechenfeng
 * @date 2019/7/7
 */
public class ThrowLinkTerminateListener extends BaseListener {

    @Override
    void doBefore(ListenerContext context) {
        throw new LinkExecutionTerminateException();
    }

    @Override
    void doSuccess(ListenerContext context, Object result) {
        throw new LinkExecutionTerminateException();
    }

    @Override
    void doFailure(ListenerContext context, Throwable cause) {
        throw new LinkExecutionTerminateException();
    }
}
