package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.runtime.exception.LinkExecutionTerminateException;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/7/7
 */
public class ThrowLinkTerminateListener extends BaseListener {

    public static final Map<String, AtomicInteger> BEFORE_COUNTER = Maps.newConcurrentMap();
    public static final Map<String, AtomicInteger> SUCCESS_COUNTER = Maps.newConcurrentMap();
    public static final Map<String, AtomicInteger> FAILURE_COUNTER = Maps.newConcurrentMap();

    private DelegateField namespace;

    @Override
    public void onBefore(ListenerContext context) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println("execute throwLinkTerminateListener.");
        }

        String namespace = this.namespace.getValue();
        if (namespace != null && BEFORE_COUNTER.containsKey(namespace)) {
            BEFORE_COUNTER.get(namespace).incrementAndGet();
        }

        throw new LinkExecutionTerminateException();
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println("execute throwLinkTerminateListener.");
        }

        String namespace = this.namespace.getValue();
        if (namespace != null && SUCCESS_COUNTER.containsKey(namespace)) {
            SUCCESS_COUNTER.get(namespace).incrementAndGet();
        }

        throw new LinkExecutionTerminateException();
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println("execute throwLinkTerminateListener.");
        }

        String namespace = this.namespace.getValue();
        if (namespace != null && FAILURE_COUNTER.containsKey(namespace)) {
            FAILURE_COUNTER.get(namespace).incrementAndGet();
        }

        throw new LinkExecutionTerminateException();
    }
}
