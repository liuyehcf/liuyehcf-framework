package org.liuyehcf.compile.engine.expression.test.function.string;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringIndexOfFunction extends TestBase {
    @Test
    public void case1() {
        long result = ExpressionEngine.execute("string.indexOf(\"abcdefg\",\"fg\")");

        assertEquals(5L, result);
    }

    @Test
    public void case2() {
        long result = ExpressionEngine.execute("string.indexOf(\"abcdefg\",\"z\")");

        assertEquals(-1L, result);
    }
}

