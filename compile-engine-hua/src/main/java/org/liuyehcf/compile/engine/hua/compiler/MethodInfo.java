package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法信息
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodInfo {

    private final List<ByteCode> byteCodes = new ArrayList<>();
    private int offset;
    private String methodName;
    private Type resultType;
    private List<Type> paramTypeList;

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
        return paramTypeList.size();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Type> getParamTypeList() {
        return paramTypeList;
    }

    public void setParamTypeList(List<Type> paramTypeList) {
        this.paramTypeList = paramTypeList;
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    public MethodDescription buildMethodDescription() {
        String[] types = new String[paramTypeList.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = paramTypeList.get(i).toTypeDescription();
        }
        return new MethodDescription(methodName, types);
    }
}
