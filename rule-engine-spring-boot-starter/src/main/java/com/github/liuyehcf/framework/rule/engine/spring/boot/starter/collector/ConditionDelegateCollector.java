package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ConditionBean;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class ConditionDelegateCollector extends AbstractDelegateCollector<ConditionBean, ConditionDelegate> {

    public ConditionDelegateCollector(List<RuleEngine> engines) {
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
    void register(RuleEngine engine, String executableName, Factory<ConditionDelegate> factory) {
        engine.registerConditionDelegateFactory(executableName, factory);
    }
}
