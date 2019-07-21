package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ListenerBean;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class ListenerDelegateCollector extends AbstractDelegateCollector<ListenerBean, ListenerDelegate> {

    @Override
    Class<ListenerDelegate> getDelegateClass() {
        return ListenerDelegate.class;
    }

    @Override
    Class<ListenerBean> getAnnotationClass() {
        return ListenerBean.class;
    }

    @Override
    void register(String executableName, Factory<ListenerDelegate> factory) {
        RuleEngine.registerListenerDelegateFactory(executableName, factory);
    }
}

