package com.github.liuyehcf.framework.expression.engine.core.io;


import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCodeUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Expression输出流
 *
 * @author hechenfeng
 * @date 2018/9/28
 */
public class ExpressionOutputStream extends DataOutputStream {

    public ExpressionOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * 写入中间代码
     */
    public void writeExpressionCode(ExpressionCode expressionCode) throws IOException {
        /*
         *  写字节码
         */
        writeInt(expressionCode.getByteCodes().size());
        for (int i = 0; i < expressionCode.getByteCodes().size(); i++) {
            writeByteCode(expressionCode.getByteCodes().get(i));
        }
    }

    private void writeString(String s) throws IOException {
        byte[] bytes = s.getBytes();
        writeInt(bytes.length);
        write(bytes);
    }

    private void writeByteCode(ByteCode byteCode) throws IOException {
        int operatorCode = ByteCodeUtils.getOperatorCode(byteCode.getClass());
        Class<?>[] operatorClasses = ByteCodeUtils.getOperatorClasses(byteCode.getClass());
        int operatorNum = operatorClasses.length;
        Object[] operators = byteCode.getOperators();

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
            } else if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
                writeBoolean((boolean) operators[i]);
            } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
                writeInt((int) operators[i]);
            } else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
                writeLong((long) operators[i]);
            } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
                writeDouble((double) operators[i]);
            } else {
                throw new IOException("unexpected operatorType='" + (clazz == null ? null : clazz.getName()) + "'");
            }
        }
    }
}
