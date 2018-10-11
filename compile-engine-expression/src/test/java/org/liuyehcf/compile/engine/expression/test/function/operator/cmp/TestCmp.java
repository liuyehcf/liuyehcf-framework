package org.liuyehcf.compile.engine.expression.test.function.operator.cmp;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.core.function.DelegateOperatorFunction;
import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.core.function.OperatorFunction;
import org.liuyehcf.compile.engine.expression.core.function.operator.cmp.CmpOperatorFunction;
import org.liuyehcf.compile.engine.expression.core.model.OperatorType;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.test.TestBase;
import org.liuyehcf.compile.engine.expression.utils.EnvBuilder;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class TestCmp extends TestBase {
    @Test
    public void caseLessLong() {
        boolean result = ExpressionEngine.execute("2 < 3");
        assertTrue(result);
    }

    @Test
    public void caseLessDouble() {
        boolean result = ExpressionEngine.execute("1.1d < 2.3D");
        assertTrue(result);

        result = ExpressionEngine.execute("1.0f < 2.1f");
        assertTrue(result);
    }

    @Test
    public void caseLessWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true < 1L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 <false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false<1.0f"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10<true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false < 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'< true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 < 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'< 0L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. < 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'< .0"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true< null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null < false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1L< null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null < -1"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1.0f< null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null < -1.3e-2"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'< null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null < 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.String'");
    }

    @Test
    public void caseLessEqualLong() {
        boolean result = ExpressionEngine.execute("2 <= 2");
        assertTrue(result);
    }

    @Test
    public void caseLessEqualDouble() {
        boolean result = ExpressionEngine.execute("1.1d <= 1.1D");
        assertTrue(result);

        result = ExpressionEngine.execute("1.0f <= 1.0f");
        assertTrue(result);
    }

    @Test
    public void caseLessEqualWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true <= 1L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 <=false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false<=1.0f"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10<=true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false <= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'<= true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 <= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'<= 0L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. <= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'<= .0"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true<= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null <= false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1L<= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null <= -1"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1.0f<= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null <= -1.3e-2"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'<= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null <= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.String'");
    }

    @Test
    public void caseLargeLong() {
        boolean result = ExpressionEngine.execute("3 > 2");
        assertTrue(result);
    }

    @Test
    public void caseLargeDouble() {
        boolean result = ExpressionEngine.execute("5.55d > 1.1D");
        assertTrue(result);

        result = ExpressionEngine.execute("5.55f > 1.0f");
        assertTrue(result);
    }

    @Test
    public void caseLargeWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true > 1L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 >false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false>1.0f"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10>true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false > 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'> true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 > 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'> 0L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. > 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'> .0"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true> null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null > false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1L> null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null > -1"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1.0f> null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null > -1.3e-2"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'> null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null > 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.String'");
    }

    @Test
    public void caseLargeEqualLong() {
        boolean result = ExpressionEngine.execute("2 >= 2");
        assertTrue(result);
    }

    @Test
    public void caseLargeEqualDouble() {
        boolean result = ExpressionEngine.execute("1.1d >= 1.1D");
        assertTrue(result);

        result = ExpressionEngine.execute("1.0f >= 1.0f");
        assertTrue(result);
    }

    @Test
    public void caseLargeEqualWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true >= 1L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 >=false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false>=1.0f"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10>=true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false >= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'>= true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 >= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'>= 0L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. >= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'>= .0"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true>= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null >= false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1L>= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null >= -1"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1.0f>= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null >= -1.3e-2"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'>= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null >= 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.String'");
    }

    @Test
    public void caseEqualLong() {
        boolean result = ExpressionEngine.execute("2 == 2");
        assertTrue(result);
    }

    @Test
    public void caseEqualDouble() {
        boolean result = ExpressionEngine.execute("1.1d == 1.1D");
        assertTrue(result);

        result = ExpressionEngine.execute("1.0f == 1.0f");
        assertTrue(result);
    }

    @Test
    public void caseEqualWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true == 1L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 ==false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false==1.0f"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10==true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false == 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'== true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 == 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'== 0L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. == 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'== .0"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true== null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null == false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1L== null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null == -1"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1.0f== null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null == -1.3e-2"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'== null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null == 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.String'");
    }

    @Test
    public void caseNotEqualLong() {
        boolean result = ExpressionEngine.execute("2 != 2");
        assertFalse(result);
    }

    @Test
    public void caseNotEqualDouble() {
        boolean result = ExpressionEngine.execute("1.1d != 1.1D");
        assertFalse(result);

        result = ExpressionEngine.execute("1.0f != 1.0f");
        assertFalse(result);
    }

    @Test
    public void caseNotEqualWrong() {
        expectedException(
                () -> ExpressionEngine.execute("true != 1L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1 !=false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false!=1.0f"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("1e-10!=true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("false != 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'!= true"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1 != 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'!= 0L"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1. != 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='java.lang.String'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'!= .0"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("true!= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Boolean', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null != false"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Boolean'");

        expectedException(
                () -> ExpressionEngine.execute("1L!= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Long', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null != -1"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Long'");

        expectedException(
                () -> ExpressionEngine.execute("1.0f!= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.Double', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null != -1.3e-2"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.Double'");

        expectedException(
                () -> ExpressionEngine.execute("'hello'!= null"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='java.lang.String', type2='null'");

        expectedException(
                () -> ExpressionEngine.execute("null != 'hello'"),
                ExpressionException.class,
                "could not find compatible operator(cmp) function. type1='null', type2='java.lang.String'");
    }

    @Test
    @SuppressWarnings("all")
    public void caseCustomOperatorFunction() {
        // return long
        OperatorFunction operatorFunction = new CmpOperatorFunction() {
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof Boolean && arg2.getValue() instanceof Boolean;
            }

            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                return ExpressionValue.valueOf(-1L);
            }
        };
        String id = ExpressionEngine.addOperatorFunction(operatorFunction);

        Object result = ExpressionEngine.execute("true < false");
        assertEquals(true, result);

        DelegateOperatorFunction removedOperatorFunction = (DelegateOperatorFunction) ExpressionEngine.removeOperatorFunction(OperatorType.CMP, id);
        assertEquals(operatorFunction, removedOperatorFunction.getOperatorFunction());

        // return int
        operatorFunction = new CmpOperatorFunction() {
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof Boolean && arg2.getValue() instanceof Boolean;
            }

            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                return ExpressionValue.valueOf(0);
            }
        };
        id = ExpressionEngine.addOperatorFunction(operatorFunction);

        result = ExpressionEngine.execute("false != false");
        assertEquals(false, result);

        removedOperatorFunction = (DelegateOperatorFunction) ExpressionEngine.removeOperatorFunction(OperatorType.CMP, id);
        assertEquals(operatorFunction, removedOperatorFunction.getOperatorFunction());

        // return short
        operatorFunction = new CmpOperatorFunction() {
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof Boolean && arg2.getValue() instanceof Boolean;
            }

            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                short s = 5;
                return ExpressionValue.valueOf(s);
            }
        };
        id = ExpressionEngine.addOperatorFunction(operatorFunction);

        result = ExpressionEngine.execute("false > false");
        assertEquals(true, result);

        removedOperatorFunction = (DelegateOperatorFunction) ExpressionEngine.removeOperatorFunction(OperatorType.CMP, id);
        assertEquals(operatorFunction, removedOperatorFunction.getOperatorFunction());

        // return byte
        operatorFunction = new CmpOperatorFunction() {
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof Boolean && arg2.getValue() instanceof Boolean;
            }

            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                byte b = -10;
                return ExpressionValue.valueOf(b);
            }
        };
        id = ExpressionEngine.addOperatorFunction(operatorFunction);

        result = ExpressionEngine.execute("false <= true");
        assertEquals(true, result);

        removedOperatorFunction = (DelegateOperatorFunction) ExpressionEngine.removeOperatorFunction(OperatorType.CMP, id);
        assertEquals(operatorFunction, removedOperatorFunction.getOperatorFunction());
    }

    @Test
    public void caseReturnTypeWrong() {
        String id = ExpressionEngine.addOperatorFunction(new CmpOperatorFunction() {
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof Boolean && arg2.getValue() instanceof Boolean;
            }

            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                return ExpressionValue.valueOf(new Object());
            }
        });
        expectedException(
                () -> ExpressionEngine.execute("true < false"),
                ExpressionException.class,
                "CmpOperatorFunction's return type must be 'java.lang.Long' (or compatible type 'java.lang.Byte', 'java.lang.Short', 'java.lang.Integer')");

        Function function = ExpressionEngine.removeOperatorFunction(OperatorType.CMP, id);
        assertNotNull(function);
    }

    @Test
    public void caseNormalComparableObject() {
        final class MyVal implements Comparable<MyVal> {
            private final int value;

            public MyVal(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }

            @Override
            public int compareTo(MyVal o) {
                return Integer.compare(value, o.getValue());
            }
        }

        Map<String, Object> env = EnvBuilder.builder().put("a.b", new MyVal(1)).put("c", new MyVal(3)).build();

        boolean result = ExpressionEngine.execute("a.b < c", env);
        assertTrue(result);

        result = ExpressionEngine.execute("a.b <= c", env);
        assertTrue(result);

        result = ExpressionEngine.execute("a.b > c", env);
        assertFalse(result);

        result = ExpressionEngine.execute("a.b >= c", env);
        assertFalse(result);

        result = ExpressionEngine.execute("a.b == c", env);
        assertFalse(result);

        result = ExpressionEngine.execute("a.b != c", env);
        assertTrue(result);
    }
}
