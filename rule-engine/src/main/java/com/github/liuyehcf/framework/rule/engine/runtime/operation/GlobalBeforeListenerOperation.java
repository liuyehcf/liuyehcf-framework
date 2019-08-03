package com.github.liuyehcf.framework.rule.engine.runtime.operation;

import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultExecutionLink;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/7/18
 */
public class GlobalBeforeListenerOperation extends AbstractOperation<Void> {

    public GlobalBeforeListenerOperation(OperationContext context) {
        super(context);
    }

    @Override
    void operate() throws Throwable {
        context.setNode(context.getRule().getStart());

        invokeGlobalBeforeListeners(this::continueStartRule);
    }

    private void continueStartRule() {
        Map<String, Object> instanceEnv = context.getExecutionInstance().getEnv();
        ExecutionLink startLink = new DefaultExecutionLink(CloneUtils.hessianClone(instanceEnv));

        context.executeAsync(new StartOperation(context.cloneLinkedContext(startLink)));
    }
}

