package org.liuyehcf.compile.engine.expression.test.function.string;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringReplaceAllFunction extends TestBase {
    @Test
    public void case1() {
        String result = ExpressionEngine.execute("string.replaceAll(\"abcdefcgg\",\".c.\",\"###\")");

        assertEquals("a###e###g", result);
    }

    @Test
    public void case2() {
        String result = ExpressionEngine.execute("string.replaceAll(\"abcdefcgg\",\".z.\",\"###\")");

        assertEquals("abcdefcgg", result);
    }
}
