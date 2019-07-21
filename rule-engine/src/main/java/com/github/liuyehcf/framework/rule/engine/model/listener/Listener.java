package com.github.liuyehcf.framework.rule.engine.model.listener;

import com.github.liuyehcf.framework.rule.engine.model.Attachable;
import com.github.liuyehcf.framework.rule.engine.model.Executable;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
public interface Listener extends Attachable, Executable {

    String ARGUMENT_NAME_EVENT = "event";

    /**
     * listenerScope
     *
     * @return scope
     */
    ListenerScope getScope();

    /**
     * listener event
     *
     * @return event
     */
    ListenerEvent getEvent();
}
