package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import com.google.common.collect.Maps;
import org.junit.Assert;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class PrintListener extends BaseListener {

    public static final Map<String, AtomicInteger> GLOBAL_BEFORE_COUNTER = Maps.newConcurrentMap();
    public static final Map<String, AtomicInteger> GLOBAL_SUCCESS_COUNTER = Maps.newConcurrentMap();
    public static final Map<String, AtomicInteger> GLOBAL_FAILURE_COUNTER = Maps.newConcurrentMap();

    private DelegateField content;
    private DelegateField namespace;

    @Override
    void doBefore(ListenerContext context) {
        String content = this.content.getValue();
        Assert.assertNotNull(content);

        if (context.getListenerScope().equals(ListenerScope.GLOBAL)) {
            String namespace = this.namespace.getValue();
            if (namespace != null && GLOBAL_BEFORE_COUNTER.containsKey(namespace)) {
                GLOBAL_BEFORE_COUNTER.get(namespace).incrementAndGet();
            }
        }

        if (STD_OUT_SWITCH) {
            System.out.println(String.format("execute printListener. content=%s", content));
        }
    }

    @Override
    void doSuccess(ListenerContext context, Object result) {
        String content = this.content.getValue();
        Assert.assertNotNull(content);

        if (context.getListenerScope().equals(ListenerScope.GLOBAL)) {
            String namespace = this.namespace.getValue();
            if (namespace != null && GLOBAL_SUCCESS_COUNTER.containsKey(namespace)) {
                GLOBAL_SUCCESS_COUNTER.get(namespace).incrementAndGet();
            }
        }

        if (STD_OUT_SWITCH) {
            System.out.println(String.format("execute printListener. content=%s", content));
        }
    }

    @Override
    void doFailure(ListenerContext context, Throwable cause) {
        String content = this.content.getValue();
        Assert.assertNotNull(content);

        if (context.getListenerScope().equals(ListenerScope.GLOBAL)) {
            String namespace = this.namespace.getValue();
            if (namespace != null && GLOBAL_FAILURE_COUNTER.containsKey(namespace)) {
                GLOBAL_FAILURE_COUNTER.get(namespace).incrementAndGet();
            }
        }

        if (STD_OUT_SWITCH) {
            System.out.println(String.format("execute printListener. content=%s", content));
        }
    }
}
