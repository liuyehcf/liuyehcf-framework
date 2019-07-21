package com.github.liuyehcf.framework.rule.engine.test.runtime.listener;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class PrintListener implements ListenerDelegate {

    private DelegateField content;

    @Override
    public void onListener(ListenerContext context) {
        String content = this.content.getValue();
        Assert.assertNotNull(content);
        if (STD_OUT_SWITCH) {
            System.out.println(String.format("execute printListener. content=%s", content));
        }
    }
}
