package org.liuyehcf.compile.engine.expression.test.function.date;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

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
