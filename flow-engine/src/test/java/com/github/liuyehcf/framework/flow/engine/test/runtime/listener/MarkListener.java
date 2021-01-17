package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author hechenfeng
 * @date 2020/10/2
 */
public class MarkListener extends BaseListener {

    public static final Set<String> BEFORE_CACHE = Sets.newConcurrentHashSet();
    public static final Set<String> SUCCESS_CACHE = Sets.newConcurrentHashSet();
    public static final Set<String> FAILURE_CACHE = Sets.newConcurrentHashSet();

    private DelegateField id;

    public void setId(DelegateField id) {
        this.id = id;
    }

    @Override
    public void onBefore(ListenerContext context) {
        Assert.assertTrue(BEFORE_CACHE.add(id.getValue()), "id already exist");
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        Assert.assertTrue(SUCCESS_CACHE.add(id.getValue()), "id already exist");
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        Assert.assertTrue(FAILURE_CACHE.add(id.getValue()), "id already exist");
    }
}
