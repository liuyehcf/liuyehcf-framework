package org.liuyehcf.compile.engine.expression.test.core;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;
import org.liuyehcf.compile.engine.expression.utils.EnvBuilder;

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
