package org.liuyehcf.compile.engine.expression.test;

import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class TestBase {

    @BeforeClass
    public static void init() throws Exception {
        Class.forName("org.liuyehcf.compile.engine.expression.ExpressionEngine");
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
