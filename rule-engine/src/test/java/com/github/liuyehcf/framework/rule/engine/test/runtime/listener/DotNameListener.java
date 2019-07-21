package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public class DotNameListener implements ListenerDelegate {

    @Override
    public void onListener(ListenerContext context) {
        if (STD_OUT_SWITCH) {
            System.out.println(getClass().getSimpleName());
        }
    }
}
