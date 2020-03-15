package com.github.liuyehcf.framework.flow.engine.test.runtime.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2020/3/15
 */
public class DashNameListener extends BaseListener {

    @Override
    public void onBefore(ListenerContext context) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println(getClass().getSimpleName());
        }
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println(getClass().getSimpleName());
        }
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) {
        if (STD_OUT_SWITCH.get()) {
            System.out.println(getClass().getSimpleName());
        }
    }
}
