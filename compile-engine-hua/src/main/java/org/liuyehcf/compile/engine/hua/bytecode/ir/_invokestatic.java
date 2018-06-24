package org.liuyehcf.compile.engine.hua.bytecode.ir;

/**
 * 方法调用指令
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _invokestatic extends InvokeAndReturn {

    /**
     * 常量池偏移量
     */
    private final int constantPoolOffset;

    public _invokestatic(int constantPoolOffset) {
        this.constantPoolOffset = constantPoolOffset;
    }

    public int getConstantPoolOffset() {
        return constantPoolOffset;
    }

    @Override
    public void operate() {

    }
}
