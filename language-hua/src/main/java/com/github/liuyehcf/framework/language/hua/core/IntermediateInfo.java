package com.github.liuyehcf.framework.language.hua.core;

import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ByteCode;

import java.util.List;

/**
 * Hua编译后的中间形式
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class IntermediateInfo {

    /**
     * 常量池
     */
    private final ConstantPool constantPool;

    /**
     * 方法表
     */
    private final MethodInfoTable methodInfoTable;

    public IntermediateInfo(ConstantPool constantPool, MethodInfoTable methodInfoTable) {
        this.constantPool = constantPool;
        this.methodInfoTable = methodInfoTable;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public MethodInfoTable getMethodInfoTable() {
        return methodInfoTable;
    }

    public String toReadableString() {
        StringBuilder sb = new StringBuilder();

        appendConstantPool(sb);

        appendMethods(sb);

        return sb.toString();
    }

    private void appendConstantPool(StringBuilder sb) {
        List<String> constants = getConstantPool().getConstants();

        sb.append("Constant pool:").append('\n');
        for (int i = 0; i < constants.size(); i++) {
            sb.append(String.format("\t#%-3d%-3s%s\n", i, "=", constants.get(i)));
        }
        sb.append('\n');
    }

    private void appendMethods(StringBuilder sb) {
        List<MethodInfo> methodInfoList = getMethodInfoTable().getMethodInfoList();

        for (MethodInfo methodInfo : methodInfoList) {
            appendMethodInfo(methodInfo, sb);
        }
    }

    private void appendMethodInfo(MethodInfo methodInfo, StringBuilder sb) {
        sb.append(methodInfo.getMethodSignature().getSignature()).append('\n');
        sb.append("\tReturn type: ").append(methodInfo.getResultType().toTypeDescription()).append('\n');

        sb.append("\tParam type: ");
        List<Type> paramTypeList = methodInfo.getParamTypeList();
        for (int i = 0; i < paramTypeList.size(); i++) {
            Type paramType = paramTypeList.get(i);
            sb.append(paramType.toTypeDescription());
            if (i < paramTypeList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append('\n');

        sb.append("\tCode: ").append('\n');
        List<ByteCode> byteCodes = methodInfo.getByteCodes();
        for (int i = 0; i < byteCodes.size(); i++) {
            sb.append(String.format("\t\t%-4d%-3s%s\n", i, ":", byteCodes.get(i)));
        }

        sb.append('\n');
    }
}
