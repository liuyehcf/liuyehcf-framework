package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@Component
public class ComponentCondition implements ConditionDelegate {

    private DelegateField output;

    public void setOutput(DelegateField output) {
        this.output = output;
    }

    @Override
    public boolean onCondition(ConditionContext context) {
        System.out.println(getClass().getSimpleName());
        return output.getValue();
    }
}
