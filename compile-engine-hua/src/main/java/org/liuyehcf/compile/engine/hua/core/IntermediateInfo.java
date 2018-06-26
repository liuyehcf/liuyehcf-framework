package org.liuyehcf.compile.engine.hua.core;

/**
 * Hua编译后的中间形式
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class IntermediateInfo {

    /**
     * 常量池
     */
    private final ConstantPool constantPool;

    /**
     * 方法表
     */
    private final MethodInfoTable methodInfoTable;

    public IntermediateInfo(ConstantPool constantPool, MethodInfoTable methodInfoTable) {
        this.constantPool = constantPool;
        this.methodInfoTable = methodInfoTable;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public MethodInfoTable getMethodInfoTable() {
        return methodInfoTable;
    }
}
