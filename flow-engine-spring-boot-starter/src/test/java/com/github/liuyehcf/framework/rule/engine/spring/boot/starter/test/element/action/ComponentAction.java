package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test.element.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@Component
public class ComponentAction implements ActionDelegate {
    @Override
    public void onAction(ActionContext context) {
        System.out.println(getClass().getSimpleName());
    }
}
