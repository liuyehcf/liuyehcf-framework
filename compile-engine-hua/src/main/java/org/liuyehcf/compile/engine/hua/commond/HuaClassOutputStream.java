package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.compiler.Namespace;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.liuyehcf.compile.engine.hua.commond.HClassConstant.MAGIC;
import static org.liuyehcf.compile.engine.hua.commond.HClassConstant.MAGIC_LENGHT;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
public class HuaClassOutputStream extends BufferedOutputStream {
    public HuaClassOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * 写魔数
     */
    public void writeMagic() throws IOException {
        byte[] bytes = new byte[MAGIC_LENGHT];
        byte[] magicBytes = MAGIC.getBytes();

        System.arraycopy(magicBytes, 0, bytes, 0, magicBytes.length);

        write(bytes);
    }

    /**
     * 写入VariableSymbol
     *
     * @param variableSymbol 符号
     */
    public void writeVariableSymbol(VariableSymbol variableSymbol) throws IOException {

    }

    /**
     * 写入Namespace
     *
     * @param namespace 命名空间
     */
    public void writeNamespace(Namespace namespace) throws IOException {

    }
}
