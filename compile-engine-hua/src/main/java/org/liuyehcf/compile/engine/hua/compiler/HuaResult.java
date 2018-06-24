package org.liuyehcf.compile.engine.hua.compiler;

/**
 * Hua编译后的结果
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class HuaResult {

    /**
     * 常量池
     */
    private final ConstantPool constantPool;

    /**
     * 符号表
     */
    private final VariableSymbolTable variableSymbolTable;

    /**
     * 方法表
     */
    private final MethodInfoTable methodInfoTable;

    HuaResult(ConstantPool constantPool, VariableSymbolTable variableSymbolTable, MethodInfoTable methodInfoTable) {
        this.constantPool = constantPool;
        this.variableSymbolTable = variableSymbolTable;
        this.methodInfoTable = methodInfoTable;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public VariableSymbolTable getVariableSymbolTable() {
        return variableSymbolTable;
    }

    public MethodInfoTable getMethodInfoTable() {
        return methodInfoTable;
    }
}
