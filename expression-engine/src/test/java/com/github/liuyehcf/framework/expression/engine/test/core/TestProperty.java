package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestProperty extends TestBase {
    @Test
    public void caseBooleanNotTrueProperty() {
        Map<String, Object> env = EnvBuilder.builder().put("a", true).build();
        boolean result = ExpressionEngine.execute("!a", env);

        assertFalse(result);
    }

    @Test
    public void caseBooleanNotFalseProperty() {
        Map<String, Object> env = EnvBuilder.builder().put("a", false).build();
        boolean result = ExpressionEngine.execute("!a", env);

        assertTrue(result);
    }
}
