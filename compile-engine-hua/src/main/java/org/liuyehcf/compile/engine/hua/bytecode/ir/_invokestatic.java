package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 方法调用指令
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _invokestatic extends InvokeAndReturn {
    private final String methodSignature;

    public _invokestatic(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    @Override
    public void operate() {

    }
}
