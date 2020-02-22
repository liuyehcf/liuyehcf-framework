package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/10/3
 */
public class TestExecuteMethod extends TestBase {
    @Test
    public void caseExecuteWithEnv() {
        final String expression = "(z & x | y) !=0 ? (a.b.c + d - f * g / h):(h % a.b.c <<d)";

        long l1, l2, l3, l4, l5, l6, l7, l8;
        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            l1 = random.nextLong();
            l2 = random.nextLong();
            l3 = random.nextLong();
            l4 = random.nextLong();
            l5 = random.nextLong();
            l6 = random.nextLong();
            l7 = random.nextLong();
            l8 = random.nextLong();

            Map<String, Object> env = EnvBuilder.builder().put("z", l1).put("x", l2).put("y", l3).put("g", l4).put("f", l5)
                    .put("a.b.c", l6).put("d", l7).put("h", l8).build();

            long expected = (l1 & l2 | l3) != 0 ? (l6 + l7 - l5 * l4 / l8) : (l8 % l6 << l7);
            Object result = ExpressionEngine.execute(expression, env);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.execute(env);
            assertEquals(expected, result);

            result = ExpressionEngine.execute(expressionCode, env);
            assertEquals(expected, result);
        }
    }

    @Test
    public void caseExec() {
        final String expression = "((h >= g) || z != a.b.c) ? (((x << 3) < y) ? (d & 3 | z) : (f - x * y)) : (g % f * z)";

        long l1, l2, l3, l4, l5, l6, l7, l8;
        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            l1 = random.nextLong();
            l2 = random.nextLong();
            l3 = random.nextLong();
            l4 = random.nextLong();
            l5 = random.nextLong();
            l6 = random.nextLong();
            l7 = random.nextLong();
            l8 = random.nextLong();

            long expected = ((l5 >= l4) || l8 != l1) ? (((l6 << 3) < l7) ? (l2 & 3 | l8) : (l3 - l6 * l7)) : (l4 % l3 * l8);
            Object result = ExpressionEngine.exec(expression, l5, l4, l8, l1, l6, l7, l2, l3);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.exec(l5, l4, l8, l1, l6, l7, l2, l3);
            assertEquals(expected, result);
        }
    }

    @Test
    @SuppressWarnings("all")
    public void caseExecuteWithoutEnv() {
        String expression = "(1 & 2 | 3) !=0 ? (4 + 5 - 6 * 7 / 8):(9 % 3 <<3)";
        long expected = (long) ((1 & 2 | 3) != 0 ? (4 + 5 - 6 * 7 / 8) : (9 % 3 << 3));

        Object result = ExpressionEngine.execute(expression);
        assertEquals(expected, result);

        ExpressionCode expressionCode = ExpressionEngine.compile(expression);
        result = ExpressionEngine.execute(expressionCode);
        assertEquals(expected, result);

        result = ExpressionEngine.exec(expressionCode);
        assertEquals(expected, result);

        result = expressionCode.execute();
        assertEquals(expected, result);

        result = expressionCode.exec();
        assertEquals(expected, result);
    }
}
