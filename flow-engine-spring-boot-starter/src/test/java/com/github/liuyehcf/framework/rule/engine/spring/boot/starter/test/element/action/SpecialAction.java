package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test.element.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ActionBean;

/**
 * @author hechenfeng
 * @date 2019/10/19
 */
@ActionBean(names = "specialAction", engineNameRegex = "specialRuleEngine")
public class SpecialAction implements ActionDelegate {

    @Override
    public void onAction(ActionContext context) {
        System.out.println(getClass().getName());
    }
}
