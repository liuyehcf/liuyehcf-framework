package org.liuyehcf.compile.engine.hua.bytecode.cf;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

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
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 1;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 代码偏移量
     */
    private int codeOffset = UNINITIALIZED;

    public ControlTransfer(int operatorCode) {
        super(operatorCode, OPERATOR_NUM, OPERATOR_CLASSES);
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
