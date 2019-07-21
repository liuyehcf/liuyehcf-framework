package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Attribute;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class DefaultListenerContext extends AbstractExecutableContext<Listener> implements ListenerContext {

    public DefaultListenerContext(Element element, String instanceId, String linkId, long executionId, Map<String, Object> env, Map<String, Attribute> globalAttributes) {
        super(element, instanceId, linkId, executionId, env, globalAttributes);
    }

    @Override
    public final Listener getElement() {
        return (Listener) element;
    }
}
