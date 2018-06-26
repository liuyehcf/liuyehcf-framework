package org.liuyehcf.compile.engine.hua.runtime;

/**
 * @author chenlu
 * @date 2018/6/26
 */
public class RuntimeContext {

    /**
     * 方法栈
     */
    private final MethodStack methodStack;


    /**
     * 当前执行的方法
     */
    private final MethodRuntimeInfo currentMethod;

    public RuntimeContext(MethodStack methodStack) {
        this.methodStack = methodStack;
        this.currentMethod = methodStack.peek();
    }

    public MethodStack getMethodStack() {
        return methodStack;
    }

    public MethodRuntimeInfo getCurrentMethod() {
        return currentMethod;
    }

    public OperatorStack getOperatorStack() {
        return currentMethod.getOperatorStack();
    }
}
