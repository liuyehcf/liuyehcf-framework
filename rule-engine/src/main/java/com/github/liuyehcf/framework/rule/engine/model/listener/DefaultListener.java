package com.github.liuyehcf.framework.rule.engine.model.listener;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.AbstractAttachable;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class DefaultListener extends AbstractAttachable implements Listener {

    private final String name;
    private final ListenerScope scope;
    private final ListenerEvent event;
    private final String[] argumentNames;
    private final Object[] argumentValues;

    public DefaultListener(String id, String attachedId, String name, ListenerScope scope, ListenerEvent event, String[] argumentNames, Object[] argumentValues) {
        super(id, attachedId);
        Assert.assertNotNull(name);
        Assert.assertNotNull(scope);
        Assert.assertNotNull(event);
        Assert.assertNotNull(argumentNames);
        Assert.assertNotNull(argumentValues);
        this.name = name;
        this.scope = scope;
        this.event = event;
        this.argumentNames = argumentNames;
        this.argumentValues = argumentValues;
    }

    @Override
    public final ElementType getType() {
        return ElementType.LISTENER;
    }

    @Override
    public final ListenerScope getScope() {
        return scope;
    }

    @Override
    public final ListenerEvent getEvent() {
        return event;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String[] getArgumentNames() {
        return argumentNames;
    }

    @Override
    public final Object[] getArgumentValues() {
        return argumentValues;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getType().getType(), getId(), getName());
    }
}
