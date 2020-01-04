package com.github.liuyehcf.framework.flow.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.flow.engine.model.Element;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Attribute;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class DefaultActionContext extends AbstractExecutableContext<Action> implements ActionContext {

    public DefaultActionContext(Element element, String instanceId, String linkId, long executionId, Map<String, Object> env, Map<String, Attribute> globalAttributes) {
        super(element, instanceId, linkId, executionId, env, globalAttributes);
    }

    @Override
    public final Action getElement() {
        return (Action) element;
    }
}
