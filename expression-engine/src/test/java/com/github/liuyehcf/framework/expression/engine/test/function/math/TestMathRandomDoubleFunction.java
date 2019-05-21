package com.github.liuyehcf.framework.expression.engine.test.function.math;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import org.junit.Test;

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
