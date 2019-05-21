package com.github.liuyehcf.framework.expression.engine.test;

import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class TestBase {

    @BeforeClass
    public static void init() throws Exception {
        Class.forName("com.github.liuyehcf.framework.expression.engine.ExpressionEngine");
    }

    protected void expectedException(Runnable testCase, Class<? extends Throwable> expectedException, String message) {
        try {
            testCase.run();
        } catch (Throwable e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(message, e.getMessage());
            return;
        }
        throw new RuntimeException();
    }
}
