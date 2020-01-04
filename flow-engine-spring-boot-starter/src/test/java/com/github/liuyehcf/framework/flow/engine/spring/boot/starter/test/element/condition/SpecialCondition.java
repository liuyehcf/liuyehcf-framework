package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ConditionBean;

/**
 * @author hechenfeng
 * @date 2019/10/19
 */
@ConditionBean(names = "specialCondition", engineNameRegex = "specialFlowEngine")
public class SpecialCondition implements ConditionDelegate {

    @Override
    public boolean onCondition(ConditionContext context) {
        System.out.println(getClass().getName());
        return true;
    }
}
