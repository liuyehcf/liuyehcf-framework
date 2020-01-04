package com.github.liuyehcf.framework.flow.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.flow.engine.model.activity.Action;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public interface ActionContext extends ExecutableContext<Action> {

    /**
     * get action of this execution
     *
     * @return current action
     */
    @Override
    Action getElement();
}
