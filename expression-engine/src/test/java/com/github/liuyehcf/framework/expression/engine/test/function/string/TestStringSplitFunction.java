package com.github.liuyehcf.framework.expression.engine.test.function.string;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public class TestStringSplitFunction extends TestBase {
    @Test
    public void case1() {
        String[] result = ExpressionEngine.execute("string.split(\"ababa\",\"b\")");

        assertEquals(3L, result.length);
    }

    @Test
    public void case2() {
        String[] result = ExpressionEngine.execute("string.split(\"ababa\",\"b\",1)");

        assertEquals(1L, result.length);
    }
}
