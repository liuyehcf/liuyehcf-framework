package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test.element.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@Component
@Setter
public class ComponentCondition implements ConditionDelegate {

    private DelegateField output;

    @Override
    public boolean onCondition(ConditionContext context) {
        System.out.println(getClass().getSimpleName());
        return output.getValue();
    }
}
