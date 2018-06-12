package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/6
 */
public class MethodInfo {

    private final List<ByteCode> byteCodes = new ArrayList<>();
    private int offset;
    private String methodName;
    private Type resultType;
    private List<Type> paramInfoList;

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

    public int getParamSize() {
        return paramInfoList.size();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Type> getParamInfoList() {
        return paramInfoList;
    }

    public void setParamInfoList(List<Type> paramInfoList) {
        this.paramInfoList = paramInfoList;
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    public MethodDescription buildMethodDescription() {
        String[] types = new String[paramInfoList.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = paramInfoList.get(i).toTypeDescription();
        }
        return new MethodDescription(methodName, types);
    }
}
