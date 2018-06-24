package org.liuyehcf.compile.engine.hua.bytecode;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 字节码抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public abstract class ByteCode {

    /**
     * 操作码，每个字节码对应唯一操作码
     */
    @JSONField(serialize = false)
    private final int operatorCode;

    /**
     * 操作数的数量
     */
    @JSONField(serialize = false)
    private final int operatorNum;

    /**
     * 每个操作数的字节数量
     */
    @JSONField(serialize = false)
    private final int[] operatorByteSize;

    public ByteCode(int operatorCode, int operatorNum, int[] operatorByteSize) {
        this.operatorCode = operatorCode;
        this.operatorNum = operatorNum;
        this.operatorByteSize = operatorByteSize;
    }

    public final String getName() {
        return getClass().getSimpleName();
    }

    public abstract void operate();

    public int getOperatorCode() {
        return operatorCode;
    }

    public int getOperatorNum() {
        return operatorNum;
    }

    public int[] getOperatorByteSize() {
        return operatorByteSize;
    }
}
