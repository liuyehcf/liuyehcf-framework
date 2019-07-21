package com.github.liuyehcf.framework.rule.engine.test.runtime.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/5/11
 */
public class SleepAction implements ActionDelegate {

    private DelegateField timeout;

    @Override
    public void onAction(ActionContext context) throws Exception {
        TimeUnit.NANOSECONDS.sleep(timeout.getValue());
    }
}
