package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.model.DefaultFlow;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.github.liuyehcf.framework.flow.engine.model.activity.DefaultAction;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/5/30
 */
public class TestIllegalFlow extends TestRuntimeBase {

    @Test
    public void test1() {
        Promise<ExecutionInstance> promise = startFlow("wrong format", null)
                .addListener((p) -> {
                    System.out.println("listener trigger");
                    p.cause().printStackTrace(System.out);
                });

        assertPromise(promise, false, true, false, true);
    }

    @Test
    public void test2() {
        Flow flow = new DefaultFlow("test", "test", new Start("start"));

        flow.addElement(new DefaultAction("action", "action", new String[]{}, new Object[]{}));

        Promise<ExecutionInstance> promise = startFlow(flow, null)
                .addListener((p) -> {
                    System.out.println("listener trigger");
                    p.cause().printStackTrace(System.out);
                });

        assertPromise(promise, false, true, false, true);
    }
}
