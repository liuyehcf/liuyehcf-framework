package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ConditionBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@ConditionBean(names = "printCondition")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrintCondition implements ConditionDelegate {

    private DelegateField content;

    public void setContent(DelegateField content) {
        this.content = content;
    }

    @Override
    public boolean onCondition(ConditionContext conditionContext) throws Exception {
        System.out.println("printCondition. content=" + content.getValue());
        return true;
    }
}
