package com.github.liuyehcf.framework.expression.engine.test.function.date;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestDateTimestampFunction extends TestBase {
    @Test
    public void case1() {
        long result = ExpressionEngine.execute("date.timestamp()");

        assertEquals(System.currentTimeMillis() / 100, result / 100);
    }
}
