package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/7/7
 */
public class ThrowLinkTerminateListener extends BaseListener {

    public static final Map<String, AtomicInteger> GLOBAL_BEFORE_COUNTER = Maps.newConcurrentMap();
    public static final Map<String, AtomicInteger> GLOBAL_SUCCESS_COUNTER = Maps.newConcurrentMap();
    public static final Map<String, AtomicInteger> GLOBAL_FAILURE_COUNTER = Maps.newConcurrentMap();

    private DelegateField namespace;

    @Override
    void doBefore(ListenerContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwLinkTerminateListener.");
        }

        if (context.getListenerScope().equals(ListenerScope.GLOBAL)) {
            String namespace = this.namespace.getValue();
            if (namespace != null && GLOBAL_BEFORE_COUNTER.containsKey(namespace)) {
                GLOBAL_BEFORE_COUNTER.get(namespace).incrementAndGet();
            }
        }

        throw new LinkExecutionTerminateException();
    }

    @Override
    void doSuccess(ListenerContext context, Object result) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwLinkTerminateListener.");
        }

        if (context.getListenerScope().equals(ListenerScope.GLOBAL)) {
            String namespace = this.namespace.getValue();
            if (namespace != null && GLOBAL_SUCCESS_COUNTER.containsKey(namespace)) {
                GLOBAL_SUCCESS_COUNTER.get(namespace).incrementAndGet();
            }
        }

        throw new LinkExecutionTerminateException();
    }

    @Override
    void doFailure(ListenerContext context, Throwable cause) {
        if (STD_OUT_SWITCH) {
            System.out.println("execute throwLinkTerminateListener.");
        }

        if (context.getListenerScope().equals(ListenerScope.GLOBAL)) {
            String namespace = this.namespace.getValue();
            if (namespace != null && GLOBAL_FAILURE_COUNTER.containsKey(namespace)) {
                GLOBAL_FAILURE_COUNTER.get(namespace).incrementAndGet();
            }
        }

        throw new LinkExecutionTerminateException();
    }
}
