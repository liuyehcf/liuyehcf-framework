package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ConditionBean;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class ConditionDelegateCollector extends AbstractDelegateCollector<ConditionBean, ConditionDelegate> {

    public ConditionDelegateCollector(List<FlowEngine> engines) {
        super(engines);
    }

    @Override
    Class<ConditionDelegate> getDelegateClass() {
        return ConditionDelegate.class;
    }

    @Override
    Class<ConditionBean> getAnnotationClass() {
        return ConditionBean.class;
    }

    @Override
    void register(FlowEngine engine, String executableName, Factory<ConditionDelegate> factory) {
        engine.registerConditionDelegateFactory(executableName, factory);
    }
}
