package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/13
 */
public class ThrowExceptionListener extends BaseListener {

    @Override
    void doBefore(ListenerContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwExceptionListener.");
        }
        throw new RuntimeException("throw exception listener");
    }

    @Override
    void doSuccess(ListenerContext context, Object result) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwExceptionListener.");
        }
        throw new RuntimeException("throw exception listener");
    }

    @Override
    void doFailure(ListenerContext context, Throwable cause) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwExceptionListener.");
        }
        throw new RuntimeException("throw exception listener");
    }
}
