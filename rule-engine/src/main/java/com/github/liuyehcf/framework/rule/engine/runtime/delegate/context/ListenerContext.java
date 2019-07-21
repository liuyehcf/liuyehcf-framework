package com.github.liuyehcf.framework.rule.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public interface ListenerContext extends ExecutableContext<Listener> {

    /**
     * get listener of this execution
     *
     * @return current listener
     */
    @Override
    Listener getElement();
}
