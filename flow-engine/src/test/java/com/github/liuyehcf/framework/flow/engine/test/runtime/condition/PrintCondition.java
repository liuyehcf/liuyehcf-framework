package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.PAUSE_SWITCH;
import static com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class PrintCondition extends BaseCondition {

    private DelegateField content;
    private DelegateField output;

    public void setOutput(DelegateField output) {
        this.output = output;
    }

    @Override
    public boolean onCondition(ConditionContext context) {
        if (PAUSE_SWITCH.get()) {
            context.pauseExecutionLink().trySuccess(null);
        }

        String content = this.content.getValue();
        Assert.assertNotNull(content);
        if (STD_OUT_SWITCH.get()) {
            System.out.println(String.format("execute printCondition. content=%s. output=%b", content, output.getValue()));
        }
        return output.getValue();
    }
}
