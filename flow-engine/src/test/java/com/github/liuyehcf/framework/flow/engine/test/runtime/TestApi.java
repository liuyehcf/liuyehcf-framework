package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class TestApi extends TestRuntimeBase {

    @Test
    public void testRegisterAndUnregister() {
        Assert.assertNull(engine.unregisterActionDelegateFactory(StringUtils.EMPTY));
        Assert.assertNull(engine.unregisterConditionDelegateFactory(StringUtils.EMPTY));
        Assert.assertNull(engine.unregisterListenerDelegateFactory(StringUtils.EMPTY));

        engine.registerActionDelegateFactory("toBeRemoved", () -> (context) -> {
        });
        Assert.assertNotNull(engine.unregisterActionDelegateFactory("toBeRemoved"));

        engine.registerConditionDelegateFactory("toBeRemoved", () -> (context) -> true);
        Assert.assertNotNull(engine.unregisterConditionDelegateFactory("toBeRemoved"));

        engine.registerListenerDelegateFactory("toBeRemoved", () -> new ListenerDelegate() {
            @Override
            public void onBefore(ListenerContext context) {
                // do nothing
            }

            @Override
            public void onSuccess(ListenerContext context, Object result) {
                // do nothing
            }

            @Override
            public void onFailure(ListenerContext context, Throwable cause) {
                // do nothing
            }
        });
        Assert.assertNotNull(engine.unregisterListenerDelegateFactory("toBeRemoved"));
    }
}
