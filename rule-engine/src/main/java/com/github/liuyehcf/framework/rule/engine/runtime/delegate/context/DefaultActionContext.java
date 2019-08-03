package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.Attribute;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public class DefaultActionContext extends AbstractExecutableContext<Action> implements ActionContext {

    public DefaultActionContext(Element element, String instanceId, String linkId, long executionId, Map<String, Object> env, Map<String, Attribute> globalAttributes, Promise<ExecutionInstance> rulePromise) {
        super(element, instanceId, linkId, executionId, env, globalAttributes, rulePromise);
    }

    @Override
    public final Action getElement() {
        return (Action) element;
    }
}
