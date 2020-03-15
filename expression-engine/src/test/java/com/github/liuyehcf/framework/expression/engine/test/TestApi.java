package com.github.liuyehcf.framework.expression.engine.test;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.Option;
import com.github.liuyehcf.framework.expression.engine.core.function.OperatorFunction;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.add.AddOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.add.AddOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.add.AddOperatorFunctionForString;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.bitand.BitAndOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.bitor.BitOrOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.bitxor.BitXorOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForComparableObject;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForString;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.div.DivOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.div.DivOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.mul.MulOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.mul.MulOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.neg.NegOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.neg.NegOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.rem.RemOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.rem.RemOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.shl.ShlOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.shr.ShrOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.sub.SubOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.sub.SubOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.ushr.UshrOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.model.OperatorType;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/3/15
 */
public class TestApi {

    @Test
    public void getOption() {
        boolean result = ExpressionEngine.getOption(Option.OPTIMIZE_CODE);
        Assert.assertTrue(result);

        ExpressionEngine.setOption(Option.OPTIMIZE_CODE, false);
        result = ExpressionEngine.getOption(Option.OPTIMIZE_CODE);
        Assert.assertFalse(result);
    }

    @Test
    public void cleanOperationFunction() {
        List<OperatorFunction> operatorFunctions;
        for (OperatorType value : OperatorType.values()) {
            operatorFunctions = ExpressionEngine.getOperatorFunctions(value);
            Assert.assertNotEmpty(operatorFunctions);
        }

        ExpressionEngine.cleanOperatorFunctions(OperatorType.ADD);

        for (OperatorType value : OperatorType.values()) {
            operatorFunctions = ExpressionEngine.getOperatorFunctions(value);
            if (Objects.equals(value, OperatorType.ADD)) {
                Assert.assertEmpty(operatorFunctions);
            } else {
                Assert.assertNotEmpty(operatorFunctions);
            }
        }

        ExpressionEngine.cleanOperatorFunctions();

        initOption();
    }

    @Test
    public void cleanOperationFunctions() {
        List<OperatorFunction> operatorFunctions;
        for (OperatorType value : OperatorType.values()) {
            operatorFunctions = ExpressionEngine.getOperatorFunctions(value);
            Assert.assertNotEmpty(operatorFunctions);
        }

        ExpressionEngine.cleanOperatorFunctions();

        for (OperatorType value : OperatorType.values()) {
            operatorFunctions = ExpressionEngine.getOperatorFunctions(value);
            Assert.assertEmpty(operatorFunctions);
        }

        initOption();
    }

    private void initOption() {
        // load add operator functions
        ExpressionEngine.addOperatorFunction(new AddOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new AddOperatorFunctionForDouble());
        ExpressionEngine.addOperatorFunction(new AddOperatorFunctionForString());

        // load bit and operator functions
        ExpressionEngine.addOperatorFunction(new BitAndOperatorFunctionForLong());

        // load bit or operator functions
        ExpressionEngine.addOperatorFunction(new BitOrOperatorFunctionForLong());

        // load bit xor operator functions
        ExpressionEngine.addOperatorFunction(new BitXorOperatorFunctionForLong());

        // load cmp operator functions
        ExpressionEngine.addOperatorFunction(new CmpOperatorFunctionForComparableObject());
        ExpressionEngine.addOperatorFunction(new CmpOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new CmpOperatorFunctionForDouble());
        ExpressionEngine.addOperatorFunction(new CmpOperatorFunctionForString());

        // load div operator functions
        ExpressionEngine.addOperatorFunction(new DivOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new DivOperatorFunctionForDouble());

        // load mul operator functions
        ExpressionEngine.addOperatorFunction(new MulOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new MulOperatorFunctionForDouble());

        // load neg operator functions
        ExpressionEngine.addOperatorFunction(new NegOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new NegOperatorFunctionForDouble());

        // load rem operator functions
        ExpressionEngine.addOperatorFunction(new RemOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new RemOperatorFunctionForDouble());

        // load shl operator functions
        ExpressionEngine.addOperatorFunction(new ShlOperatorFunctionForLong());

        // load shr operator functions
        ExpressionEngine.addOperatorFunction(new ShrOperatorFunctionForLong());

        // load sub operator functions
        ExpressionEngine.addOperatorFunction(new SubOperatorFunctionForLong());
        ExpressionEngine.addOperatorFunction(new SubOperatorFunctionForDouble());

        // LOAD ushr operator functions
        ExpressionEngine.addOperatorFunction(new UshrOperatorFunctionForLong());
    }
}
