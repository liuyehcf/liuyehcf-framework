package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.core.MethodInfo;

/**
 * 方法运行时信息，方法栈的元素
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
public class MethodRuntimeInfo {

    /**
     * 方法信息
     */
    private final MethodInfo methodInfo;

    /**
     * 操作数栈
     */
    private final OperatorStack operatorStack = new OperatorStack();

    /**
     * 栈内存
     */
    private final byte[] stackMemory;

    /**
     * 代码偏移量
     */
    private int codeOffset = 0;

    /**
     * 是否执行完毕
     */
    private boolean isFinished = false;

    MethodRuntimeInfo(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
        stackMemory = new byte[this.methodInfo.getMaxOffset()];
    }

    void run(MethodStack methodStack) {
        while (!isFinished) {
            methodInfo.getByteCodes().get(codeOffset).operate(new RuntimeContext(methodStack));
        }
    }

    public void increaseCodeOffset() {
        codeOffset++;
    }

    public void setCodeOffset(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    public void finishMethod() {
        isFinished = true;
    }

    public OperatorStack getOperatorStack() {
        return operatorStack;
    }
}
