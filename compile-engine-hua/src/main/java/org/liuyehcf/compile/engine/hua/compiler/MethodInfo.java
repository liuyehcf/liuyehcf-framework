package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/6
 */
public class MethodInfo {

    private int offset;

    private String methodName;

    private final List<ParamInfo> paramInfoList = new ArrayList<>();

    private final List<ByteCode> byteCodes = new ArrayList<>();

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getParamSize() {
        return paramInfoList.size();
    }

    public int getOffset() {
        return offset;
    }

    public List<ParamInfo> getParamInfoList() {
        return paramInfoList;
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void addParamInfo(ParamInfo paramInfo) {
        paramInfoList.add(paramInfo);
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    public MethodDescription buildMethodDescription() {
        String[] types = new String[paramInfoList.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = paramInfoList.get(i).getType();
        }
        return new MethodDescription(methodName, types);
    }
}
