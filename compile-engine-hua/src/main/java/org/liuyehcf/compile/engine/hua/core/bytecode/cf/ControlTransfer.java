package org.liuyehcf.compile.engine.hua.core.bytecode.cf;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

/**
 * 跳转指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class ControlTransfer extends ByteCode {

    /**
     * 偏移量初始值
     */
    private static final int UNINITIALIZED = -1;

    /**
     * 代码偏移量
     */
    private int codeOffset = UNINITIALIZED;

    public ControlTransfer() {
    }

    public ControlTransfer(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    public int getCodeOffset() {
        return codeOffset;
    }

    public void setCodeOffset(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{codeOffset};
    }
}
