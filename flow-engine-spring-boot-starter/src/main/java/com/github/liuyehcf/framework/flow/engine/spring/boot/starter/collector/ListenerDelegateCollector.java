package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ListenerBean;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class ListenerDelegateCollector extends AbstractDelegateCollector<ListenerBean, ListenerDelegate> {

    public ListenerDelegateCollector(List<FlowEngine> engines) {
        super(engines);
    }

    @Override
    Class<ListenerDelegate> getDelegateClass() {
        return ListenerDelegate.class;
    }

    @Override
    Class<ListenerBean> getAnnotationClass() {
        return ListenerBean.class;
    }

    @Override
    void register(FlowEngine engine, String executableName, Factory<ListenerDelegate> factory) {
        engine.registerListenerDelegateFactory(executableName, factory);
    }
}

