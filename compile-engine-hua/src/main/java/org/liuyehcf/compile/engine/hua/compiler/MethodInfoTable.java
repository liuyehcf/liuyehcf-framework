package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

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

    public MethodInfo getCurMethodInfo() {
        return curMethodInfo;
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

    public Map<String, MethodInfo> getJSONTable() {
        Map<String, MethodInfo> tableJSONMap = new HashMap<>(16);
        table.forEach(
                (key, value) -> {
                    tableJSONMap.put(key.getDescription(), value);
                }
        );
        return tableJSONMap;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

}
