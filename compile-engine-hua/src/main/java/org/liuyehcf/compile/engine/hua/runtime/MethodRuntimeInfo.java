package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.core.MethodInfo;

/**
 * 方法运行时信息，方法栈的元素
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
class MethodRuntimeInfo {

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

    MethodRuntimeInfo(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
        stackMemory = new byte[this.methodInfo.getMaxOffset()];
    }
}
