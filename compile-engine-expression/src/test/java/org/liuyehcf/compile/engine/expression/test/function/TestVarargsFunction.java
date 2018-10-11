package org.liuyehcf.compile.engine.expression.test.function;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.core.function.VarargsFunction;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestVarargsFunction extends TestBase {
    @Test
    public void case1() {
        PrintAll function = new PrintAll();
        ExpressionEngine.addFunction(function);

        assertEquals("1 ", ExpressionEngine.execute("printAll(1)"));
        assertEquals("1 2 ", ExpressionEngine.execute("printAll(1, 2)"));
        assertEquals("1 2 3 ", ExpressionEngine.execute("printAll(1, 2, 3)"));
        assertEquals("1 2 3 4 ", ExpressionEngine.execute("printAll(1, 2, 3, 4)"));
        assertEquals("1 2 3 4 5 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5)"));
        assertEquals("1 2 3 4 5 6 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6)"));
        assertEquals("1 2 3 4 5 6 7 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7)"));
        assertEquals("1 2 3 4 5 6 7 8 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8)"));
        assertEquals("1 2 3 4 5 6 7 8 9 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9)"));
        assertEquals("1 2 3 4 5 6 7 8 9 10 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9, 10)"));
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9, 10, 11)"));
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9, 10, 11, 12)"));
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9, 10, 11, 12, 13)"));
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9, 10, 11, 12, 13, 14)"));
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 ", ExpressionEngine.execute("printAll(1, 2, 3, 4, 5,6, 7,8, 9, 10, 11, 12, 13, 14, 15)"));

        Function removedFunction = ExpressionEngine.removeFunction(function.getName());
        assertNotNull(removedFunction);
    }

    private static final class PrintAll extends VarargsFunction {

        @Override
        public ExpressionValue variadicCall(ExpressionValue... args) {
            StringBuilder sb = new StringBuilder();

            for (ExpressionValue arg : args) {
                sb.append(Objects.toString(arg.getValue())).append(' ');
            }

            return ExpressionValue.valueOf(sb.toString());
        }

        @Override
        public String getName() {
            return "printAll";
        }
    }

}
