package com.github.liuyehcf.framework.flow.engine.test.runtime.action;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public class DotNameAction extends BaseAction {

    @Override
    public void onAction(ActionContext context) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println(getClass().getSimpleName());
        }
    }
}
