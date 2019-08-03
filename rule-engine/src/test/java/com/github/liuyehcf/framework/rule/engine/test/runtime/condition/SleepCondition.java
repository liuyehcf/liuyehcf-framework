package com.github.liuyehcf.framework.rule.engine.test.runtime.condition;

import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DelegateField;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/5/11
 */
public class SleepCondition extends BaseCondition {

    private DelegateField timeout;
    private DelegateField output;

    @Override
    boolean doCondition(ConditionContext context) throws Exception {
        TimeUnit.NANOSECONDS.sleep(timeout.getValue());
        return output.getValue();
    }
}
