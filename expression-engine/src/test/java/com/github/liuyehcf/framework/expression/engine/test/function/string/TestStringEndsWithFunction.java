package com.github.liuyehcf.framework.expression.engine.test.function.string;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringEndsWithFunction extends TestBase {
    @Test
    public void case1() {
        boolean result = ExpressionEngine.execute("string.endsWith(\"abcdefg\",\"fg\")");

        assertTrue(result);
    }

    @Test
    public void case2() {
        boolean result = ExpressionEngine.execute("string.endsWith(\"abcdefg\",\"f\")");

        assertFalse(result);
    }
}
