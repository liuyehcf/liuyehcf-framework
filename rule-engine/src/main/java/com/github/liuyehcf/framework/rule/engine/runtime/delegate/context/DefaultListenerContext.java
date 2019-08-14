package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Attribute;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class DefaultListenerContext extends AbstractExecutableContext<Listener> implements ListenerContext {

    private final ListenerScope listenerScope;

    public DefaultListenerContext(Element element, String instanceId, String linkId, long executionId, Map<String, Object> env, Map<String, Attribute> globalAttributes, ListenerScope listenerScope) {
        super(element, instanceId, linkId, executionId, env, globalAttributes);
        this.listenerScope = listenerScope;
    }

    @Override
    public final Listener getElement() {
        return (Listener) element;
    }

    @Override
    public final ListenerScope getListenerScope() {
        return listenerScope;
    }
}
