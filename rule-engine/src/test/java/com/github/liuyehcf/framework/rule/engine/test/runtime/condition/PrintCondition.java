package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;
import org.junit.Assert;

import static com.github.liuyehcf.framework.rule.engine.test.runtime.TestRuntimeBase.STD_OUT_SWITCH;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class PrintCondition implements ConditionDelegate {

    private DelegateField content;
    private DelegateField output;

    public void setOutput(DelegateField output) {
        this.output = output;
    }

    @Override
    public boolean onCondition(ConditionContext context) {
        String content = this.content.getValue();
        Assert.assertNotNull(content);
        if (STD_OUT_SWITCH) {
            System.out.println(String.format("execute printCondition. content=%s. output=%b", content, output.getValue()));
        }
        return output.getValue();
    }
}
