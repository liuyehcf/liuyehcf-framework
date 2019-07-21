package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.rule.engine.model.DefaultRule;
import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.github.liuyehcf.framework.rule.engine.model.activity.DefaultAction;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/5/30
 */
public class TestIllegalRule extends TestRuntimeBase {

    @Test
    public void test1() {
        Promise<ExecutionInstance> promise = startRule("wrong format", null)
                .addListener((p) -> {
                    System.out.println("listener trigger");
                    p.cause().printStackTrace(System.out);
                });

        assertPromise(promise, false, true, false, true);
    }

    @Test
    public void test2() {
        Rule rule = new DefaultRule("test", "test", new Start("start"));

        rule.addElement(new DefaultAction("action", LinkType.NORMAL, "action", new String[]{}, new Object[]{}));

        Promise<ExecutionInstance> promise = startRule(rule, null)
                .addListener((p) -> {
                    System.out.println("listener trigger");
                    p.cause().printStackTrace(System.out);
                });

        assertPromise(promise, false, true, false, true);
    }
}
