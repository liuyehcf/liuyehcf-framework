package com.github.liuyehcf.framework.flow.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.Element;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Attribute;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class DefaultConditionContext extends AbstractExecutableContext<Condition> implements ConditionContext {

    public DefaultConditionContext(Element element, Promise<ExecutionInstance> promise, String instanceId, String linkId, long executionId, Map<String, Object> env, Map<String, Attribute> globalAttributes) {
        super(element, promise, instanceId, linkId, executionId, env, globalAttributes);
    }

    @Override
    public final Condition getElement() {
        return (Condition) element;
    }
}
