package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/8/30
 */
public class ScopeListener extends BaseListener {

    public static final Set<String> IS_BEFORE_TRIGGERED = Sets.newConcurrentHashSet();
    public static final Set<String> IS_SUCCESS_TRIGGERED = Sets.newConcurrentHashSet();
    public static final Set<String> IS_FAILURE_TRIGGERED = Sets.newConcurrentHashSet();

    private DelegateField namespace;
    private DelegateField scope;

    @Override
    public void onBefore(ListenerContext context) {
        IS_BEFORE_TRIGGERED.add(namespace.getValue());
        Assert.assertNotNull(context.getListenerScope(), "listener scope is null");
        Assert.assertEquals(context.getListenerScope().name(), scope.getValue(), "listener scope mismatch");
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        IS_SUCCESS_TRIGGERED.add(namespace.getValue());
        Assert.assertNotNull(context.getListenerScope(), "listener scope is null");
        Assert.assertEquals(context.getListenerScope().name(), scope.getValue(), "listener scope mismatch");
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        IS_FAILURE_TRIGGERED.add(namespace.getValue());
        Assert.assertNotNull(context.getListenerScope(), "listener scope is null");
        Assert.assertEquals(context.getListenerScope().name(), scope.getValue(), "listener scope mismatch");
    }
}
