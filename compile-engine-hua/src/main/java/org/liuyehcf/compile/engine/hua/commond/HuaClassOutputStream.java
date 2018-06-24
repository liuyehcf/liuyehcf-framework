package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfoTable;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.liuyehcf.compile.engine.hua.commond.HClassConstant.MAGIC;
import static org.liuyehcf.compile.engine.hua.commond.HClassConstant.MAGIC_LENGHT;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
class HuaClassOutputStream extends BufferedOutputStream {

    HuaClassOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * 写魔数
     */
    void writeMagic() throws IOException {
        byte[] bytes = new byte[MAGIC_LENGHT];
        byte[] magicBytes = MAGIC.getBytes();

        System.arraycopy(magicBytes, 0, bytes, 0, magicBytes.length);

        write(bytes);
    }

    /**
     * 写入MethodInfoTable
     *
     * @param methodInfoTable 方法表
     */
    void writeMethodInfoTable(MethodInfoTable methodInfoTable) throws IOException {

        for (MethodInfo methodInfo : methodInfoTable.getMethodInfoList()) {

        }
    }
}
