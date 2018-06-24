package org.liuyehcf.compile.engine.hua.bytecode.sl;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 存储加载指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class StoreLoad extends ByteCode {
    public StoreLoad(int operatorCode) {
        super(operatorCode, 0, null);
    }

    public StoreLoad(int operatorCode, int operatorNum, int[] operatorByteSize) {
        super(operatorCode, operatorNum, operatorByteSize);
    }
}
