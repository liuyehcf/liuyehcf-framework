package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.compiler.ConstantPool;
import org.liuyehcf.compile.engine.hua.compiler.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfoTable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.liuyehcf.compile.engine.hua.commond.HClassConstant.MAGIC;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
class HuaClassOutputStream extends DataOutputStream {

    HuaClassOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * 写入中间代码
     */
    void writeHClass(IntermediateInfo intermediateInfo) throws IOException {
        /*
         * 1. 写魔数
         */
        writeMagic();

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
     * 写魔数
     */
    private void writeMagic() throws IOException {
        byte[] bytes = MAGIC.getBytes();

        /*
         * 写魔数长度
         */
        writeInt(bytes.length);

        /*
         * 写魔术
         */
        write(bytes);
    }

    /**
     * 写入常量池
     *
     * @param constantPool 常量池
     */
    private void writeConstantPoll(ConstantPool constantPool) throws IOException {
        /*
         * 写入常量个数
         */
        writeInt(constantPool.getConstants().size());

        for (String constant : constantPool.getConstants()) {
            byte[] bytes = constant.getBytes();

            /*
             * 写常量的长度
             */
            writeInt(bytes.length);

            /*
             * 写常量
             */
            write(bytes);
        }
    }

    /**
     * 写入方法表
     *
     * @param methodInfoTable 方法表
     */
    private void writeMethodInfoTable(MethodInfoTable methodInfoTable) throws IOException {

        /*
         * 写入方法个数
         */
        writeInt(methodInfoTable.getMethodInfoList().size());

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

    }
}
