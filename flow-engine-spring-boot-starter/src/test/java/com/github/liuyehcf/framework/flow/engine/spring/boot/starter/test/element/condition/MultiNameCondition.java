package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ConditionBean;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@ConditionBean(names = {"multi.name.condition", "multi/name/condition", "*****"})
public class MultiNameCondition implements ConditionDelegate {

    private DelegateField output;

    @Override
    public boolean onCondition(ConditionContext context) {
        System.out.println(getClass().getSimpleName());
        return output.getValue();
    }
}
