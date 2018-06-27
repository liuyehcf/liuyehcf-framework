package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodSignature;
import org.liuyehcf.compile.engine.hua.core.SystemMethod;

/**
 * @author chenlu
 * @date 2018/6/26
 */
public class RuntimeContext {

    /**
     * Hua编译后的中间形式
     */
    private final IntermediateInfo intermediateInfo;

    /**
     * 方法栈
     */
    private final MethodStack methodStack;

    /**
     * 当前执行的方法
     */
    private final MethodRuntimeInfo currentMethod;

    public RuntimeContext(IntermediateInfo intermediateInfo, MethodStack methodStack) {
        this.intermediateInfo = intermediateInfo;
        this.methodStack = methodStack;
        this.currentMethod = methodStack.peek();
    }

    public void push(Object obj) {
        currentMethod.getOperatorStack().push(obj);
    }

    public <T> T pop() {
        return currentMethod.getOperatorStack().pop();
    }

    public void invoke(MethodSignature methodSignature, Object[] args) {
        if (intermediateInfo.getMethodInfoTable().isSystemMethod(methodSignature)) {
            SystemMethod.invoke(methodSignature, args);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public String getConstant(int constantOffset) {
        return intermediateInfo.getConstantPool().getConstant(constantOffset);
    }

    public void increaseCodeOffset() {
        currentMethod.increaseCodeOffset();
    }

    public void setCodeOffset(int codeOffset) {
        currentMethod.setCodeOffset(codeOffset);
    }

    public int loadInt(int offset) {
        return currentMethod.loadInt(offset);
    }

    public void storeInt(int offset, int value) {
        currentMethod.storeInt(offset, value);
    }

    public int loadBoolean(int offset) {
        return currentMethod.loadBoolean(offset);
    }

    public void storeBoolean(int offset, int value) {
        currentMethod.storeBoolean(offset, value);
    }

    public int loadReference(int offset) {
        return currentMethod.loadReference(offset);
    }

    public void storeReference(int offset, int value) {
        currentMethod.storeReference(offset, value);
    }
}
