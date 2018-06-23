package org.liuyehcf.compile.engine.hua.compiler;

/**
 * Hua编译后的结果
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class HuaResult {

    /**
     * 符号表
     */
    private final VariableSymbolTable variableSymbolTable;

    /**
     * 方法表
     */
    private final MethodInfoTable methodInfoTable;

    public HuaResult(VariableSymbolTable variableSymbolTable, MethodInfoTable methodInfoTable) {
        this.variableSymbolTable = variableSymbolTable;
        this.methodInfoTable = methodInfoTable;
    }

    public VariableSymbolTable getVariableSymbolTable() {
        return variableSymbolTable;
    }

    public MethodInfoTable getMethodInfoTable() {
        return methodInfoTable;
    }
}
