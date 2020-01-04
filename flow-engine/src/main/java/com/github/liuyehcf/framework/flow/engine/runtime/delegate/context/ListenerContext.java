package com.github.liuyehcf.framework.flow.engine.runtime.delegate.context;

import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerScope;

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

    /**
     * get listener scope of current listener
     *
     * @return listener scope
     * @see ListenerScope
     */
    ListenerScope getListenerScope();
}
