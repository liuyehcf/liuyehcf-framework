package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodInfoTable {

    private final Map<MethodDescription, MethodInfo> table;
    private MethodInfo curMethodInfo;

    public MethodInfoTable() {
        table = new LinkedHashMap<>(16);
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

    public Map<String, MethodInfo> getJSONTable() {
        Map<String, MethodInfo> tableJSONMap = new LinkedHashMap<>(16);
        table.forEach((key, value) -> tableJSONMap.put(key.getDescription(), value));
        return tableJSONMap;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

}
