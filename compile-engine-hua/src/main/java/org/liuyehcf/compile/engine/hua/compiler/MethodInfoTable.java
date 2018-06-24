package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    MethodInfoTable() {
        table = new LinkedHashMap<>(16);
    }

    Map<MethodSignature, MethodInfo> getTable() {
        return table;
    }

    /**
     * 是否包含给定的方法
     *
     * @param methodSignature 方法签名
     * @return 是否包含
     */
    boolean containsMethod(MethodSignature methodSignature) {
        return table.containsKey(methodSignature);
    }

    /**
     * 根据方法签名查找方法信息
     *
     * @param methodSignature 方法签名
     * @return 方法信息
     */
    MethodInfo getMethodByMethodSignature(MethodSignature methodSignature) {
        return table.get(methodSignature);
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
     * 完成方法签名的扫描
     */
    void finishMethodDeclarator() {
        MethodSignature methodSignature = curMethodInfo.buildMethodSignature();
        table.put(methodSignature, curMethodInfo);
    }

    /**
     * 退出方法
     */
    void exitMethod() {
        curMethodInfo = null;
    }

    public List<MethodInfo> getMethodInfoList() {
        return new ArrayList<>(table.values());
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
