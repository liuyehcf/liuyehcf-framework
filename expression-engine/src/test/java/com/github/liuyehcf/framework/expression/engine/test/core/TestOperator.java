package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.core.function.VarargsFunction;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestOperator extends TestBase {
    @Test
    public void caseBitReverseLong() {
        long result = ExpressionEngine.execute("~100");
        assertEquals((~100L), result);

        result = ExpressionEngine.execute("~0L");
        assertEquals((~0L), result);

        result = ExpressionEngine.execute("~-0L");
        assertEquals((~0L), result);

        result = ExpressionEngine.execute("~-100L");
        assertEquals((~-100L), result);
    }

    @Test
    public void caseBooleanNotTrue() {
        boolean result = ExpressionEngine.execute("!true");
        assertFalse(result);

        result = ExpressionEngine.execute("!(1<3)");
        assertFalse(result);

        result = ExpressionEngine.execute("!(3<=3)");
        assertFalse(result);

        result = ExpressionEngine.execute("!(1>0)");
        assertFalse(result);

        result = ExpressionEngine.execute("!(1>=1)");
        assertFalse(result);

        result = ExpressionEngine.execute("!(1==1)");
        assertFalse(result);

        result = ExpressionEngine.execute("!(1!=0)");
        assertFalse(result);
    }

    @Test
    public void caseBooleanNotFalse() {
        boolean result = ExpressionEngine.execute("!false");
        assertTrue(result);

        result = ExpressionEngine.execute("!(1<0)");
        assertTrue(result);

        result = ExpressionEngine.execute("!(3<=2)");
        assertTrue(result);

        result = ExpressionEngine.execute("!(1>1)");
        assertTrue(result);

        result = ExpressionEngine.execute("!(1>=3)");
        assertTrue(result);

        result = ExpressionEngine.execute("!(1==2)");
        assertTrue(result);

        result = ExpressionEngine.execute("!(1!=1)");
        assertTrue(result);
    }

    @Test
    public void caseBooleanNotOnExpressionBoundary() {
        ExpressionEngine.addFunction(new VarargsFunction() {
            @Override
            public ExpressionValue variadicCall(ExpressionValue... args) {
                Object[] result = new Object[args.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = args[i].getValue();
                }
                return ExpressionValue.valueOf(result);
            }

            @Override
            public String getName() {
                return "test.pass";
            }
        });

        Object result = ExpressionEngine.execute("(1<2) ? !(2<3) : !(2>=3)");
        assertEquals(false, result);

        result = ExpressionEngine.execute("(2<2) ? !(2<3) : !(2>=3)");
        assertEquals(true, result);

        // arg list
        ExpressionCode code = ExpressionEngine.compile("test.pass(!(2<3))");
        result = code.execute();
        assertEquals(1, Array.getLength(result));
        assertEquals(false, Array.get(result, 0));

        code = ExpressionEngine.compile("test.pass(1,!(2<3),(2<2) ? !(2<3) : !(2>=3))");
        result = code.execute();
        assertEquals(3, Array.getLength(result));
        assertEquals(1L, Array.get(result, 0));
        assertEquals(false, Array.get(result, 1));
        assertEquals(true, Array.get(result, 2));

        // array access
        Map<Boolean, Integer> boolMap = Maps.newHashMap();
        boolMap.put(true, 1);
        boolMap.put(false, 0);
        Map<String, Object> env = EnvBuilder.builder().put("a", boolMap).build();

        code = ExpressionEngine.compile("test.pass(a[true],a[(1<2) ? !(2<3) : !(2>=3)])");
        result = code.execute(env);
        assertEquals(2, Array.getLength(result));
        assertEquals(1L, Array.get(result, 0));
        assertEquals(0L, Array.get(result, 1));

        // array initializer
        code = ExpressionEngine.compile("[!(2<3)]");
        result = code.execute(env);
        assertEquals(1, Array.getLength(result));
        assertEquals(false, Array.get(result, 0));

        code = ExpressionEngine.compile("[(1<2) ? !(2<3) : !(2>=3),1L,(2<2) ? !(2<3) : !(2>=3)]");
        result = code.execute(env);
        assertEquals(3, Array.getLength(result));
        assertEquals(false, Array.get(result, 0));
        assertEquals(1L, Array.get(result, 1));
        assertEquals(true, Array.get(result, 2));

        Function removedFunction = ExpressionEngine.removeFunction("test.pass");
        assertNotNull(removedFunction);
    }
}
