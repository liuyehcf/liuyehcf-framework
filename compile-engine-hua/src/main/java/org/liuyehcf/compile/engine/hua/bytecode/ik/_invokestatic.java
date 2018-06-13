package org.liuyehcf.compile.engine.hua.bytecode.ik;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class _invokestatic extends Invoke {
    private final String methodName;

    private final int argumentSize;

    public _invokestatic(String methodName, int argumentSize) {
        this.methodName = methodName;
        this.argumentSize = argumentSize;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getArgumentSize() {
        return argumentSize;
    }

    @Override
    public void operate() {

    }
}
