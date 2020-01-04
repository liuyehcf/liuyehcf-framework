package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.action;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ActionBean;

/**
 * @author hechenfeng
 * @date 2019/10/19
 */
@ActionBean(names = "specialAction", engineNameRegex = "specialFlowEngine")
public class SpecialAction implements ActionDelegate {

    @Override
    public void onAction(ActionContext context) {
        System.out.println(getClass().getName());
    }
}
