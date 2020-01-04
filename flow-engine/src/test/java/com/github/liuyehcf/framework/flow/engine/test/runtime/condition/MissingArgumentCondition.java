package com.github.liuyehcf.framework.flow.engine.test.runtime.condition;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class MissingArgumentCondition extends BaseCondition {

    private DelegateField notMissing1;
    private DelegateField notMissing2;
    private DelegateField missing1;
    private DelegateField missing2;

    public void setNotMissing1(DelegateField notMissing1) {
        this.notMissing1 = notMissing1;
    }

    public void setMissing1(DelegateField missing1) {
        this.missing1 = missing1;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean onCondition(ConditionContext context) {
        Assert.assertNotNull(notMissing1);
        Assert.assertNotNull(notMissing2);
        Assert.assertNotNull(missing1);
        Assert.assertNotNull(missing2);

        Assert.assertNotNull(notMissing1.getValue());
        Assert.assertNotNull(notMissing2.getValue());
        Assert.assertNull(missing1.getValue());
        Assert.assertNull(missing2.getValue());

        return true;
    }
}
