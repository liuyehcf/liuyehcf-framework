package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ListenerBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@ListenerBean(names = "printListener")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrintListener implements ListenerDelegate {

    private DelegateField content;

    public void setContent(DelegateField content) {
        this.content = content;
    }

    @Override
    public void onBefore(ListenerContext context) throws Exception {
        System.out.println(String.format("printListener onBefore. content=%s", content.getValue()));
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) throws Exception {
        System.out.println(String.format("printListener onSuccess. content=%s", content.getValue()));
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) throws Exception {
        System.out.println(String.format("printListener onFailure. content=%s", content.getValue()));
    }
}
