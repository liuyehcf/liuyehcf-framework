package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * 字节码抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public abstract class ByteCode {

    public final String getName() {
        return getClass().getSimpleName();
    }

    public abstract void operate();

    public abstract Object[] getOperators();

}
