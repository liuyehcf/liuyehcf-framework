package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.element.listener;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ListenerBean;

/**
 * @author hechenfeng
 * @date 2019/10/19
 */
@ListenerBean(names = "specialListener", engineNameRegex = "specialFlowEngine")
public class SpecialListener implements ListenerDelegate {

    @Override
    public void onBefore(ListenerContext context) {
        System.out.println(getClass().getName());
    }
}
