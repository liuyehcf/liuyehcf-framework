package com.github.liuyehcf.framework.rule.engine.runtime.delegate;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface ListenerDelegate extends Delegate {

    /**
     * method invoke when listener is reached
     *
     * @param context context of execution
     * @throws Exception exceptions
     */
    void onListener(ListenerContext context) throws Exception;
}
