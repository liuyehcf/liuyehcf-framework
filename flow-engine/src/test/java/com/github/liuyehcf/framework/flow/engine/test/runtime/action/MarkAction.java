package com.github.liuyehcf.framework.flow.engine.test.runtime.action;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author hechenfeng
 * @date 2020/10/5
 */
public class MarkAction extends BaseAction {

    public static final Set<String> CACHE = Sets.newConcurrentHashSet();

    private DelegateField id;

    public void setId(DelegateField id) {
        this.id = id;
    }

    @Override
    public void onAction(ActionContext context) throws Exception {
        Assert.assertTrue(CACHE.add(id.getValue()), "id already exist");
    }
}
