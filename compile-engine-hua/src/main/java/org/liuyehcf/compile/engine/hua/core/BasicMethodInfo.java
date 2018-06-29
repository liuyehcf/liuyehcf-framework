package org.liuyehcf.compile.engine.hua.core;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;

import java.util.List;

/**
 * 方法基本信息
 *
 * @author hechenfeng
 * @date 2018/6/29
 */
public class BasicMethodInfo {
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 返回类型
     */
    private Type resultType;

    /**
     * 参数类型
     */
    private List<Type> paramTypeList;

    BasicMethodInfo() {
    }

    BasicMethodInfo(String methodName, Type resultType, List<Type> paramTypeList) {
        this.methodName = methodName;
        this.resultType = resultType;
        this.paramTypeList = paramTypeList;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Type getResultType() {
        return resultType;
    }

    public void setResultType(Type resultType) {
        this.resultType = resultType;
    }

    public List<Type> getParamTypeList() {
        return paramTypeList;
    }

    public void setParamTypeList(List<Type> paramTypeList) {
        this.paramTypeList = paramTypeList;
    }
}
