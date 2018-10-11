package org.liuyehcf.compile.engine.expression.test.function;

import org.junit.Test;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.test.TestBase;
import org.liuyehcf.compile.engine.expression.utils.FunctionUtils;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public class TestFunction extends TestBase {
    @Test
    public void caseException() {
        expectedException(() -> {
            ExpressionValue[] args = new ExpressionValue[15];
            for (int i = 0; i < 15; i++) {
                args[i] = ExpressionValue.valueOf(i);
            }

            FunctionUtils.invoke(
                    FunctionUtils.getFunctionByName("string.substring"),
                    args
            );
        }, ExpressionException.class, "string.substring(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, args) hasn't been defined");
    }
}
