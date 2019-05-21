package com.github.liuyehcf.framework.expression.engine.test.function.string;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

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

