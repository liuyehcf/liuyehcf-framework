package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2021/3/30
 */
public class TestDelegateField extends TestRuntimeBase {

    private static FlowEngine specifiedFlowEngine;

    @BeforeClass
    public static void init() {
        specifiedFlowEngine = new DefaultFlowEngine();

        specifiedFlowEngine.registerActionDelegateFactory("actionWithOnlyPublicSetMethod", () -> new ActionDelegate() {

            private String name;

            public void setName(DelegateField name) {
                this.name = name.getValue();
            }

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name);
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithOnlyPrivateSetMethod", () -> new ActionDelegate() {

            private String name;

            private void setName(DelegateField name) {
                this.name = name.getValue();
            }

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name);
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithOnlyPublicField", () -> new ActionDelegate() {

            public DelegateField name;

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name.getValue());
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithOnlyPrivateField", () -> new ActionDelegate() {

            private DelegateField name;

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name.getValue());
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithPublicSetMethodPublicField", () -> new ActionDelegate() {

            public DelegateField name;

            public void setName(DelegateField name) {
                this.name = name;
            }

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name.getValue());
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithPublicSetMethodPrivateField", () -> new ActionDelegate() {

            private DelegateField name;

            public void setName(DelegateField name) {
                this.name = name;
            }

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name.getValue());
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithPrivateSetMethodPublicField", () -> new ActionDelegate() {

            public DelegateField name;

            private void setName(DelegateField name) {
                this.name = name;
            }

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name.getValue());
            }
        });

        specifiedFlowEngine.registerActionDelegateFactory("actionWithPrivateSetMethodPrivateField", () -> new ActionDelegate() {

            private DelegateField name;

            private void setName(DelegateField name) {
                this.name = name;
            }

            @Override
            public void onAction(ActionContext context) {
                context.getEnv().put("name", name.getValue());
            }
        });
    }

    @Test
    public void testAnonymousInnerClassWithOnlyPublicSetMethod() {
        Flow flow = compile("{\n" +
                "    actionWithOnlyPublicSetMethod(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithOnlyPrivateSetMethod() {
        Flow flow = compile("{\n" +
                "    actionWithOnlyPrivateSetMethod(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertNull(executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithOnlyPublicField() {
        Flow flow = compile("{\n" +
                "    actionWithOnlyPublicField(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithOnlyPrivateField() {
        Flow flow = compile("{\n" +
                "    actionWithOnlyPrivateField(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithPublicSetMethodPublicField() {
        Flow flow = compile("{\n" +
                "    actionWithPublicSetMethodPublicField(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithPublicSetMethodPrivateField() {
        Flow flow = compile("{\n" +
                "    actionWithPublicSetMethodPrivateField(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithPrivateSetMethodPublicField() {
        Flow flow = compile("{\n" +
                "    actionWithPrivateSetMethodPublicField(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }

    @Test
    public void testAnonymousInnerClassWithPrivateSetMethodPrivateField() {
        Flow flow = compile("{\n" +
                "    actionWithPrivateSetMethodPrivateField(name=\"actionA\")\n" +
                "}");

        Promise<ExecutionInstance> promise = specifiedFlowEngine.startFlow(new ExecutionCondition(flow));

        ExecutionInstance executionInstance = promise.get();
        Assert.assertEquals("actionA", executionInstance.getEnv().get("name"));
    }
}
