package org.liuyehcf.compile.engine.hua.runtime;

import java.util.LinkedList;

/**
 * 方法调用堆栈
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
public class MethodStack {
    private final LinkedList<MethodRuntimeInfo> stack = new LinkedList<>();

    public void push(MethodRuntimeInfo methodRuntimeInfo) {
        stack.push(methodRuntimeInfo);
    }

    public MethodRuntimeInfo pop() {
        return stack.pop();
    }
}
