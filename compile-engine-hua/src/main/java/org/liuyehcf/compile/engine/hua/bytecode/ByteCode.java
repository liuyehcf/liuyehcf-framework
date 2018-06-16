package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public interface ByteCode {

    default String getName() {
        return getClass().getSimpleName();
    }

    void operate();
}
