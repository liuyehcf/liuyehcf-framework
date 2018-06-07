package org.liuyehcf.compile.engine.hua.compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlu
 * @date 2018/6/6
 */
public class MethodInfoTable {

    private MethodInfo curMethodInfo;

    private final Map<MethodDescription, MethodInfo> table;

    public MethodInfoTable() {
        table = new HashMap<>(16);
    }

    public void enterMethod() {
        curMethodInfo = new MethodInfo();
    }

    public void exitMethod() {
        MethodDescription methodDescription = curMethodInfo.buildMethodDescription();
        table.put(methodDescription, curMethodInfo);
        curMethodInfo = null;
    }

    public void setMethodNameOfCurrentMethod(String methodName) {
        curMethodInfo.setMethodName(methodName);
    }

    public void addParamInfoToCurrentMethod(ParamInfo paramInfo) {
        curMethodInfo.addParamInfo(paramInfo);
    }
}
