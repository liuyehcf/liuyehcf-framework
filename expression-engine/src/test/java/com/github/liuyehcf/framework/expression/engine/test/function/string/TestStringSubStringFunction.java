package com.github.liuyehcf.framework.expression.engine.test.function.string;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringSubStringFunction extends TestBase {
    @Test
    public void case1() {
        String result = ExpressionEngine.execute("string.substring(\"abcde\",1)");

        assertEquals("bcde", result);
    }

    @Test
    public void case2() {
        String result = ExpressionEngine.execute("string.substring(\"abcde\",1,2)");

        assertEquals("b", result);
    }
}
