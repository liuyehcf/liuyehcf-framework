package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public class SlashNameListener implements ListenerDelegate {

    @Override
    public void onBefore(ListenerContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println(getClass().getSimpleName());
        }
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        if (STD_OUT_SWITCH) {
            System.out.println(getClass().getSimpleName());
        }
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        if (STD_OUT_SWITCH) {
            System.out.println(getClass().getSimpleName());
        }
    }
}