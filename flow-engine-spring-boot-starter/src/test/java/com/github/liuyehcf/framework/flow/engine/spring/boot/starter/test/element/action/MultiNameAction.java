package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.action;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ActionBean;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@ActionBean(names = {"multi.name.action", "multi/name/action", "*****"})
public class MultiNameAction implements ActionDelegate {
    @Override
    public void onAction(ActionContext context) {
        System.out.println(getClass().getSimpleName());
    }
}
