package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfo;

import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;

/**
 * 方法运行时信息，方法栈的元素
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
class MethodRuntimeInfo {

    /**
     * Hua编译后的中间形式
     */
    private final IntermediateInfo intermediateInfo;

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
     * 第一维度：方法中有效符号的最大个数
     * 第二维度：固定是8，即允许任意类型的数值
     * 为什么要这样做，而不用一维数组？因为根据标志符序号来获取内存偏移量比较难，因为同一个标志符序号，在不同时刻，偏移量是不同的。例如如下代码
     * {
     * int i;
     * int j;
     * }
     * {
     * boolean i;
     * int j;
     * }
     * 在上述代码中，标志符i、j分别位于两个block中，j的序号都是2，但是j的offset却是不同的
     */
    private final byte[][] stackMemory;

    /**
     * 代码偏移量
     */
    private int codeOffset = 0;

    /**
     * 返回值
     */
    private Object result;

    /**
     * 是否执行完毕
     */
    private boolean isFinished = false;


    MethodRuntimeInfo(IntermediateInfo intermediateInfo, MethodInfo methodInfo) {
        this.intermediateInfo = intermediateInfo;
        this.methodInfo = methodInfo;
        stackMemory = new byte[this.methodInfo.getMaxOrder()][Type.REFERENCE_TYPE_WIDTH];
    }

    Object run(Object[] args) {
        setArgs(args);

        while (!isFinished) {
            methodInfo.getByteCodes().get(codeOffset).operate(new RuntimeContext(intermediateInfo, this));
        }

        return result;
    }

    private void setArgs(Object[] args) {
        List<Type> paramTypeList = methodInfo.getParamTypeList();
        assertEquals(args.length, paramTypeList.size(), "[SYSTEM_ERROR] - The actual parameter quantity must be consistent with the shape parameter quantity");

        for (int i = 0; i < args.length; i++) {
            Type paramType = paramTypeList.get(i);
            if (paramType.isArrayType()) {
                storeReference(i, (Reference) args[i]);
            } else if (Type.TYPE_INT.equals(paramType)) {
                storeInt(i, (int) args[i]);
            } else if (Type.TYPE_BOOLEAN.equals(paramType)) {
                storeInt(i, (int) args[i]);
            }
        }
    }

    void setResult(Object result) {
        this.result = result;
    }

    OperatorStack getOperatorStack() {
        return operatorStack;
    }

    void increaseCodeOffset() {
        codeOffset++;
    }

    void setCodeOffset(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    void finishMethod() {
        isFinished = true;
    }

    int loadInt(int order) {
        return ByteUtils.loadInt(stackMemory[order], 0);
    }

    void storeInt(int order, int value) {
        ByteUtils.storeInt(stackMemory[order], 0, value);
    }

    long loadLong(int order) {
        return ByteUtils.loadLong(stackMemory[order], 0);
    }

    void storeLong(int order, long value) {
        ByteUtils.storeLong(stackMemory[order], 0, value);
    }

    float loadFloat(int order) {
        return ByteUtils.loadFloat(stackMemory[order], 0);
    }

    void storeFloat(int order, float value) {
        ByteUtils.storeFloat(stackMemory[order], 0, value);
    }

    double loadDouble(int order) {
        return ByteUtils.loadDouble(stackMemory[order], 0);
    }

    void storeDouble(int order, double value) {
        ByteUtils.storeDouble(stackMemory[order], 0, value);
    }

    Reference loadReference(int order) {
        return ByteUtils.loadReference(stackMemory[order], 0);
    }

    void storeReference(int order, Reference reference) {
        ByteUtils.storeReference(stackMemory[order], 0, reference);
    }
}
