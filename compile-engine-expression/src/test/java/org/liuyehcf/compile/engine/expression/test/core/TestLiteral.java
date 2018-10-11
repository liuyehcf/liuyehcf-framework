package org.liuyehcf.compile.engine.expression.test.core;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestLiteral extends TestBase {
    @Test
    public void caseNull() {
        Object result = ExpressionEngine.execute("null");
        assertNull(result);
    }

    @Test
    public void caseBoolean() {
        Object result = ExpressionEngine.execute("true");
        assertEquals(true, result);

        result = ExpressionEngine.execute("false");
        assertEquals(false, result);
    }

    @Test
    public void caseLong() {
        Object result = ExpressionEngine.execute("100");
        assertEquals(100L, result);

        result = ExpressionEngine.execute("0");
        assertEquals(0L, result);

        result = ExpressionEngine.execute("0L");
        assertEquals(0L, result);

        result = ExpressionEngine.execute("-100l");
        assertEquals(-100L, result);

        expectedException(
                () -> ExpressionEngine.execute("110000000000000000000000000000000000000000l"),
                ExpressionException.class,
                "illegal integer literal='1110000000000000000000000000000000000000000l'");
    }

    @Test
    public void caseDouble() {
        Object result = ExpressionEngine.execute("100.");
        assertEquals(100., (double) result, 1e-10);

        result = ExpressionEngine.execute("0.");
        assertEquals(0., (double) result, 1e-10);

        result = ExpressionEngine.execute(".0");
        assertEquals(.0, (double) result, 1e-10);

        result = ExpressionEngine.execute("0f");
        assertEquals(0d, (double) result, 1e-10);

        result = ExpressionEngine.execute("0D");
        assertEquals(0d, (double) result, 1e-10);

        result = ExpressionEngine.execute("-1.3e10");
        assertEquals(-1.3e10, (double) result, 1e-10);

        result = ExpressionEngine.execute("1.3e-10");
        assertEquals(1.3e-10, (double) result, 1e-10);

        expectedException(
                () -> ExpressionEngine.execute("1e10000000"),
                ExpressionException.class,
                "illegal float literal='1e10000000'");
    }

    @Test
    public void caseString() {
        String result = ExpressionEngine.execute("'abc'");
        assertEquals("abc", result);

        result = ExpressionEngine.execute("\"abc\"");
        assertEquals("abc", result);

        result = ExpressionEngine.execute("'\n\t\0'");
        assertEquals("\n\t\0", result);

        result = ExpressionEngine.execute("\"\n\t\0\"");
        assertEquals("\n\t\0", result);

        result = ExpressionEngine.execute("\"'\\\"\"");
        assertEquals("'\"", result);

        result = ExpressionEngine.execute("'\"\\''");
        assertEquals("\"'", result);
    }
}
