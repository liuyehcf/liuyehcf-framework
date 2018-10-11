package org.liuyehcf.compile.engine.expression.test.function.string;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringLengthFunction extends TestBase {
    @Test
    public void case1() {
        long result = ExpressionEngine.execute("string.length(\"abcdefg\")");

        assertEquals(7L, result);
    }

    @Test
    public void case2() {
        long result = ExpressionEngine.execute("string.length(\"\")");

        assertEquals(0L, result);
    }

    @Test
    public void case3() {
        long result = ExpressionEngine.execute("string.length(null)");

        assertEquals(0L, result);
    }
}