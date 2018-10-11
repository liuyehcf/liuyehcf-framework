package org.liuyehcf.compile.engine.expression.test.core;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;
import org.liuyehcf.compile.engine.expression.test.TestBase;
import org.liuyehcf.compile.engine.expression.utils.EnvBuilder;

import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class TestComplexExpression extends TestBase {
    @Test
    @SuppressWarnings("all")
    public void caseConstant() {
        boolean result = ExpressionEngine.execute("2>=3 &&  1 <= 2|| 5!=6");
        assertTrue(result);

        result = ExpressionEngine.execute("2>=3 &&  (1 <= 2|| 5!=6)");
        assertFalse(result);

        result = ExpressionEngine.execute("(1 <= 2 || 3 > 4) && !(((5 < -3 || 3 >= 3) || 7 != 8 && 8 == 9) || true)");
        assertEquals((1 <= 2 || 3 > 4) && !(((5 < -3 || 3 >= 3) || 7 != 8 && 8 == 9) || true), result);

        result = ExpressionEngine.execute("(1 < 3 || 5 > 6) && (((7 > 8) || (9 > -1 ? true : (8 > (100 - 2 > 200 ? 7 : 9)))) || 10 != 12) || (1 <= 2 + 2 || 3 - 6 > 4) && !(((5 < -3 || 3 >= 3) || 7 != ((1 < 2 ? 3 : 4) + (3 < 6 ? 4 : 0)) && 8 == 9) || true)");
        assertEquals((1 < 3 || 5 > 6) && (((7 > 8) || (9 > -1 ? true : (8 > (100 - 2 > 200 ? 7 : 9)))) || 10 != 12) || (1 <= 2 + 2 || 3 - 6 > 4) && !(((5 < -3 || 3 >= 3) || 7 != ((1 < 2 ? 3 : 4) + (3 < 6 ? 4 : 0)) && 8 == 9) || true), result);

        result = ExpressionEngine.execute("(1 <= 2 || 3 > 4) && !(((5<-3||3>=3) || 7!=8 && 8==9 && collection.include(['xixi',1L,1d],1d)) || true)");
        assertEquals((1 <= 2 || 3 > 4) && !(((5 < -3 || 3 >= 3) || 7 != 8 && 8 == 9) || true), result);
    }

    @Test
    public void caseProperty1() {
        final String expression = "(l1 + l2 * l5) / (l2 == 0 ? 1 : (l2 + 10000 == 0 ? 1 : (l2 + 10000000 == 0 ? 1 : l2 + 10000000))) < l3 - l4 || ((l5 >>> (l3 - l2) > 10 ? l3 : l2 + l3 * l1 - 3 * l4) / (l1 == 0 ? 1 : l1) > 9 ? (d1 < d2) : (d3 >= d4)) && l1 * d5 > d2 ? !(((l3 << l2 > (l4 | (l1 + l3 ^ l2)) || !(((l5 >>> (l3 - l2) > 10 ? l3 : l2 + l4 & l5 - l3 * l1) / (l1 == 0 ? 1 : l1) > 9 ? (d1 < d2) : (d3 >= d4)))))) : (l3 >> l5 <= l2) || (l1 & l3 | l4 ^ l5 - ~l2 + l3) > d5 ? (b1 && (b3 ? l2 < d5 + 3 : d3 / (d1 == 0 ? 1. : (d1 > 100 ? 50 : d1 + 10000)) <= 100) || b5) : (b2 || !b4)";

        long l1, l2, l3, l4, l5;
        double d1, d2, d3, d4, d5;
        boolean b1, b2, b3, b4, b5;
        Random random = new Random();
        for (int index = 0; index < 10000; index++) {
            l1 = random.nextLong();
            l2 = random.nextLong();
            l3 = random.nextLong();
            l4 = random.nextLong();
            l5 = random.nextLong();

            d1 = random.nextDouble();
            d2 = random.nextDouble();
            d3 = random.nextDouble();
            d4 = random.nextDouble();
            d5 = random.nextDouble();

            b1 = random.nextBoolean();
            b2 = random.nextBoolean();
            b3 = random.nextBoolean();
            b4 = random.nextBoolean();
            b5 = random.nextBoolean();

            Map<String, Object> env = EnvBuilder.builder().put("l1", l1).put("l2", l2).put("l3", l3).put("l4", l4).put("l5", l5)
                    .put("d1", d1).put("d2", d2).put("d3", d3).put("d4", d4).put("d5", d5)
                    .put("b1", b1).put("b2", b2).put("b3", b3).put("b4", b4).put("b5", b5)
                    .build();

            boolean expected = (l1 + l2 * l5) / (l2 == 0 ? 1 : (l2 + 10000 == 0 ? 1 : (l2 + 10000000 == 0 ? 1 : l2 + 10000000))) < l3 - l4 || ((l5 >>> (l3 - l2) > 10 ? l3 : l2 + l3 * l1 - 3 * l4) / (l1 == 0 ? 1 : l1) > 9 ? (d1 < d2) : (d3 >= d4)) && l1 * d5 > d2 ? !(((l3 << l2 > (l4 | (l1 + l3 ^ l2)) || !(((l5 >>> (l3 - l2) > 10 ? l3 : l2 + l4 & l5 - l3 * l1) / (l1 == 0 ? 1 : l1) > 9 ? (d1 < d2) : (d3 >= d4)))))) : (l3 >> l5 <= l2) || (l1 & l3 | l4 ^ l5 - ~l2 + l3) > d5 ? (b1 && (b3 ? l2 < d5 + 3 : d3 / (d1 == 0 ? 1. : (d1 > 100 ? 50 : d1 + 10000)) <= 100) || b5) : (b2 || !b4);
            Object result = ExpressionEngine.execute(expression, env);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.exec(l1, l2, l5, l3, l4, d1, d2, d3, d4, d5, b1, b3, b5, b2, b4);
            assertEquals(expected, result);
        }
    }

    @Test
    public void caseProperty2() {
        final String expression = "l3 - l5 << l2 >> l4 >>> l1 + l3 * l4 / (l2 == 0 ? 1 : l2)";

        long l1, l2, l3, l4, l5;
        Random random = new Random();
        for (int index = 0; index < 10000; index++) {
            l1 = random.nextLong();
            l2 = random.nextLong();
            l3 = random.nextLong();
            l4 = random.nextLong();
            l5 = random.nextLong();

            Map<String, Object> env = EnvBuilder.builder().put("l1", l1).put("l2", l2).put("l3", l3).put("l4", l4).put("l5", l5)
                    .build();

            long expected = l3 - l5 << l2 >> l4 >>> l1 + l3 * l4 / (l2 == 0 ? 1 : l2);
            Object result = ExpressionEngine.execute(expression, env);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.exec(l3, l5, l2, l4, l1);
            assertEquals(expected, result);
        }
    }

    @Test
    public void caseProperty3() {
        final String expression = "(l3 - l5 << l2) >> l4 * 3 >>> (l1 + l3) * (l4) / (l2 == 0 ? 1 : l2 * (l5 + l4) - l3)";

        long l1, l2, l3, l4, l5;
        Random random = new Random();
        for (int index = 0; index < 10000; index++) {
            l1 = random.nextLong();
            l2 = random.nextLong();
            l3 = random.nextLong();
            l4 = random.nextLong();
            l5 = random.nextLong();

            Map<String, Object> env = EnvBuilder.builder().put("l1", l1).put("l2", l2).put("l3", l3).put("l4", l4).put("l5", l5)
                    .build();

            long expected = (l3 - l5 << l2) >> l4 * 3 >>> (l1 + l3) * (l4) / (l2 == 0 ? 1 : l2 * (l5 + l4) - l3);
            Object result = ExpressionEngine.execute(expression, env);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.exec(l3, l5, l2, l4, l1);
            assertEquals(expected, result);
        }
    }

    @Test
    public void caseProperty4() {
        final String expression = "(~(l5 ^ l3 & l1 | l4) + l2 & (l3 ^ l1) - l1 & l5 ^ ~l2) | (l2 + l3) * ~l5 / (l1 == 0 ? 1 : l1)";

        long l1, l2, l3, l4, l5;
        Random random = new Random();
        for (int index = 0; index < 10000; index++) {
            l1 = random.nextLong();
            l2 = random.nextLong();
            l3 = random.nextLong();
            l4 = random.nextLong();
            l5 = random.nextLong();

            Map<String, Object> env = EnvBuilder.builder().put("l1", l1).put("l2", l2).put("l3", l3).put("l4", l4).put("l5", l5)
                    .build();

            long expected = (~(l5 ^ l3 & l1 | l4) + l2 & (l3 ^ l1) - l1 & l5 ^ ~l2) | (l2 + l3) * ~l5 / (l1 == 0 ? 1 : l1);
            Object result = ExpressionEngine.execute(expression, env);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.exec(l5, l3, l1, l4, l2);
            assertEquals(expected, result);
        }
    }

    @Test
    public void caseProperty5() {
        final String expression = "((b3 ? (d4 >= d2 + d3) : (d1 <= d2) && b4) || b1) ? b3 : (b2 || (d1 > d2 - 1e3 ? (d4 >= d2 + d3) : (d1 <= d2) && b4)) || d1 + d3 - d2 * d5 >= 100 && (b3 ? (d4 >= d2 + d3) : (d1 <= d2) && b4) || (b3 ? (d4 >= d2 % d3 - d5) : (d1 <= d2 * (d5 + d3)) && b4 || (-d1 > 0) ? (d1 < d5 ? b3 : (d2 != d3 + d1 && d5 * d1 > 0) && (d3 % d1 != d4 && (d2 != 0 ? (d4 >= d2 + d3) : (d1 <= d2) && b4) && d4 != d1 + d2 - d5 || b4) || ((d1 <= d2 - 1 && b4 ? d5 + d3 - d2 < 0 : d1 / (d3 == 0 ? 1 : d3) != d4) || b2 ? b1 : (((d2 + d1 - d1) * d4 < d5 ? (d4 >= d2 + d3) : ((d1 <= d2) && b4)) && (1 <= d2 * d2 % (d5 + 1))))) : b3)";

        boolean b1, b2, b3, b4, b5;
        double d1, d2, d3, d4, d5;
        Random random = new Random();
        for (int index = 0; index < 10000; index++) {
            d1 = random.nextDouble();
            d2 = random.nextDouble();
            d3 = random.nextDouble();
            d4 = random.nextDouble();
            d5 = random.nextDouble();

            b1 = random.nextBoolean();
            b2 = random.nextBoolean();
            b3 = random.nextBoolean();
            b4 = random.nextBoolean();
            b5 = random.nextBoolean();

            Map<String, Object> env = EnvBuilder.builder().put("b1", b1).put("b2", b2).put("b3", b3).put("b4", b4).put("b5", b5)
                    .put("d1", d1).put("d2", d2).put("d3", d3).put("d4", d4).put("d5", d5)
                    .build();

            boolean expected = ((b3 ? (d4 >= d2 + d3) : (d1 <= d2) && b4) || b1) ? b3 : (b2 || (d1 > d2 - 1e3 ? (d4 >= d2 + d3) : (d1 <= d2) && b4)) || d1 + d3 - d2 * d5 >= 100 && (b3 ? (d4 >= d2 + d3) : (d1 <= d2) && b4) || (b3 ? (d4 >= d2 % d3 - d5) : (d1 <= d2 * (d5 + d3)) && b4 || (-d1 > 0) ? (d1 < d5 ? b3 : (d2 != d3 + d1 && d5 * d1 > 0) && (d3 % d1 != d4 && (d2 != 0 ? (d4 >= d2 + d3) : (d1 <= d2) && b4) && d4 != d1 + d2 - d5 || b4) || ((d1 <= d2 - 1 && b4 ? d5 + d3 - d2 < 0 : d1 / (d3 == 0 ? 1 : d3) != d4) || b2 ? b1 : (((d2 + d1 - d1) * d4 < d5 ? (d4 >= d2 + d3) : ((d1 <= d2) && b4)) && (1 <= d2 * d2 % (d5 + 1))))) : b3);
            Object result = ExpressionEngine.execute(expression, env);
            assertEquals(expected, result);

            ExpressionCode expressionCode = ExpressionEngine.compile(expression);
            result = expressionCode.exec(b3, d4, d2, d3, d1, b4, b1, b2, d5);
            assertEquals(expected, result);
        }
    }
}
