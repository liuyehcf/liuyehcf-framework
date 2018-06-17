package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * 字节码接口
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public interface ByteCode {

    default String getName() {
        return getClass().getSimpleName();
    }

    void operate();
}
