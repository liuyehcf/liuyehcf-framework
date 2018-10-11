package org.liuyehcf.compile.engine.expression.test.function.math;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathRandomLongFunction extends TestBase {
    @Test
    public void case1() {
        long result = ExpressionEngine.execute("math.randomLong()");

        System.out.println(result);
    }

    @Test
    public void case2() {
        long result = ExpressionEngine.execute("math.randomLong(3)");

        System.out.println(result);
    }
}

