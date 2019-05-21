package com.github.liuyehcf.framework.expression.engine.core.io;


import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCodeUtils;
import com.google.common.collect.Lists;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Expression输入流
 *
 * @author hechenfeng
 * @date 2018/9/28
 */
public class ExpressionInputStream extends DataInputStream {

    public ExpressionInputStream(InputStream in) {
        super(in);
    }

    /**
     * 读出中间代码相关数据结构
     */
    public ExpressionCode readExpressionCode() throws IOException {
        /*
         *  读字节码
         */
        int byteCodeNum = readInt();
        List<ByteCode> byteCodes = Lists.newArrayList();
        for (int i = 0; i < byteCodeNum; i++) {
            byteCodes.add(readByteCode());
        }

        return new ExpressionCode(byteCodes);
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

    private ByteCode readByteCode() throws IOException {
        /*
         * 1. 读操作码
         */
        int operatorCode = readInt();

        Class<? extends ByteCode> byteCodeClass = ByteCodeUtils.getByteCodeByOperatorCode(operatorCode);

        /*
         * 2. 读操作数
         */
        Class<?>[] operatorClasses = ByteCodeUtils.getOperatorClasses(byteCodeClass);
        Object[] operators = new Object[operatorClasses.length];
        for (int i = 0; i < operatorClasses.length; i++) {
            Class<?> clazz = operatorClasses[i];

            if (String.class.equals(clazz)) {
                operators[i] = readString();
            } else if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
                operators[i] = readBoolean();
            } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
                operators[i] = readInt();
            } else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
                operators[i] = readLong();
            } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
                operators[i] = readDouble();
            } else {
                throw new IOException("unexpected operatorType='" + (clazz == null ? null : clazz.getName()) + "'");
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
