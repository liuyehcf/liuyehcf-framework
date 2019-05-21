package com.github.liuyehcf.framework.language.hua.core.bytecode.ir;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.core.MethodInfo;
import com.github.liuyehcf.framework.language.hua.core.MethodSignature;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeContext;

/**
 * 方法调用指令
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _invokestatic extends Invoke {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb8;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 常量池偏移量
     */
    private final int constantPoolOffset;

    public _invokestatic(int constantPoolOffset) {
        this.constantPoolOffset = constantPoolOffset;
    }

    public int getConstantPoolOffset() {
        return constantPoolOffset;
    }

    @Override
    public void operate(RuntimeContext context) {
        MethodSignature methodSignature = MethodSignature.parse(context.getConstant(constantPoolOffset));
        MethodInfo methodInfo = context.getMethodInfoByMethodSignature(methodSignature);

        int paramSize = methodSignature.getTypeDescriptions().length;

        Object[] args = new Object[paramSize];
        for (int i = paramSize - 1; i >= 0; i--) {
            args[i] = context.pop();
        }

        Object result = context.invoke(methodSignature, args);
        Type resultType = methodInfo.getResultType();

        if (!Type.TYPE_VOID.equals(resultType)) {
            context.push(result);
        }

        context.increaseCodeOffset();
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{constantPoolOffset};
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " #" + constantPoolOffset;
    }
}
