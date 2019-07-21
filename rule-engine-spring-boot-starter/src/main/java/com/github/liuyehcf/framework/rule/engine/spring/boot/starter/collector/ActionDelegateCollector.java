package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ActionBean;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class ActionDelegateCollector extends AbstractDelegateCollector<ActionBean, ActionDelegate> {

    @Override
    Class<ActionDelegate> getDelegateClass() {
        return ActionDelegate.class;
    }

    @Override
    Class<ActionBean> getAnnotationClass() {
        return ActionBean.class;
    }

    @Override
    void register(String executableName, Factory<ActionDelegate> factory) {
        RuleEngine.registerActionDelegateFactory(executableName, factory);
    }
}
