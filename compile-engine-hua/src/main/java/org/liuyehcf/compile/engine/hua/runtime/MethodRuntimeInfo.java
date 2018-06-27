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
public class MethodRuntimeInfo {

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
        stackMemory = new byte[this.methodInfo.getMaxOrder()][8];
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
        assertEquals(args.length, paramTypeList.size());

        for (int i = 0; i < args.length; i++) {
            Type paramType = paramTypeList.get(i);
            if (paramType.isArrayType()) {
                storeReference(i, (int) args[i]);
            } else if (Type.TYPE_INT.equals(paramType)) {
                storeInt(i, (int) args[i]);
            } else if (Type.TYPE_BOOLEAN.equals(paramType)) {
                storeBoolean(i, (int) args[i]);
            }
        }
    }

    public OperatorStack getOperatorStack() {
        return operatorStack;
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

    public int loadInt(int order) {
        return ByteUtil.loadInt(stackMemory[order], 0);
    }

    public void storeInt(int order, int value) {
        ByteUtil.storeInt(stackMemory[order], 0, value);
    }

    public int loadBoolean(int order) {
        return ByteUtil.loadBoolean(stackMemory[order], 0);
    }

    public void storeBoolean(int order, int value) {
        ByteUtil.storeBoolean(stackMemory[order], 0, value);
    }

    public int loadReference(int order) {
        return ByteUtil.loadReference(stackMemory[order], 0);
    }

    public void storeReference(int order, int value) {
        ByteUtil.storeReference(stackMemory[order], 0, value);
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
