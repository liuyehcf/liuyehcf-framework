package org.liuyehcf.compile.engine.expression.test.function.math;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestMathRandomDoubleFunction extends TestBase {
    @Test
    public void case1() {
        double result = ExpressionEngine.execute("math.randomDouble()");

        System.out.println(result);
    }
}
