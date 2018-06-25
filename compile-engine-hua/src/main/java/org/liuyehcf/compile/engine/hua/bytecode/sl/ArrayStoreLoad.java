package org.liuyehcf.compile.engine.hua.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * @author chenlu
 * @date 2018/6/25
 */
public abstract class ArrayStoreLoad extends ByteCode {

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 0;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[0];

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    public ArrayStoreLoad(int operatorCode) {
        super(operatorCode, OPERATOR_NUM, OPERATOR_CLASSES);
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return OPERATORS;
    }
}
