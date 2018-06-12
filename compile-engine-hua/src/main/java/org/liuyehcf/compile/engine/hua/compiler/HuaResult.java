package org.liuyehcf.compile.engine.hua.compiler;

/**
 * @author chenlu
 * @date 2018/6/12
 */
public class HuaResult {
    private final VariableSymbolTable variableSymbolTable;

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
