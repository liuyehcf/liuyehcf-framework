package org.liuyehcf.compile.engine.expression.runtime;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.liuyehcf.compile.engine.core.utils.Assert;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;
import org.liuyehcf.compile.engine.expression.core.bytecode.ByteCode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class ExpressionVirtualMachine implements RuntimeContext {

    private final LinkedList<ExpressionValue> operatorStack = new LinkedList<>();

    private final ExpressionCode expressionCode;

    private final Map<String, Object> env;

    private int codeOffset = 0;

    private ExpressionVirtualMachine(ExpressionCode expressionCode, Map<String, Object> env) {
        Assert.assertNotNull(expressionCode);
        this.expressionCode = expressionCode;
        if (env == null) {
            this.env = Maps.newHashMap();
        } else {
            this.env = env;
        }
    }

    public static ExpressionValue execute(ExpressionCode expressionCode, Map<String, Object> env) {
        return new ExpressionVirtualMachine(expressionCode, env).execute();
    }

    @Override
    public void push(ExpressionValue originOperator) {
        operatorStack.push(originOperator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExpressionValue pop() {
        return operatorStack.pop();
    }

    @Override
    public void increaseCodeOffset() {
        codeOffset++;
    }

    @Override
    public void setCodeOffset(int offset) {
        codeOffset = offset;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getProperty(String propertyName) {
        try {
            return PropertyUtils.getProperty(env, propertyName);
        } catch (Throwable e) {
            return null;
        }
    }

    private ExpressionValue execute() {
        final List<ByteCode> byteCodes = expressionCode.getByteCodes();
        while (codeOffset < byteCodes.size()) {
            byteCodes.get(codeOffset).operate(this);
        }

        return operatorStack.pop();
    }
}
