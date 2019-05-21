package com.github.liuyehcf.framework.expression.engine.core.bytecode.ir;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionValue;
import com.github.liuyehcf.framework.expression.engine.runtime.RuntimeContext;
import com.github.liuyehcf.framework.expression.engine.utils.FunctionUtils;

import java.util.LinkedList;

/**
 * 方法调用指令
 * < before → after >
 * < arg1,arg2,... → result >
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class _invokestatic extends Invoke {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb8;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{String.class, int.class};

    /**
     * 方法名称
     */
    private final String functionName;

    /**
     * 参数长度
     */
    private final int argSize;

    public _invokestatic(String functionName, int argSize) {
        this.functionName = functionName;
        this.argSize = argSize;
    }

    public String getFunctionName() {
        return functionName;
    }

    public int getArgSize() {
        return argSize;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{functionName, argSize};
    }

    @Override
    public void operate(RuntimeContext context) {
        Function function = FunctionUtils.getFunctionByName(functionName);

        LinkedList<ExpressionValue> args = new LinkedList<>();
        int size = argSize;
        while (size > 0) {
            size--;
            args.addFirst(context.pop());
        }

        ExpressionValue result = FunctionUtils.invoke(function, args.toArray(new ExpressionValue[0]));
        Assert.assertNotNull(result, "function's result cannot be null");

        context.push(result);
        context.increaseCodeOffset();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getFunctionName() + ":" + getArgSize();
    }
}
