package org.liuyehcf.compile.engine.expression.core.bytecode.cf;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.expression.core.bytecode.ByteCode;

/**
 * 跳转指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/9/25
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

    ControlTransfer() {
    }

    ControlTransfer(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    public final int getCodeOffset() {
        return codeOffset;
    }

    public final void setCodeOffset(int codeOffset) {
        this.codeOffset = codeOffset;
    }

    @Override
    @JSONField(serialize = false)
    public final Object[] getOperators() {
        return new Object[]{codeOffset};
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + " " + getCodeOffset();
    }
}
