package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 方法表
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodInfoTable {

    @JSONField(serialize = false)
    private final Map<MethodDescription, MethodInfo> table;

    @JSONField(serialize = false)
    private MethodInfo curMethodInfo;

    MethodInfoTable() {
        table = new LinkedHashMap<>(16);
    }

    Map<MethodDescription, MethodInfo> getTable() {
        return table;
    }

    /**
     * 是否包含给定的方法
     *
     * @param methodDescription 方法描述符
     * @return 是否包含
     */
    boolean containsMethod(MethodDescription methodDescription) {
        return table.containsKey(methodDescription);
    }

    /**
     * 根据方法描述符查找方法信息
     *
     * @param methodDescription 方法描述符
     * @return 方法信息
     */
    MethodInfo getMethodByMethodDescription(MethodDescription methodDescription) {
        return table.get(methodDescription);
    }

    /**
     * 获取当前方法信息
     *
     * @return 方法信息
     */
    MethodInfo getCurMethodInfo() {
        return curMethodInfo;
    }

    /**
     * 进入方法
     */
    void enterMethod() {
        curMethodInfo = new MethodInfo();
    }

    /**
     * 完成方法描述符的扫描
     */
    void finishMethodDeclarator() {
        MethodDescription methodDescription = curMethodInfo.buildMethodDescription();
        table.put(methodDescription, curMethodInfo);
    }

    /**
     * 退出方法
     */
    void exitMethod() {
        curMethodInfo = null;
    }

    public String toSimpleString() {
        Map<String, Object> jsonMap = new LinkedHashMap<>();

        table.forEach((key, value) -> jsonMap.put(key.getDescription(), value.getByteCodes()));

        return JSON.toJSONString(jsonMap);
    }

    @Override
    public String toString() {
        Map<String, MethodInfo> tableJSONMap = new LinkedHashMap<>(16);
        table.forEach((key, value) -> tableJSONMap.put(key.getDescription(), value));
        return JSON.toJSONString(tableJSONMap);
    }
}
