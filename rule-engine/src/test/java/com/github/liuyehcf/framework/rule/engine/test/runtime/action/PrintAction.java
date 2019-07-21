package com.github.liuyehcf.framework.rule.engine.test.runtime.action;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class PrintAction implements ActionDelegate {

    private DelegateField content;

    @Override
    public void onAction(ActionContext context) {
        String content = this.content.getValue();
        Assert.assertNotNull(content);
        if (STD_OUT_SWITCH) {
            System.out.println(String.format("execute printAction. content=%s", content));
        }
    }
}
