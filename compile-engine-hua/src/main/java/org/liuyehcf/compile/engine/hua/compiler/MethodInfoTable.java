package org.liuyehcf.compile.engine.hua.compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlu
 * @date 2018/6/6
 */
public class MethodInfoTable {

    private final Map<MethodDescription, MethodInfo> table;

    public MethodInfoTable() {
        table = new HashMap<>(16);
    }

    public void enterMethod(String name, int paramNum, String[] types) {

    }

    public void exitMethod() {

    }
}
