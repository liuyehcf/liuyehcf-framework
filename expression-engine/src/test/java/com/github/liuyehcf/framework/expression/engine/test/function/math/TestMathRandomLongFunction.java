package com.github.liuyehcf.framework.expression.engine.test.function.math;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

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

