package org.liuyehcf.compile.engine.hua.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.liuyehcf.compile.engine.hua.core.SystemMethod.SYSTEM_METHOD_POOL;

/**
 * 方法表
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodInfoTable {

    /**
     * 方法签名 -> 方法信息
     */
    @JSONField(serialize = false)
    private final Map<MethodSignature, MethodInfo> table;

    /**
     * 当前方法信息
     */
    @JSONField(serialize = false)
    private MethodInfo curMethodInfo;

    public MethodInfoTable() {
        table = new LinkedHashMap<>(16);
    }

    public MethodInfoTable(List<MethodInfo> methodInfoList) {
        table = new LinkedHashMap<>(16);

        for (MethodInfo methodInfo : methodInfoList) {
            table.put(methodInfo.buildMethodSignature(), methodInfo);
        }
    }

    public Map<MethodSignature, MethodInfo> getTable() {
        return table;
    }

    /**
     * 是否包含给定的方法
     *
     * @param methodSignature 方法签名
     * @return 是否包含
     */
    public boolean containsMethod(MethodSignature methodSignature) {
        return SYSTEM_METHOD_POOL.containsKey(methodSignature) || table.containsKey(methodSignature);
    }

    /**
     * 根据方法签名查找方法信息
     *
     * @param methodSignature 方法签名
     * @return 方法信息
     */
    public MethodInfo getMethodByMethodSignature(MethodSignature methodSignature) {
        if (SYSTEM_METHOD_POOL.containsKey(methodSignature)) {
            return SYSTEM_METHOD_POOL.get(methodSignature).getFirst();
        }
        return table.get(methodSignature);
    }

    /**
     * 获取当前方法信息
     *
     * @return 方法信息
     */
    public MethodInfo getCurMethodInfo() {
        return curMethodInfo;
    }

    /**
     * 进入方法
     */
    public void enterMethod() {
        curMethodInfo = new MethodInfo();
    }

    /**
     * 完成方法签名的扫描
     */
    public MethodSignature finishMethodDeclarator() {
        MethodSignature methodSignature = curMethodInfo.buildMethodSignature();
        checkIfExists(methodSignature);
        table.put(methodSignature, curMethodInfo);
        return methodSignature;
    }

    private void checkIfExists(MethodSignature methodSignature) {
        if (SYSTEM_METHOD_POOL.containsKey(methodSignature)) {
            throw new RuntimeException(methodSignature.getSignature() + " is System method");
        }
        if (table.containsKey(methodSignature)) {
            throw new RuntimeException(methodSignature.getSignature() + " has been already defined");
        }
    }

    /**
     * 退出方法
     */
    public void exitMethod() {
        curMethodInfo = null;
    }

    public List<MethodInfo> getMethodInfoList() {
        return new ArrayList<>(table.values());
    }

    public boolean isSystemMethod(MethodSignature signature) {
        return SYSTEM_METHOD_POOL.containsKey(signature);
    }

    public String toSimpleString() {
        Map<String, Object> jsonMap = new LinkedHashMap<>();

        table.forEach((key, value) -> jsonMap.put(key.getSignature(), value.getByteCodes()));

        return JSON.toJSONString(jsonMap, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String toString() {
        Map<String, MethodInfo> tableJSONMap = new LinkedHashMap<>(16);
        table.forEach((key, value) -> tableJSONMap.put(key.getSignature(), value));
        return JSON.toJSONString(tableJSONMap, SerializerFeature.DisableCircularReferenceDetect);
    }
}
