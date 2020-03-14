package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.test.runtime.action.FieldConditionAction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2020/2/13
 */
public class TestFieldCondition extends TestRuntimeBase {

    @Test
    public void testWithEnv() {
        Map<String, Object> env = EnvBuilder.builder()
                .put("topic", "test")
                .build();

        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    fieldConditionAction(fieldContentWithAnnotationFalse=\"${topic}\"," +
                        "fieldContentWithAnnotationTrue=\"${topic}\"," +
                        "fieldContentWithoutAnnotation=\"${topic}\"," +
                        "methodContentWithAnnotationFalse=\"${topic}\"," +
                        "methodContentWithAnnotationTrue=\"${topic}\"," +
                        "methodContentWithoutAnnotation=\"${topic}\")\n" +
                        "}",
                env);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals("${topic}", FieldConditionAction.staticFieldContentWithAnnotationFalse);
        Assert.assertEquals("test", FieldConditionAction.staticFieldContentWithAnnotationTrue);
        Assert.assertEquals("test", FieldConditionAction.staticFieldContentWithoutAnnotation);

        Assert.assertEquals("${topic}", FieldConditionAction.staticMethodContentWithAnnotationFalse);
        Assert.assertEquals("test", FieldConditionAction.staticMethodContentWithAnnotationTrue);
        Assert.assertEquals("test", FieldConditionAction.staticMethodContentWithoutAnnotation);
    }

    @Test
    public void testWithoutEnv() {
        Promise<ExecutionInstance> promise = startFlow(
                "{\n" +
                        "    fieldConditionAction(fieldContentWithAnnotationFalse=\"${topic}\"," +
                        "fieldContentWithAnnotationTrue=\"${topic}\"," +
                        "fieldContentWithoutAnnotation=\"${topic}\"," +
                        "methodContentWithAnnotationFalse=\"${topic}\"," +
                        "methodContentWithAnnotationTrue=\"${topic}\"," +
                        "methodContentWithoutAnnotation=\"${topic}\")\n" +
                        "}",
                null);

        promise.sync();

        assertPromise(promise, false, true, true, false);

        Assert.assertEquals("${topic}", FieldConditionAction.staticFieldContentWithAnnotationFalse);
        Assert.assertNull(FieldConditionAction.staticFieldContentWithAnnotationTrue);
        Assert.assertNull(FieldConditionAction.staticFieldContentWithoutAnnotation);

        Assert.assertEquals("${topic}", FieldConditionAction.staticMethodContentWithAnnotationFalse);
        Assert.assertNull(FieldConditionAction.staticMethodContentWithAnnotationTrue);
        Assert.assertNull(FieldConditionAction.staticMethodContentWithoutAnnotation);
    }
}
