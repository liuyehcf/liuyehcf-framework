package org.liuyehcf.compile.engine.expression.test.function.string;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringStartsWithFunction extends TestBase {
    @Test
    public void case1() {
        boolean result = ExpressionEngine.execute("string.startsWith(\"abcdefg\",\"ab\")");

        assertTrue(result);
    }

    @Test
    public void case2() {
        boolean result = ExpressionEngine.execute("string.startsWith(\"abcdefg\",\"f\")");

        assertFalse(result);
    }
}
