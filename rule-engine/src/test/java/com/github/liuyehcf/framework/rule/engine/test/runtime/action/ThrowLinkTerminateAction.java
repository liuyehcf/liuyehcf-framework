package com.github.liuyehcf.framework.rule.engine.test.runtime.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.exception.LinkExecutionTerminateException;

/**
 * @author hechenfeng
 * @date 2019/5/17
 */
public class ThrowLinkTerminateAction extends BaseAction {

    @Override
    public void onAction(ActionContext context) {
        throw new LinkExecutionTerminateException();
    }
}
