package com.github.liuyehcf.framework.rule.engine.test.runtime.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public class DotNameAction implements ActionDelegate {
    @Override
    public void onAction(ActionContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println(getClass().getSimpleName());
        }
    }
}
