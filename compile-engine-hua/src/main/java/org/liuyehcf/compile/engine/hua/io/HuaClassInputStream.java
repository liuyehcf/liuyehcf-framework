package org.liuyehcf.compile.engine.hua.io;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCodeUtil;
import org.liuyehcf.compile.engine.hua.core.ConstantPool;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfoTable;
import org.liuyehcf.compile.engine.hua.definition.model.Type;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
public class HuaClassInputStream extends DataInputStream {

    public HuaClassInputStream(InputStream in) {
        super(in);
    }

    /**
     * 读出中间代码相关数据结构
     */
    public IntermediateInfo readHClass() throws IOException {
        /*
         * 1. 读魔数
         */
        String magic = readString();
        if (!HClassConstant.MAGIC.equals(magic)) {
            throw new IOException();
        }

        /*
         * 2. 读魔数
         */
        ConstantPool constantPool = readConstantPool();

        /*
         * 3. 读魔数
         */
        MethodInfoTable methodInfoTable = readMethodInfoTable();

        return new IntermediateInfo(constantPool, null, methodInfoTable);
    }

    /**
     * 读出常量池
     */
    private ConstantPool readConstantPool() throws IOException {

        /*
         * 1. 读常量池大小
         */
        int constantSize = readInt();

        ConstantPool constantPool = new ConstantPool();

        /*
         * 2. 读常量
         */
        for (int i = 0; i < constantSize; i++) {
            constantPool.addConstant(readString());
        }

        return constantPool;
    }

    /**
     * 读出方法表
     */
    private MethodInfoTable readMethodInfoTable() throws IOException {

        /*
         * 1. 读方法数量
         */
        int methodSize = readInt();

        List<MethodInfo> methodInfoList = new ArrayList<>();

        /*
         * 2. 读方法信息
         */
        for (int i = 0; i < methodSize; i++) {
            methodInfoList.add(readMethodInfo());
        }

        return new MethodInfoTable(methodInfoList);
    }

    private MethodInfo readMethodInfo() throws IOException {
        /*
         * 1. 读方法名字
         */
        String methodName = readString();

        /*
         * 2. 读返回类型
         */
        Type resultType = readType();

        /*
         * 3. 读方法参数类型列表
         */
        int paramSize = readInt();
        List<Type> paramTypeList = new ArrayList<>();
        for (int i = 0; i < paramSize; i++) {
            paramTypeList.add(readType());
        }

        /*
         * 4. 读偏移量
         */
        int maxOffset = readInt();

        /*
         * 5. 读字节码
         */
        int byteCodeNum = readInt();
        List<ByteCode> byteCodeList = new ArrayList<>();
        for (int i = 0; i < byteCodeNum; i++) {
            byteCodeList.add(readByteCode());
        }

        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setMethodName(methodName);
        methodInfo.setResultType(resultType);
        methodInfo.setParamTypeList(paramTypeList);
        methodInfo.setMaxOffset(maxOffset);
        methodInfo.setByteCodes(byteCodeList);

        return methodInfo;
    }

    private String readString() throws IOException {
        int len = readInt();
        byte[] bytes = new byte[len];
        int cnt = read(bytes);
        if (cnt != len) {
            throw new IOException();
        }
        return new String(bytes);
    }

    private Type readType() throws IOException {
        /*
         * 1. 读类型名
         */
        String typeName = readString();

        /*
         * 2. 读类型宽度
         */
        int typeWidth = readInt();

        /*
         * 3. 读维度
         */
        int dim = readInt();

        return Type.createType(typeName, typeWidth, dim);
    }

    private ByteCode readByteCode() throws IOException {
        /*
         * 1. 读操作码
         */
        int operatorCode = readInt();

        Class<? extends ByteCode> byteCodeClass = ByteCodeUtil.getByteCodeByOperatorCode(operatorCode);

        /*
         * 2. 读操作数
         */
        Class<?>[] operatorClasses = ByteCodeUtil.getOperatorClasses(byteCodeClass);
        Object[] operators = new Object[operatorClasses.length];
        for (int i = 0; i < operatorClasses.length; i++) {
            Class<?> clazz = operatorClasses[i];

            if (String.class.equals(clazz)) {
                operators[i] = readString();
            } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
                operators[i] = readInt();
            } else {
                throw new IOException();
            }
        }

        /*
         * 3. 创建字节码对象
         */
        try {
            Constructor<? extends ByteCode> constructor = byteCodeClass.getConstructor(operatorClasses);
            return constructor.newInstance(operators);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IOException(e);
        }
    }
}
