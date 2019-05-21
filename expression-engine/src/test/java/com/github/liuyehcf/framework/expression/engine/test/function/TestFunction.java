package com.github.liuyehcf.framework.expression.engine.test.function;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.FunctionUtils;
import org.junit.Test;

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
