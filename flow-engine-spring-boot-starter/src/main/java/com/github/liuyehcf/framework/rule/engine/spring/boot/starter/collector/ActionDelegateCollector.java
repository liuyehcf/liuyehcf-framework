package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.rule.engine.FlowEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ActionBean;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class ActionDelegateCollector extends AbstractDelegateCollector<ActionBean, ActionDelegate> {

    public ActionDelegateCollector(List<FlowEngine> engines) {
        super(engines);
    }

    @Override
    Class<ActionDelegate> getDelegateClass() {
        return ActionDelegate.class;
    }

    @Override
    Class<ActionBean> getAnnotationClass() {
        return ActionBean.class;
    }

    @Override
    void register(FlowEngine engine, String executableName, Factory<ActionDelegate> factory) {
        engine.registerActionDelegateFactory(executableName, factory);
    }
}
