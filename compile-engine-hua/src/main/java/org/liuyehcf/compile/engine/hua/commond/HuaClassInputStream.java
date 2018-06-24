package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.compiler.ConstantPool;
import org.liuyehcf.compile.engine.hua.compiler.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfoTable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
public class HuaClassInputStream extends DataInputStream {
    HuaClassInputStream(InputStream in) {
        super(in);
    }

    /**
     * 读出中间代码相关数据结构
     */
    IntermediateInfo readHClass() throws IOException {
        String magic = readMagic();
        assertEquals(magic, HClassConstant.MAGIC);

        ConstantPool constantPool = readConstantPool();

        MethodInfoTable methodInfoTable = readMethodInfoTable();

        return new IntermediateInfo(constantPool, null, methodInfoTable);
    }

    /**
     * 读出魔数
     */
    private String readMagic() throws IOException {
        int magicLength = readInt();

        byte[] bytes = new byte[magicLength];

        int cnt = read(bytes);
        assertEquals(cnt, magicLength);

        return new String(bytes);
    }

    /**
     * 读出常量池
     */
    private ConstantPool readConstantPool() throws IOException {
        int constantSize = readInt();

        ConstantPool constantPool = new ConstantPool();

        for (int i = 0; i < constantSize; i++) {
            int length = readInt();

            byte[] bytes = new byte[length];

            int cnt = read(bytes);
            assertEquals(cnt, length);

            constantPool.addConstant(new String(bytes));
        }

        return constantPool;
    }

    /**
     * 读出方法表
     */
    private MethodInfoTable readMethodInfoTable() throws IOException {
        return null;
    }
}
