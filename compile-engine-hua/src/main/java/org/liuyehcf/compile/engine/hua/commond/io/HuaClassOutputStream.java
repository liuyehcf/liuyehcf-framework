package org.liuyehcf.compile.engine.hua.commond.io;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCodeUtil;
import org.liuyehcf.compile.engine.hua.compiler.ConstantPool;
import org.liuyehcf.compile.engine.hua.compiler.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfoTable;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.liuyehcf.compile.engine.hua.commond.io.HClassConstant.MAGIC;


/**
 * @author hechenfeng
 * @date 2018/6/24
 */
public class HuaClassOutputStream extends DataOutputStream {

    public HuaClassOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * 写入中间代码
     */
    public void writeHClass(IntermediateInfo intermediateInfo) throws IOException {
        /*
         * 1. 写魔数
         */
        writeString(MAGIC);

        /*
         * 2. 写常量池
         */
        writeConstantPoll(intermediateInfo.getConstantPool());

        /*
         * 3. 写方法表
         */
        writeMethodInfoTable(intermediateInfo.getMethodInfoTable());
    }

    /**
     * 写入常量池
     *
     * @param constantPool 常量池
     */
    private void writeConstantPoll(ConstantPool constantPool) throws IOException {
        /*
         * 1. 写入常量个数
         */
        writeInt(constantPool.getConstants().size());

        /*
         * 2. 写常量
         */
        for (String constant : constantPool.getConstants()) {
            writeString(constant);
        }
    }

    /**
     * 写入方法表
     *
     * @param methodInfoTable 方法表
     */
    private void writeMethodInfoTable(MethodInfoTable methodInfoTable) throws IOException {

        /*
         * 1. 写入方法个数
         */
        writeInt(methodInfoTable.getMethodInfoList().size());

        /*
         * 2. 写方法信息
         */
        for (MethodInfo methodInfo : methodInfoTable.getMethodInfoList()) {
            writeMethodInfo(methodInfo);
        }
    }

    /**
     * 写入方法信息
     *
     * @param methodInfo 方法信息
     */
    private void writeMethodInfo(MethodInfo methodInfo) throws IOException {
        /*
         * 1. 写方法名字
         */
        writeString(methodInfo.getMethodName());

        /*
         * 2. 写返回类型
         */
        writeType(methodInfo.getResultType());

        /*
         * 3. 写方法参数类型列表
         */
        writeInt(methodInfo.getParamTypeList().size());
        for (int i = 0; i < methodInfo.getParamTypeList().size(); i++) {
            writeType(methodInfo.getParamTypeList().get(i));
        }

        /*
         * 4. 写偏移量
         */
        writeInt(methodInfo.getOffset());

        /*
         * 5. 写字节码
         */
        writeInt(methodInfo.getByteCodes().size());
        for (int i = 0; i < methodInfo.getByteCodes().size(); i++) {
            writeByteCode(methodInfo.getByteCodes().get(i));
        }
    }

    private void writeString(String s) throws IOException {
        writeInt(s.length());
        write(s.getBytes());
    }

    private void writeType(Type type) throws IOException {
        /*
         * 1. 写类型名
         */
        writeString(type.getTypeName());

        /*
         * 2. 写类型宽度
         */
        writeInt(type.getTypeWidth());

        /*
         * 3. 写维度
         */
        writeInt(type.getDim());
    }

    private void writeByteCode(ByteCode code) throws IOException {
        int operatorCode = ByteCodeUtil.getOperatorCode(code.getClass());
        Class<?>[] operatorClasses = ByteCodeUtil.getOperatorClasses(code.getClass());
        int operatorNum = operatorClasses.length;
        Object[] operators = code.getOperators();

        /*
         * 1. 写操作码
         */
        writeInt(operatorCode);

        /*
         * 2. 写操作数
         */
        for (int i = 0; i < operatorNum; i++) {
            Class<?> clazz = operatorClasses[i];
            if (String.class.equals(clazz)) {
                writeString((String) operators[i]);
            } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
                writeInt((int) operators[i]);
            } else {
                throw new IOException();
            }
        }
    }
}
