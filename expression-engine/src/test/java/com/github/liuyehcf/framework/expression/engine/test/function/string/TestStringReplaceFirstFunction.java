package com.github.liuyehcf.framework.expression.engine.test.function.string;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringReplaceFirstFunction extends TestBase {
    @Test
    public void case1() {
        String result = ExpressionEngine.execute("string.replaceFirst(\"abcdefcgg\",\".c.\",\"###\")");

        assertEquals("a###efcgg", result);
    }

    @Test
    public void case2() {
        String result = ExpressionEngine.execute("string.replaceFirst(\"abcdefcgg\",\".z.\",\"###\")");

        assertEquals("abcdefcgg", result);
    }
}

