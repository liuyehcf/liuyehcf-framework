package com.github.liuyehcf.framework.flow.engine.test.runtime.action;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.PAUSE_SWITCH;
import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class PrintAction extends BaseAction {

    private DelegateField content;

    @Override
    public void onAction(ActionContext context) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String content = this.content.getValue();
        Assert.assertNotNull(content);
        if (STD_OUT_SWITCH.get()) {
            System.out.println(String.format("execute printAction. content=%s", content));
        }
    }
}
