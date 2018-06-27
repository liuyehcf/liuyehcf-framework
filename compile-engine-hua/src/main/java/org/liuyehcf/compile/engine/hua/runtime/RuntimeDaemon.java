package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfo;

import static org.liuyehcf.compile.engine.hua.core.MethodInfo.buildMethodSignature;

/**
 * @author hechenfeng
 * @date 2018/6/26
 */
public class RuntimeDaemon {

    /**
     * Hua编译后的中间形式
     */
    private final IntermediateInfo intermediateInfo;

    private final MethodStack methodStack = new MethodStack();

    public RuntimeDaemon(IntermediateInfo intermediateInfo) {
        this.intermediateInfo = intermediateInfo;
        methodStack.push(new MethodRuntimeInfo(intermediateInfo, getMainMethod()));
    }

    private MethodInfo getMainMethod() {
        MethodInfo mainMethod = intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(buildMethodSignature("main", null));
        if (mainMethod == null) {
            throw new RuntimeException("源文件没有定义main方法");
        }
        return mainMethod;
    }

    public void doExecute() {
        methodStack.peek().run(methodStack, new Object[0]);
    }
}
