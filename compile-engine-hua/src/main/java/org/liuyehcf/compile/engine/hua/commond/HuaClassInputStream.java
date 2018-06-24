package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.compiler.Namespace;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
public class HuaClassInputStream extends BufferedInputStream {
    public HuaClassInputStream(InputStream in) {
        super(in);
    }

    /**
     * 读入一个VariableSymbol
     *
     * @return 符号
     */
    public VariableSymbol readVariableSymbol() throws IOException {
        return null;
    }

    /**
     * 读入一个Namespace
     *
     * @return 命名空间
     */
    public Namespace readNamespace() throws IOException {
        return null;
    }
}
