package org.liuyehcf.compile.engine.hua.runtime;

import java.util.LinkedList;

/**
 * 方法调用堆栈
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
class MethodStack {
    private final LinkedList<MethodRuntimeInfo> stack = new LinkedList<>();

    void push(MethodRuntimeInfo methodRuntimeInfo) {
        stack.push(methodRuntimeInfo);
    }

    MethodRuntimeInfo pop() {
        return stack.pop();
    }
}
