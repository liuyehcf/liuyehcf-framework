package org.liuyehcf.compile.engine.hua.bytecode.ik;

/**
 * 方法调用指令
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _invokestatic extends Invoke {
    private final String methodDescription;

    public _invokestatic(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public String getMethodDescription() {
        return methodDescription;
    }

    @Override
    public void operate() {

    }
}
