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

    /**
     * 字节码
     */
    private List<ByteCode> byteCodes = new ArrayList<>();

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

    /**
     * 符号偏移量
     */
    private int offset = 0;

    /**
     * 创建方法签名
     *
     * @param methodName 方法名字
     * @param types      参数类型列表
     * @return 方法签名
     */
    public static MethodSignature buildMethodSignature(String methodName, List<Type> types) {
        if (types == null || types.isEmpty()) {
            return new MethodSignature(methodName, null);
        }
        String[] typeStrings = new String[types.size()];
        for (int i = 0; i < typeStrings.length; i++) {
            typeStrings[i] = types.get(i).toTypeDescription();
        }
        return new MethodSignature(methodName, typeStrings);
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void setByteCodes(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
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

    public int getParamSize() {
        return paramTypeList.size();
    }

    void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    void increaseOffset(int step) {
        offset += step;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * 创建方法签名
     *
     * @return 方法签名
     */
    MethodSignature buildMethodSignature() {
        return buildMethodSignature(this.methodName, this.paramTypeList);
    }
}
