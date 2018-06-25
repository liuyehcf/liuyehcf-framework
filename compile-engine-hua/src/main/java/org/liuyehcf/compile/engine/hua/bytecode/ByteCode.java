package org.liuyehcf.compile.engine.hua.bytecode;

import com.alibaba.fastjson.annotation.JSONField;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;

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
     * 操作数数量
     */
    @JSONField(serialize = false)
    private final int operatorNum;

    /**
     * 操作数类型
     */
    @JSONField(serialize = false)
    private final Class<?>[] operatorClasses;

    public ByteCode(int operatorCode, int operatorNum, Class<?>[] operatorClasses) {
        if (operatorClasses == null) {
            throw new NullPointerException();
        }
        this.operatorCode = operatorCode;
        this.operatorNum = operatorNum;
        this.operatorClasses = operatorClasses;

        assertEquals(this.operatorNum, this.operatorClasses.length);
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

    public Class<?>[] getOperatorClasses() {
        return operatorClasses;
    }

    public abstract Object[] getOperators();
}
